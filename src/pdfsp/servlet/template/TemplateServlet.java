package pdfsp.servlet.template;

import java.io.IOException;
import java.util.List;

import pdfsp.api.TemplateAPI;
import pdfsp.model.Template;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.servlet.CRUDServlet;

/**
 * Controller who implements the CRUD of template api and responses over json
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class TemplateServlet extends CRUDServlet {

    /**
     * Create a new template
     * 
     * @param req
     * @param response
     * @throws IOException
     */
    @Override
    protected void doCreate(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {
	String userKey = req.getParameter("userKey");
	String html = req.getParameter("html");
	TemplateAPI templateApi = null;
	try {
	    templateApi = new TemplateAPI(userKey, new Text(html));
	} catch (InvalidUserKeyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    // send error
	    this.doPrintErrorJson(e.getMessage());
	    return;
	} catch (EmptyHtmlSourceException e) {
	    try {
		templateApi = new TemplateAPI(userKey);
	    } catch (InvalidUserKeyException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		// send error
		this.doPrintErrorJson(e1.getMessage());
		return;
	    }
	}
	pm.makePersistent(templateApi.getTemplate());

	this.jsonResponse.put("success", true);
	this.jsonResponse.put("hash", templateApi.getTemplate().getHash());
    }

    /**
     * List template(s)
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

	List<Template> results = null;
	// search the template
	if (hash == null || hash.equalsIgnoreCase("")) {
	    results = TemplateAPI.select(userKey, pm);
	} else {
	    results = TemplateAPI.select(userKey, hash, pm);
	}
	java.util.Iterator<Template> iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Templates entries found!");
	}

	JSONArray list = new JSONArray();
	while (iter.hasNext()) {
	    try {
		TemplateAPI templateApi = new TemplateAPI(iter.next());
		list.add(templateApi.toJson());
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
     * Update temple source by template hash and userKey
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

	List<Template> results = TemplateAPI.select(userKey, hash, pm);
	java.util.Iterator<Template> iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Templates entries found!");
	}

	try {
	    JSONArray list = new JSONArray();
	    while (iter.hasNext()) {
		TemplateAPI templateApi = new TemplateAPI(iter.next());
		templateApi.setSource(html);
		pm.makePersistent(templateApi.getTemplate());
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
     * Remove by template hash and userKey
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

	List<Template> results = TemplateAPI.select(userKey, hash, pm);
	java.util.Iterator<Template> iter = results.iterator();
	if (!iter.hasNext()) {
	    System.out.println("No Templates entries found!");
	}

	try {
	    JSONArray list = new JSONArray();
	    TemplateAPI templateApi = null;
	    while (iter.hasNext()) {
		templateApi = new TemplateAPI(iter.next());
		pm.deletePersistent(templateApi.getTemplate());
	    }
	    this.doPrintSuccessJson(list);
	} catch (InvalidUserKeyException e) {
	    // send error
	    this.doPrintErrorJson(e.getMessage());
	}
    }
}
