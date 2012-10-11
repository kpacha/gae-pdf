package pdfsp.servlet.order;

import java.io.IOException;
import java.util.List;

import pdfsp.api.OrderAPI;
import pdfsp.model.Order;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.servlet.CRUDServlet;

/**
 * Controller who implements the CRUD of order api
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class OrderServlet extends CRUDServlet {

    /**
     * Create a new order
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    @Override
    protected void doCreate(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	Text html = new Text(req.getParameter("html"));
	String userKey = this.encode(req.getParameter("userKey"));
	try {
	    OrderAPI orderApi = new OrderAPI(userKey, html);
	    pm.makePersistent(orderApi.getOrder());
	    this.jsonResponse.put("success", true);
	    this.jsonResponse.put("hash", orderApi.getOrder().getHash());
	} catch (InvalidUserKeyException e) {
	    this.doPrintErrorJson(e.getMessage());
	} catch (EmptyHtmlSourceException e) {
	    this.doPrintErrorJson(e.getMessage());
	}
    }

    /**
     * List order(s)
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    @Override
    protected void doRead(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	String userKey = this.encode(req.getParameter("userKey"));
	String hash = this.encode(req.getParameter("k"));

	List<Order> results = null;
	// search the template
	if (hash == null || hash.equalsIgnoreCase("")) {
	    results = OrderAPI.select(userKey, pm);
	} else {
	    results = OrderAPI.select(userKey, hash, pm);
	}
	java.util.Iterator<Order> iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Orders entries found!");
	}

	JSONArray list = new JSONArray();
	while (iter.hasNext()) {
	    try {
		OrderAPI orderApi = new OrderAPI(iter.next());
		list.add(orderApi.toJson());
	    } catch (JSONException e) {
		// do nothing!!
	    } catch (InvalidUserKeyException e) {
		// stop looping, the user is invalid for some unknown reason
		break;
	    }
	}
	this.doPrintSuccessJson(list);
    }

    /**
     * Update a order
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    @Override
    protected void doUpdate(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	String userKey = this.encode(req.getParameter("userKey"));
	String hash = this.encode(req.getParameter("k"));
	String html = req.getParameter("html");

	List<Order> results = OrderAPI.select(userKey, hash, pm);
	java.util.Iterator<Order> iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Order entries found!");
	}

	try {
	    JSONArray list = new JSONArray();
	    while (iter.hasNext()) {
		OrderAPI orderApi = new OrderAPI(iter.next());
		orderApi.setSource(html);
		pm.makePersistent(orderApi.getOrder());
	    }
	    this.doPrintSuccessJson(list);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    // send error
	    this.doPrintErrorJson(e.getMessage());
	}
    }

    /**
     * Delete a order
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    @Override
    protected void doRemove(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	String userKey = this.encode(req.getParameter("userKey"));
	String hash = this.encode(req.getParameter("k"));

	List<Order> results = OrderAPI.select(userKey, hash, pm);
	// java.util.Iterator<Order> iter = results.iterator();
	java.util.Iterator iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Order entries found!");
	}

	try {
	    JSONArray list = new JSONArray();
	    // OrderAPI orderApi = null;
	    // while (iter.hasNext()) {
	    // orderApi = new OrderAPI(iter.next());
	    // pm.deletePersistent(orderApi.getOrder());
	    // }
	    // if (!OrderAPI.deleteOrders(iter, pm)) {

	    if (!OrderAPI.deleteObjects(iter, pm)) {
		throw new Exception("Error deleting orders!");
	    }
	    this.doPrintSuccessJson(list);
	    // } catch (InvalidUserKeyException e) {
	    // // send error
	    // this.doPrintErrorJson(e.getMessage());
	} catch (Exception e) {
	    // send error
	    this.doPrintErrorJson(e.getMessage());
	}
    }
}
