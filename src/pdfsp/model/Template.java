package pdfsp.model;

import java.security.KeyFactory;
import java.util.Date;

/**
 * The Template Model.<br/>
 * 
 * A template contains the html to encode, with the tags to replace and some
 * identification and statistical params.
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Template extends BaseModel {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Text source;

    @Persistent
    private Date creation;

    @Persistent
    private Date revision;

    @Persistent
    private String userKey;

    @Persistent
    private int renderTimes;

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
    public Template(String userKey) {
	this.creation = new Date();
	this.userKey = userKey;
	this.renderTimes = 0;
	this.size = 0;
	this.hash = super.createHash(this.getClass().getName(), this.userKey,
		this.creation.getTime() + "");
	this.key = KeyFactory.createKey(this.getClass().getSimpleName(),
		this.hash);
	this.isCleanned = false;
    }

    /**
     * Creates a template with the userKey and the source
     * 
     * @param userKey
     * @param source
     */
    public Template(String userKey, Text source) {
	this.source = source;
	this.creation = new Date();
	this.userKey = userKey;
	this.renderTimes = 0;
	this.size = source.getValue().length();
	this.hash = super.createHash(this.getClass().getName(), this.userKey,
		this.creation.getTime() + "");
	this.key = KeyFactory.createKey(this.getClass().getSimpleName(),
		this.hash);
	this.isCleanned = false;
    }

    /**
     * @param key
     * @param source
     * @param creation
     * @param userKey
     * @param view
     * @param size
     * @param expiration
     * @param hash
     * @param isCleanned
     */
    public Template(Key key, Text source, Date creation, String userKey,
	    int view, long size, long expiration, String hash,
	    boolean isCleanned) {
	super();
	this.key = key;
	this.source = source;
	this.creation = creation;
	this.userKey = userKey;
	this.renderTimes = view;
	this.size = size;
	this.expiration = expiration;
	this.hash = hash;
	this.isCleanned = isCleanned;
    }

    /**
     * @return the key
     */
    public final Key getKey() {
	return key;
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
     * @return the userKey
     */
    public final String getUserKey() {
	return userKey;
    }

    /**
     * @return the renderTimes
     */
    public final int getRenderTimes() {
	return renderTimes;
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
     * @return the revision
     */
    public final Date getRevision() {
	return revision;
    }

    /**
     * @return the isCleanned
     */
    public final boolean isCleanned() {
	return isCleanned;
    }

    /**
     * @param isCleanned
     *            the isCleanned to set
     */
    public final void setCleanned(boolean isCleanned) {
	this.isCleanned = isCleanned;
    }

    /**
     * @param revision
     *            the revision to set
     */
    public final void setRevision(Date revision) {
	this.revision = revision;
    }

    /**
     * @param key
     *            the key to set
     */
    public final void setKey(Key key) {
	this.key = key;
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
     * @param userKey
     *            the userKey to set
     */
    public final void setUserKey(String userKey) {
	this.userKey = userKey;
    }

    /**
     * @param renderTimes
     *            the renderTimes to set
     */
    public final void setRenderTimes(int renderTimes) {
	this.renderTimes = renderTimes;
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
    public final void setHash(String hash) {
	this.hash = hash;
    }
}
