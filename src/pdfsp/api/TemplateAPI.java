package pdfsp.api;

import java.util.Date;
import java.util.List;

import javax.management.Query;

import pdfsp.model.Template;
import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.EmptyTemplateParametersException;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.util.PDFCreator;

/**
 * API to work with the Template Model
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class TemplateAPI extends BaseAPI {

    /**
     * The Order to work with
     */
    private Template template;

    /**
     * The time to store the template. When the template expires, we remove it
     * EXPIRATION_LAPSE = 365 days
     */
    private static final int EXPIRATION_LAPSE = 365 * 24 * 60 * 60;

    /**
     * Creates an API for a given user
     * 
     * @param userKey
     * @throws InvalidUserKeyException
     */
    public TemplateAPI(String userKey) throws InvalidUserKeyException {
	if (userKey == null || userKey.equals("")) {
	    throw new InvalidUserKeyException("User key is empty!");
	}
	this.template = new Template(userKey);
    }

    /**
     * Creates an API for a given user and html source
     * 
     * @param userKey
     * @param html
     * @throws InvalidUserKeyException
     * @throws EmptyHtmlSourceException
     */
    public TemplateAPI(String userKey, Text html)
	    throws InvalidUserKeyException, EmptyHtmlSourceException {
	this(userKey);
	this.setSource(html);
    }

    /**
     * Creates an API for a given Template model
     * 
     * @param template
     * @throws InvalidUserKeyException
     */
    public TemplateAPI(Template template) throws InvalidUserKeyException {
	this(template.getUserKey());
	this.template = template;
    }

    /**
     * Get the Template linked to the API
     * 
     * @return the Template associated to the API
     */
    public Template getTemplate() {
	return template;
    }

    /**
     * Sets the html source and updates the template
     * 
     * @param text
     * @throws EmptyHtmlSourceException
     */
    public void setSource(Text text) throws EmptyHtmlSourceException {
	if (text.getValue() == null) {
	    throw new EmptyHtmlSourceException();
	}
	if (text.getValue().equalsIgnoreCase("")) {
	    throw new EmptyHtmlSourceException();
	}
	// TODO: load the external resources and change the references to make
	// them local
	this.template.setSource(text);
	this.template.setSize(text.getValue().length());
	this.template.setRevision(new Date());
    }

    /**
     * Sets the html source
     * 
     * @param text
     * @throws EmptyHtmlSourceException
     */
    public void setSource(String text) throws EmptyHtmlSourceException {
	this.setSource(new Text(text));
    }

    /**
     * Increment in one the embedded renderTimes count
     */
    private void incrementRenders() {
	// FIXME imrpove this! make it atomic
	template.setRenderTimes(template.getRenderTimes() + 1);
    }

    /**
     * Checks the expiration date of the Order
     * 
     * @throws ExpirationException
     */
    private void checkExpiration() throws ExpirationException {
	long now = new Date().getTime();
	if (template.getExpiration() != 0 && template.getExpiration() < now) {
	    throw new ExpirationException(template.getExpiration());
	}
    }

    /**
     * Creates an order with the template and the received serialized params
     * 
     * @param params
     * @return the OrderAPI
     * @throws InvalidUserKeyException
     * @throws EmptyHtmlSourceException
     * @throws ExpirationException
     *             when the template is expired
     * @throws EmptyTemplateParametersException
     *             when the params are empty
     * @throws EmptyBodyException
     */
    public OrderAPI createOrder(String params) throws InvalidUserKeyException,
	    EmptyHtmlSourceException, ExpirationException,
	    EmptyTemplateParametersException, EmptyBodyException {
	this.checkExpiration();
	if (params == null || params.equalsIgnoreCase("")) {
	    throw new EmptyTemplateParametersException(params);
	}
	if (!this.getTemplate().isCleanned()) {
	    // TODO: load the external resources and change the references to
	    // make them local
	    // clean the template source here!
	    PDFCreator pdfCreator = new PDFCreator(this.getTemplate()
		    .getSource(), true);
	    this.setSource(pdfCreator.getHtml());
	    this.getTemplate().setCleanned(true);
	}
	// replace the placeholders with the received params
	String html = this.replaceParams(this.getTemplate().getSource()
		.getValue(), params);
	this.incrementRenders();
	// create the order and return it
	return new OrderAPI(this.template.getUserKey(), new Text(html));
    }

    /**
     * Replace the placeholders with the received data
     * 
     * @param html
     * @param params
     * @return the source with all the placeholders replaced
     */
    protected String replaceParams(String html, String params) {
	JSONObject p = (JSONObject) JSONValue.parse(params);
	JSONArray tags = (JSONArray) p.get("tag");
	JSONArray values = (JSONArray) p.get("value");
	// TODO validate the encoding!!!
	for (int i = 0; i < values.size(); i++) {
	    html = html.replace(tags.get(i).toString(), values.get(i)
		    .toString());
	}
	return html;
    }

    /**
     * Select templates by userKey and hash
     * 
     * @param userKey
     * @param hash
     * @param pm
     * @return the list of selected templates
     */
    public static List<Template> select(String userKey, String hash,
	    PersistenceManager pm) {
	// search the template
	Query query = pm.newQuery(Template.class);
	query.setFilter("hash == hashParam && userKey == userKeyParam");
	query.declareParameters("String hashParam, String userKeyParam");
	query.setRange(0, TemplateAPI.MAX_SELECT_RANGE);
	List<Template> results = (List<Template>) query.execute(hash, userKey);
	return results;
    }

    /**
     * Select templates by userKey
     * 
     * @param userKey
     * @param pm
     * @return the list of selected templates
     */
    public static List<Template> select(String userKey, PersistenceManager pm) {
	// search the template
	Query query = pm.newQuery(Template.class);
	query.setFilter("userKey == userKeyParam");
	query.declareParameters("String userKeyParam");
	query.setRange(0, TemplateAPI.MAX_SELECT_RANGE);
	List<Template> results = (List<Template>) query.execute(userKey);
	return results;
    }

    /**
     * Parse the template in a json
     * 
     * @return the JSON encoded template
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
	JSONObject json = new JSONObject();
	json.put("hash", template.getHash());
	json.put("source", (template.getSource() == null) ? " " : template
		.getSource().getValue());
	json.put("creation", template.getCreation());
	json.put("userKey", template.getUserKey());
	json.put("renderTimes", template.getRenderTimes());
	json.put("size", template.getSize());
	json.put("expiration", template.getExpiration());
	return json;
    }

    /**
     * Get all templates
     * 
     * @param pm
     * @return the list of selected templates
     */
    public static List<Template> select(PersistenceManager pm) {
	return TemplateAPI.select(pm, false);
    }

    /**
     * Select templates ordered by creation
     * 
     * @param pm
     * @param creationAsc
     * @return the list of selected templates
     */
    private static List<Template> select(PersistenceManager pm,
	    boolean creationAsc) {
	// search the template
	Query query = pm.newQuery(Template.class);
	if (creationAsc) {
	    query.setOrdering("creation asc");
	} else {
	    query.setOrdering("creation desc");
	}
	query.setRange(0, TemplateAPI.MAX_SELECT_RANGE);
	return (List<Template>) query.execute();
    }

    /**
     * Delete the received templates
     * 
     * @param templates
     * @param pm
     * @return true if success
     */
    public static boolean deleteTemplates(List templates, PersistenceManager pm) {
	return TemplateAPI.deleteObjects(templates.iterator(), pm);
    }
}
