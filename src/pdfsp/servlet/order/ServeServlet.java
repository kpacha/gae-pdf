package pdfsp.servlet.order;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.management.Query;

import pdfsp.model.Order;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.model.exception.PdfNotCreatedYetException;
import pdfsp.servlet.BaseServlet;
import pdfsp.util.PMF;

/**
 * Serves the pdf and forces to display it in the browser
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class ServeServlet extends BaseServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	    throws IOException {
	this.doServe(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
	    throws IOException {
	this.doServe(req, res);
    }

    protected void doServe(HttpServletRequest req, HttpServletResponse res)
	    throws IOException {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Order order = null;
	byte[] pdfBytes = null;
	String contentType = "application/pdf";
	String hash = URLEncoder.encode(req.getParameter("k"), "UTF-8");
	try {
	    Query query = pm.newQuery(Order.class);
	    query.setFilter("hash == hashParam");
	    query.declareParameters("String hashParam");
	    List<Order> results = (List<Order>) query.execute(hash);
	    if (results.iterator().hasNext()) {
		for (Order search : results) {
		    order = search;
		}
		pdfBytes = order.getAPI().getPdf().getBytes();
		res.setContentType(contentType);
		res.getOutputStream().write(pdfBytes);
	    } else {
		System.out.print("Error serving file : " + hash);
		res.sendError(404);
	    }
	} catch (InvalidUserKeyException e) {
	    System.out.print("Error serving file : " + e.getMessage() + "\n");
	    res.sendError(404);
	} catch (ExpirationException e) {
	    System.out.print("Error serving file : " + e.getMessage() + "\n");
	    res.sendError(404);
	} catch (PdfNotCreatedYetException e) {
	    System.out.print("Error serving file : " + e.getMessage() + "\n");
	    res.sendError(404);
	} finally {
	    pm.close();
	}
    }

}
