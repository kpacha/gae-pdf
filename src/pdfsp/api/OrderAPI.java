package pdfsp.api;

import java.io.IOException;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.management.Query;

import pdfsp.model.Order;
import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;
import pdfsp.model.exception.PdfNotCreatedYetException;
import pdfsp.util.PDFCreator;

/**
 * API to work with the Order Model
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class OrderAPI extends BaseAPI {

    /**
     * The Order to work with
     */
    private Order order;

    /**
     * The time to store the order. When the order expires, we remove it
     * EXPIRATION_LAPSE = 30 days
     */
    private static final int EXPIRATION_LAPSE = 30 * 24 * 60 * 60;

    /**
     * Creates an API for a given user
     * 
     * @param userKey
     * @throws InvalidUserKeyException
     */
    public OrderAPI(String userKey) throws InvalidUserKeyException {
	if (userKey == null || userKey.equals("")) {
	    throw new InvalidUserKeyException("User key is empty!");
	}
	this.order = new Order(userKey);
    }

    /**
     * Creates an API for a given user and html source
     * 
     * @param userKey
     * @param html
     * @throws InvalidUserKeyException
     * @throws EmptyHtmlSourceException
     */
    public OrderAPI(String userKey, Text html) throws InvalidUserKeyException,
	    EmptyHtmlSourceException {
	this(userKey);
	this.setSource(html);
    }

    /**
     * Creates an API for a given Order model
     * 
     * @param order
     * @throws InvalidUserKeyException
     */
    public OrderAPI(Order order) throws InvalidUserKeyException {
	this(order.getUserKey());
	this.order = order;
    }

    /**
     * Get the pdf as a blob if it is not expired
     * 
     * @return the pdf blob
     * @throws ExpirationException
     *             if it is expired
     * @throws PdfNotCreatedYetException
     */
    public Blob getPdf() throws ExpirationException, PdfNotCreatedYetException {
	this.checkExpiration();
	if (order.getSize() == 0) {
	    throw new PdfNotCreatedYetException();
	}
	this.incrementViews();
	return order.getPdf();
    }

    /**
     * Get the Order linked to the API
     * 
     * @return the Order associated to the API
     */
    public Order getOrder() {
	return order;
    }

    /**
     * Sets the html source
     * 
     * @param text
     * @throws EmptyHtmlSourceException
     */
    public void setSource(Text text) throws EmptyHtmlSourceException {
	if (text.equals("") || text == null) {
	    throw new EmptyHtmlSourceException();
	}
	order.setSource(text);
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
     * Saves a pdf blob in the Order
     * 
     * @param pdf
     * @throws ExpirationException
     */
    private final void setPdf(Blob pdf) throws ExpirationException {
	this.checkExpiration();
	order.setPdf(pdf);
	order.setEncoding(new Date());
	order.setSize(pdf.getBytes().length);
    }

    /**
     * Encode the Order source
     * 
     * @return the pdf size
     * @throws ExpirationException
     * @throws IOException
     * @throws DocumentException
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public long encodePdf() throws ExpirationException, IOException,
	    DocumentException, EmptyHtmlSourceException, EmptyBodyException {
	return this.encodePdf(order.getSource());
    }

    /**
     * Encode the received source
     * 
     * @param source
     * @return the pdf size
     * @throws ExpirationException
     * @throws IOException
     * @throws DocumentException
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public long encodePdf(Text source) throws ExpirationException, IOException,
	    DocumentException, EmptyHtmlSourceException, EmptyBodyException {
	Date startTime = new Date();
	boolean shouldClean = !this.getOrder().isCleanned();
	PDFCreator pdfCreator = new PDFCreator(source, shouldClean);
	this.setPdf(pdfCreator.convert());
	if (shouldClean) {
	    this.getOrder().setCleanned(true);
	    this.setSource(pdfCreator.getHtml());
	}
	order.setEncodingTime(new Date().getTime() - startTime.getTime());
	order.setExpiration(startTime.getTime() + this.EXPIRATION_LAPSE);
	return order.getSize();
    }

    /**
     * Encode the received source
     * 
     * @param source
     * @return the pdf size
     * @throws ExpirationException
     * @throws IOException
     * @throws DocumentException
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public long encodePdf(String source) throws ExpirationException,
	    IOException, DocumentException, EmptyHtmlSourceException,
	    EmptyBodyException {
	return this.encodePdf(new Text(source));
    }

    /**
     * Increment in one the embedded viewer count
     */
    private void incrementViews() {
	order.setView(order.getView() + 1);
    }

    /**
     * Checks the expiration date of the Order
     * 
     * @throws ExpirationException
     */
    private void checkExpiration() throws ExpirationException {
	long now = new Date().getTime();
	if (order.getPdf() != null && order.getExpiration() < now) {
	    throw new ExpirationException(order.getExpiration());
	}
    }

    /**
     * Queues the encoding of the associated order
     */
    public void queueEncode() {
	Queue queue = QueueFactory.getDefaultQueue();
	queue.add(withUrl("/worker/encode").param("k",
		this.getOrder().getHash()));
    }

    /**
     * Select orders by userKey and hash
     * 
     * @param userKey
     * @param hash
     * @param pm
     * @return the list of selected orders
     */
    public static List<Order> select(String userKey, String hash,
	    PersistenceManager pm) {
	// search the order
	Query query = pm.newQuery(Order.class);
	query.setFilter("hash == hashParam && userKey == userKeyParam");
	query.declareParameters("String hashParam, String userKeyParam");
	query.setRange(0, OrderAPI.MAX_SELECT_RANGE);
	List<Order> results = (List<Order>) query.execute(hash, userKey);
	return results;
    }

    /**
     * Select orders by userKey
     * 
     * @param userKey
     * @param pm
     * @return the list of selected orders
     */
    public static List<Order> select(String userKey, PersistenceManager pm) {
	// search the order
	Query query = pm.newQuery(Order.class);
	query.setFilter("userKey == userKeyParam");
	query.declareParameters("String userKeyParam");
	query.setRange(0, OrderAPI.MAX_SELECT_RANGE);
	List<Order> results = (List<Order>) query.execute(userKey);
	return results;
    }

    /**
     * Parse the order in a json
     * 
     * @return the JSON encoded order
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
	JSONObject json = new JSONObject();
	json.put("hash", order.getHash());
	json.put("source", (order.getSource() == null) ? " " : order
		.getSource().getValue());
	json.put("creation", order.getCreation());
	json.put("userKey", order.getUserKey());
	json.put("view", order.getView());
	json.put("size", order.getSize());
	json.put("expiration", order.getExpiration());
	json.put("encoding", order.getEncoding());
	json.put("encodingTime", order.getEncodingTime());
	return json;
    }

    /**
     * Get all orders
     * 
     * @param pm
     * @return the list of selected orders
     */
    public static List<Order> select(PersistenceManager pm) {
	return OrderAPI.select(pm, false);
    }

    /**
     * Select orders ordered by creation
     * 
     * @param pm
     * @param creationAsc
     * @return the list of selected orders
     */
    public static List<Order> select(PersistenceManager pm, boolean creationAsc) {
	// search the order
	Query query = pm.newQuery(Order.class);
	if (creationAsc) {
	    query.setOrdering("creation asc");
	} else {
	    query.setOrdering("creation desc");
	}
	query.setRange(0, OrderAPI.MAX_SELECT_RANGE);
	return (List<Order>) query.execute();
    }

    /**
     * Delete the received orders
     * 
     * @param orders
     * @param pm
     * @return true if success
     */
    public static boolean deleteOrders(List orders, PersistenceManager pm) {
	return OrderAPI.deleteObjects(orders.iterator(), pm);
    }
}
