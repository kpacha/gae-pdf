package pdfsp.model;

import java.security.KeyFactory;
import java.sql.Blob;
import java.util.Date;

import pdfsp.api.OrderAPI;
import pdfsp.model.exception.ExpirationException;
import pdfsp.model.exception.InvalidUserKeyException;

/**
 * The Order Model
 * 
 * An order is the core of the business. Contains the html to encode, the pdf
 * result, the id of the owner and other statistical data.
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Order extends BaseModel {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Blob pdf;

    @Persistent
    private Text source;

    @Persistent
    private Date creation;

    @Persistent
    private Date encoding;

    @Persistent
    private long encodingTime;

    @Persistent
    private String userKey;

    @Persistent
    private int view;

    @Persistent
    private long size;

    @Persistent
    private long expiration;

    @Persistent
    private String hash;

    @Persistent
    private boolean isCleanned;

    /**
     * Default constructor
     * 
     * @param userKey
     */
    public Order(String userKey) {
	this.creation = new Date();
	this.userKey = userKey;
	this.view = 0;
	this.size = 0;
	this.encodingTime = 0;
	this.hash = super.createHash(this.getClass().getName(), this.userKey,
		this.creation.getTime() + "");
	this.key = KeyFactory.createKey(this.getClass().getSimpleName(),
		this.hash);
	this.setCleanned(false);
    }

    /**
     * @param key
     * @param pdf
     * @param source
     * @param creation
     * @param encoding
     * @param userKey
     * @param view
     * @param size
     * @param expiration
     * @param hash
     * @param isCleanned
     */
    public Order(Key key, Blob pdf, Text source, Date creation, Date encoding,
	    String userKey, int view, long size, long expiration, String hash,
	    boolean isCleanned) {
	super();
	this.key = key;
	this.pdf = pdf;
	this.source = source;
	this.creation = creation;
	this.encoding = encoding;
	this.userKey = userKey;
	this.view = view;
	this.size = size;
	this.expiration = expiration;
	this.hash = hash;
	this.setCleanned(isCleanned);
    }

    /**
     * @return the key
     */
    public final Key getKey() {
	return key;
    }

    /**
     * @return the pdf
     * @throws ExpirationException
     *             when the file is expired
     */
    public final Blob getPdf() throws ExpirationException {
	return pdf;
    }

    /**
     * @return the source
     */
    public final Text getSource() {
	return source;
    }

    /**
     * @return the creation
     */
    public final Date getCreation() {
	return creation;
    }

    /**
     * @return the encoding
     */
    public final Date getEncoding() {
	return encoding;
    }

    /**
     * @return the userKey
     */
    public final String getUserKey() {
	return userKey;
    }

    /**
     * @return the view
     */
    public final int getView() {
	return view;
    }

    /**
     * @return the size
     */
    public final long getSize() {
	return size;
    }

    /**
     * @return the expiration
     */
    public final long getExpiration() {
	return expiration;
    }

    /**
     * @return the hash
     */
    public final String getHash() {
	return hash;
    }

    /**
     * @return the encodingTime
     */
    public final long getEncodingTime() {
	return encodingTime;
    }

    public void setCleanned(boolean isCleanned) {
	this.isCleanned = isCleanned;
    }

    public boolean isCleanned() {
	return isCleanned;
    }

    /**
     * @param encodingTime
     *            the encodingTime to set
     */
    public final void setEncodingTime(long encodingTime) {
	this.encodingTime = encodingTime;
    }

    /**
     * @param key
     *            the key to set
     */
    public final void setKey(Key key) {
	this.key = key;
    }

    /**
     * @param pdf
     *            the pdf to set
     */
    public final void setPdf(Blob pdf) {
	this.pdf = pdf;
    }

    /**
     * @param source
     *            the source to set
     */
    public final void setSource(Text source) {
	this.source = source;
    }

    /**
     * @param creation
     *            the creation to set
     */
    public final void setCreation(Date creation) {
	this.creation = creation;
    }

    /**
     * @param encoding
     *            the encoding to set
     */
    public final void setEncoding(Date encoding) {
	this.encoding = encoding;
    }

    /**
     * @param userKey
     *            the userKey to set
     */
    public final void setUserKey(String userKey) {
	this.userKey = userKey;
    }

    /**
     * @param view
     *            the view to set
     */
    public final void setView(int view) {
	this.view = view;
    }

    /**
     * @param size
     *            the size to set
     */
    public final void setSize(long size) {
	this.size = size;
    }

    /**
     * @param expiration
     *            the expiration to set
     */
    public final void setExpiration(long expiration) {
	this.expiration = expiration;
    }

    /**
     * @param hash
     *            the hash to set
     */
    public final void setHas(String hash) {
	this.hash = hash;
    }

    /**
     * Returns the associated API
     * 
     * @return the associated OrderAPI
     * @throws InvalidUserKeyException
     */
    public OrderAPI getAPI() throws InvalidUserKeyException {
	return new OrderAPI(this);
    }
}
