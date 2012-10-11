package pdfsp.servlet.template;

import java.io.IOException;
import java.util.List;

import javax.management.Query;

import pdfsp.api.OrderAPI;
import pdfsp.api.TemplateAPI;
import pdfsp.model.Template;
import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.EmptyTemplateParametersException;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.model.exception.TemplateNotFoundException;
import pdfsp.servlet.BaseServlet;
import pdfsp.util.PMF;

/**
 * Creates a new order with the received params and a template. Then, enqueues
 * the order conversion
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class ProcessServlet extends BaseServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	this.doProcess(req, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	this.doProcess(req, response);
    }

    public void doProcess(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	// Extract the parameters
	String userKey = req.getParameter("userKey");
	String params = req.getParameter("params");

	TemplateAPI templateApi = null;
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    // search the template
	    Query query = pm.newQuery(Template.class);
	    query.setFilter("hash == hashParam");
	    query.declareParameters("String hashParam");
	    List<Template> results = (List<Template>) query.execute(req
		    .getParameter("k"));
	    if (!results.iterator().hasNext()) {
		throw new TemplateNotFoundException();
	    }
	    for (Template search : results) {
		templateApi = new TemplateAPI(search);
	    }
	    // get a new order with the received params and the template
	    OrderAPI orderApi = templateApi.createOrder(params);
	    pm.makePersistent(orderApi.getOrder());
	    response.getWriter().println(orderApi.getOrder().getHash());
	    orderApi.queueEncode();
	} catch (TemplateNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidUserKeyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (EmptyHtmlSourceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ExpirationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (EmptyTemplateParametersException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (EmptyBodyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    pm.close();
	}
    }
}
