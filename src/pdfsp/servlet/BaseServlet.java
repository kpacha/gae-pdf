package pdfsp.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pdfsp.util.session.SessionIdentifierGenerator;

/**
 * Base Servlet class.
 * 
 * Contains the session identifier generator and shares it with all the servlets
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

    protected PersistenceManager pm = null;

    protected JSONObject jsonResponse = new JSONObject();

    protected static final SessionIdentifierGenerator sessIdGen = new SessionIdentifierGenerator();

    /**
     * Print the protected jsonResponse
     * 
     * @param req
     * @param response
     */
    protected void doPrintJson(HttpServletRequest req,
	    HttpServletResponse response) {
	response.setContentType("text/x-json");
	try {
	    response.getWriter().println(this.jsonResponse.toJSONString());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Add an error message in the jsonResponse
     * 
     * @param errorMessage
     */
    protected void doPrintErrorJson(String errorMessage) {
	this.jsonResponse = new JSONObject();
	this.jsonResponse.put("success", false);
	this.jsonResponse.put("total", 0);
	this.jsonResponse.put("result", null);
	this.jsonResponse.put("error", errorMessage);
    }

    /**
     * Add the success result in the jsonResponse with an array of objects
     * 
     * @param list
     */
    protected void doPrintSuccessJson(JSONArray list) {
	this.jsonResponse = new JSONObject();
	this.jsonResponse.put("success", true);
	this.jsonResponse.put("total", list.size());
	this.jsonResponse.put("result", list);
    }

    protected String encode(String text) {
	try {
	    return URLEncoder.encode(text, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    // do nothing
	}
	return "";
    }

    protected String nextSessionId() {
	return BaseServlet.sessIdGen.nextSessionId();
    }

    // TODO should interact with a session filter
}
