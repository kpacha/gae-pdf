package pdfsp.servlet.order;

import java.io.IOException;
import java.util.List;

import javax.management.Query;

import pdfsp.api.OrderAPI;
import pdfsp.model.Order;
import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.model.exception.OrderNotFoundException;
import pdfsp.model.exception.PdfNotCreatedYetException;
import pdfsp.servlet.BaseServlet;
import pdfsp.util.PMF;

/**
 * Gets the order and trigger the encoder
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class EncoderServlet extends BaseServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	this.doEncode(req, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	this.doEncode(req, response);
    }

    public void doEncode(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	OrderAPI orderApi = null;
	try {
	    Query query = pm.newQuery(Order.class);
	    query.setFilter("hash == hashParam");
	    query.declareParameters("String hashParam");
	    List<Order> results = (List<Order>) query.execute(req
		    .getParameter("k"));
	    if (!results.iterator().hasNext()) {
		throw new OrderNotFoundException();
	    }
	    for (Order search : results) {
		orderApi = new OrderAPI(search);
	    }
	    if (orderApi.encodePdf() == 0) {
		throw new PdfNotCreatedYetException("Empty pdf!");
	    }
	    pm.makePersistent(orderApi.getOrder());
	    response.getWriter().println(orderApi.getOrder().getHash());
	} catch (PdfNotCreatedYetException e) {
	    System.out.print(e.getMessage());
	} catch (ExpirationException e) {
	    System.out.print(e.getMessage());
	} catch (DocumentException e) {
	    System.out.print(e.getMessage());
	} catch (OrderNotFoundException e) {
	    System.out.print(e.getMessage());
	} catch (InvalidUserKeyException e) {
	    System.out.print(e.getMessage());
	} catch (EmptyHtmlSourceException e) {
	    System.out.print(e.getMessage());
	} catch (EmptyBodyException e) {
	    System.out.print(e.getMessage());
	} finally {
	    pm.close();
	}
    }
}
