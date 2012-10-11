package pdfsp.servlet;

import java.io.IOException;

import pdfsp.model.exception.InvalidActionException;
import pdfsp.util.PMF;

/**
 * CRUD Servlet class
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class CRUDServlet extends BaseServlet {

    protected static final String CREATE_STRING = "create";
    protected static final String READ_STRING = "read";
    protected static final String UPDATE_STRING = "update";
    protected static final String DELETE_STRING = "delete";
    protected static final int CREATE = 0;
    protected static final int READ = 1;
    protected static final int UPDATE = 2;
    protected static final int DELETE = 3;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	// TODO validate the user
	this.doCRUD(req, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	// TODO validate the user
	this.doCRUD(req, response);
    }

    /**
     * Switch to the correct method by the 'action' parameter
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    protected void doCRUD(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	pm = PMF.get().getPersistenceManager();
	String action = getServletConfig().getInitParameter("action");
	try {
	    switch (this.getActionId(action)) {
	    case CRUDServlet.CREATE:
		this.doCreate(req, response);
		break;
	    case CRUDServlet.READ:
		this.doRead(req, response);
		break;
	    case CRUDServlet.UPDATE:
		this.doUpdate(req, response);
		break;
	    case CRUDServlet.DELETE:
		this.doRemove(req, response);
		break;
	    }
	} catch (InvalidActionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.doPrintJson(req, response);
	pm.close();
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
	if (action.equalsIgnoreCase(CRUDServlet.CREATE_STRING)) {
	    return CRUDServlet.CREATE;
	} else if (action.equalsIgnoreCase(CRUDServlet.READ_STRING)) {
	    return CRUDServlet.READ;
	} else if (action.equalsIgnoreCase(CRUDServlet.UPDATE_STRING)) {
	    return CRUDServlet.UPDATE;
	} else if (action.equalsIgnoreCase(CRUDServlet.DELETE_STRING)) {
	    return CRUDServlet.DELETE;
	}
	throw new InvalidActionException(action);
    }

    protected void doCreate(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
    }

    protected void doRead(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
    }

    protected void doUpdate(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
    }

    protected void doRemove(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
    }
}
