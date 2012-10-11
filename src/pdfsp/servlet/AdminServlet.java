package pdfsp.servlet;

import java.io.IOException;

import pdfsp.api.OrderAPI;
import pdfsp.api.TemplateAPI;
import pdfsp.model.exception.InvalidActionException;
import pdfsp.util.PMF;

public class AdminServlet extends BaseServlet {

    private static final int DELETE_ORDERS = 1;
    private static final String DELETE_ORDER_STRING = "deleteOrders";
    private static final int DELETE_TEMPLATES = 2;
    private static final String DELETE_TEMPLATE_STRING = "deleteTemplates";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	// TODO validate the user
	this.doAction(req, response);
    }

    protected void doAction(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	pm = PMF.get().getPersistenceManager();
	String action = getServletConfig().getInitParameter("action");
	try {
	    switch (this.getActionId(action)) {
	    // case CRUDServlet.CREATE:
	    // this.doCreate(req, response);
	    // break;
	    // case CRUDServlet.READ:
	    // this.doRead(req, response);
	    // break;
	    // case CRUDServlet.UPDATE:
	    // this.doUpdate(req, response);
	    // break;
	    case AdminServlet.DELETE_ORDERS:
		this.doRemoveOrders(req, response);
		break;
	    case AdminServlet.DELETE_TEMPLATES:
		this.doRemoveTemplates(req, response);
		break;
	    }
	} catch (InvalidActionException e) {
	    this.doPrintErrorJson(e.getMessage());
	}
	this.doPrintJson(req, response);
	pm.close();
    }

    private void doRemoveTemplates(HttpServletRequest req,
	    HttpServletResponse response) {
	if (!TemplateAPI.deleteTemplates(TemplateAPI.select(pm), pm)) {
	    this.doPrintErrorJson("Error deleting templates");
	} else {
	    this.doPrintSuccessJson(new JSONArray());
	}
    }

    private void doRemoveOrders(HttpServletRequest req,
	    HttpServletResponse response) {
	if (!OrderAPI.deleteOrders(OrderAPI.select(pm), pm)) {
	    this.doPrintErrorJson("Error deleting orders");
	} else {
	    this.doPrintSuccessJson(new JSONArray());
	}
    }

    /**
     * Map the action
     * 
     * @param action
     * @return the action id from the received action string
     * @throws InvalidActionException
     */
    protected int getActionId(String action) throws InvalidActionException {
	if (action == null || action.equalsIgnoreCase("")) {
	    throw new InvalidActionException(action);
	}
	if (action.equalsIgnoreCase(AdminServlet.DELETE_ORDER_STRING)) {
	    return AdminServlet.DELETE_ORDERS;
	} else if (action.equalsIgnoreCase(AdminServlet.DELETE_TEMPLATE_STRING)) {
	    return AdminServlet.DELETE_TEMPLATES;
	}
	throw new InvalidActionException(action);
    }
}
