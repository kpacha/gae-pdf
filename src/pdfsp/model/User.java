package pdfsp.model;

import java.util.Date;

/**
 * The User Model
 * 
 * An user is the representation of a customer. Contains the contact data, the
 * account settings, the billing data and other statistical data.
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String userKey;

    @Persistent
    private String name;

    @Persistent
    private String surnames;

    @Persistent
    private String nickName;

    @Persistent
    private String password;

    @Persistent
    private String salt;

    @Persistent
    private Date creationTime;

    @Persistent
    private String creationIp;

    @Persistent
    private Date lastLogin;

    @Persistent
    private String lastLoginIp;

    @Persistent
    private long totalOrders;

    @Persistent
    private long mensualOrders;

    @Persistent
    private long dailyOrders;

    @Persistent
    private long totalOrderErrors;

    @Persistent
    private long mensualOrderErrors;

    @Persistent
    private long dailyOrderErrors;

    @Persistent
    private long rate;

    @Persistent
    private long maxSize;

    @Persistent
    private long currentQuota;

    @Persistent
    private long limitQuota;

    @Persistent
    private int status;

    @Persistent
    private int type;

    /**
     * @param key
     * @param userKey
     * @param name
     * @param surnames
     * @param nickName
     * @param password
     * @param salt
     * @param creationTime
     * @param creationIp
     * @param lastLogin
     * @param lastLoginIp
     * @param totalOrders
     * @param mensualOrders
     * @param dailyOrders
     * @param totalOrderErrors
     * @param mensualOrderErrors
     * @param dailyOrderErrors
     * @param rate
     * @param maxSize
     * @param currentQuota
     * @param limitQuota
     * @param status
     * @param type
     */
    public User(Key key, String userKey, String name, String surnames,
	    String nickName, String password, String salt, Date creationTime,
	    String creationIp, Date lastLogin, String lastLoginIp,
	    long totalOrders, long mensualOrders, long dailyOrders,
	    long totalOrderErrors, long mensualOrderErrors,
	    long dailyOrderErrors, long rate, long maxSize, long currentQuota,
	    long limitQuota, int status, int type) {
	super();
	this.key = key;
	this.userKey = userKey;
	this.name = name;
	this.surnames = surnames;
	this.nickName = nickName;
	this.password = password;
	this.salt = salt;
	this.creationTime = creationTime;
	this.creationIp = creationIp;
	this.lastLogin = lastLogin;
	this.lastLoginIp = lastLoginIp;
	this.totalOrders = totalOrders;
	this.mensualOrders = mensualOrders;
	this.dailyOrders = dailyOrders;
	this.totalOrderErrors = totalOrderErrors;
	this.mensualOrderErrors = mensualOrderErrors;
	this.dailyOrderErrors = dailyOrderErrors;
	this.rate = rate;
	this.maxSize = maxSize;
	this.currentQuota = currentQuota;
	this.limitQuota = limitQuota;
	this.status = status;
	this.type = type;
    }

    /**
     * @return the key
     */
    public final Key getKey() {
	return key;
    }

    /**
     * @return the userKey
     */
    public final String getUserKey() {
	return userKey;
    }

    /**
     * @return the name
     */
    public final String getName() {
	return name;
    }

    /**
     * @return the surnames
     */
    public final String getSurnames() {
	return surnames;
    }

    /**
     * @return the nickName
     */
    public final String getNickName() {
	return nickName;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
	return password;
    }

    /**
     * @return the salt
     */
    public final String getSalt() {
	return salt;
    }

    /**
     * @return the creationTime
     */
    public final Date getCreationTime() {
	return creationTime;
    }

    /**
     * @return the creationIp
     */
    public final String getCreationIp() {
	return creationIp;
    }

    /**
     * @return the lastLogin
     */
    public final Date getLastLogin() {
	return lastLogin;
    }

    /**
     * @return the lastLoginIp
     */
    public final String getLastLoginIp() {
	return lastLoginIp;
    }

    /**
     * @return the totalOrders
     */
    public final long getTotalOrders() {
	return totalOrders;
    }

    /**
     * @return the mensualOrders
     */
    public final long getMensualOrders() {
	return mensualOrders;
    }

    /**
     * @return the dailyOrders
     */
    public final long getDailyOrders() {
	return dailyOrders;
    }

    /**
     * @return the totalOrderErrors
     */
    public final long getTotalOrderErrors() {
	return totalOrderErrors;
    }

    /**
     * @return the mensualOrderErrors
     */
    public final long getMensualOrderErrors() {
	return mensualOrderErrors;
    }

    /**
     * @return the dailyOrderErrors
     */
    public final long getDailyOrderErrors() {
	return dailyOrderErrors;
    }

    /**
     * @return the rate
     */
    public final long getRate() {
	return rate;
    }

    /**
     * @return the maxSize
     */
    public final long getMaxSize() {
	return maxSize;
    }

    /**
     * @return the currentQuota
     */
    public final long getCurrentQuota() {
	return currentQuota;
    }

    /**
     * @return the limitQuota
     */
    public final long getLimitQuota() {
	return limitQuota;
    }

    /**
     * @return the status
     */
    public final int getStatus() {
	return status;
    }

    /**
     * @return the type
     */
    public final int getType() {
	return type;
    }

    /**
     * @param key
     *            the key to set
     */
    public final void setKey(Key key) {
	this.key = key;
    }

    /**
     * @param userKey
     *            the userKey to set
     */
    public final void setUserKey(String userKey) {
	this.userKey = userKey;
    }

    /**
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
	this.name = name;
    }

    /**
     * @param surnames
     *            the surnames to set
     */
    public final void setSurnames(String surnames) {
	this.surnames = surnames;
    }

    /**
     * @param nickName
     *            the nickName to set
     */
    public final void setNickName(String nickName) {
	this.nickName = nickName;
    }

    /**
     * @param password
     *            the password to set
     */
    public final void setPassword(String password) {
	this.password = password;
    }

    /**
     * @param salt
     *            the salt to set
     */
    public final void setSalt(String salt) {
	this.salt = salt;
    }

    /**
     * @param creationTime
     *            the creationTime to set
     */
    public final void setCreationTime(Date creationTime) {
	this.creationTime = creationTime;
    }

    /**
     * @param creationIp
     *            the creationIp to set
     */
    public final void setCreationIp(String creationIp) {
	this.creationIp = creationIp;
    }

    /**
     * @param lastLogin
     *            the lastLogin to set
     */
    public final void setLastLogin(Date lastLogin) {
	this.lastLogin = lastLogin;
    }

    /**
     * @param lastLoginIp
     *            the lastLoginIp to set
     */
    public final void setLastLoginIp(String lastLoginIp) {
	this.lastLoginIp = lastLoginIp;
    }

    /**
     * @param totalOrders
     *            the totalOrders to set
     */
    public final void setTotalOrders(long totalOrders) {
	this.totalOrders = totalOrders;
    }

    /**
     * @param mensualOrders
     *            the mensualOrders to set
     */
    public final void setMensualOrders(long mensualOrders) {
	this.mensualOrders = mensualOrders;
    }

    /**
     * @param dailyOrders
     *            the dailyOrders to set
     */
    public final void setDailyOrders(long dailyOrders) {
	this.dailyOrders = dailyOrders;
    }

    /**
     * @param totalOrderErrors
     *            the totalOrderErrors to set
     */
    public final void setTotalOrderErrors(long totalOrderErrors) {
	this.totalOrderErrors = totalOrderErrors;
    }

    /**
     * @param mensualOrderErrors
     *            the mensualOrderErrors to set
     */
    public final void setMensualOrderErrors(long mensualOrderErrors) {
	this.mensualOrderErrors = mensualOrderErrors;
    }

    /**
     * @param dailyOrderErrors
     *            the dailyOrderErrors to set
     */
    public final void setDailyOrderErrors(long dailyOrderErrors) {
	this.dailyOrderErrors = dailyOrderErrors;
    }

    /**
     * @param rate
     *            the rate to set
     */
    public final void setRate(long rate) {
	this.rate = rate;
    }

    /**
     * @param maxSize
     *            the maxSize to set
     */
    public final void setMaxSize(long maxSize) {
	this.maxSize = maxSize;
    }

    /**
     * @param currentQuota
     *            the currentQuota to set
     */
    public final void setCurrentQuota(long currentQuota) {
	this.currentQuota = currentQuota;
    }

    /**
     * @param limitQuota
     *            the limitQuota to set
     */
    public final void setLimitQuota(long limitQuota) {
	this.limitQuota = limitQuota;
    }

    /**
     * @param status
     *            the status to set
     */
    public final void setStatus(int status) {
	this.status = status;
    }

    /**
     * @param type
     *            the type to set
     */
    public final void setType(int type) {
	this.type = type;
    }
}
