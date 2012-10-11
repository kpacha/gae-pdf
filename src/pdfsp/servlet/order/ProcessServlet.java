package pdfsp.servlet.order;

import java.io.IOException;

import pdfsp.api.OrderAPI;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.servlet.BaseServlet;
import pdfsp.util.PMF;

/**
 * Creates a new order with the received html and enqueues the conversion
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
	Text html = new Text(req.getParameter("html"));
	String userKey = req.getParameter("userKey");
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    OrderAPI orderApi = new OrderAPI(userKey, html);
	    pm.makePersistent(orderApi.getOrder());
	    response.getWriter().println(orderApi.getOrder().getHash());
	    orderApi.queueEncode();
	} catch (InvalidUserKeyException e) {
	    System.out.print(e.getMessage());
	} catch (EmptyHtmlSourceException e) {
	    System.out.print(e.getMessage());
	} finally {
	    pm.close();
	}
    }
}
