package pdfsp.util.session;

import pdfsp.connector.MemcacheConnector;
import pdfsp.model.Session;
import pdfsp.model.exception.MemcacheConnectorException;

/**
 * Handler to work with the Session Model and the MemcacheConnector
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class SessionHandler {

    private MemcacheConnector cache = null;

    private final Session session;

    private static final SessionIdentifierGenerator sessIdGen = new SessionIdentifierGenerator();

    /**
     * Init the memcache
     * 
     * @throws MemcacheConnectorException
     */
    public SessionHandler() throws MemcacheConnectorException {
	try {
	    this.cache = new MemcacheConnector();
	} catch (CacheException e) {
	    throw new MemcacheConnectorException(e.getMessage());
	}
	this.session = new Session();
    }

    /**
     * Creates a new session
     * 
     * @return the sessionKey
     */
    public String create() {
	this.session.setSessionKey(this.sessIdGen.nextSessionId());
	return this.session.getSessionKey();
    }

    /**
     * Returns the session data
     * 
     * @param sessionKey
     * @return the session data
     */
    public String getSession(String sessionKey) {
	return this.cache.getString(sessionKey);
    }

    /**
     * Returns the sessionKey
     * 
     * @return the sessionKey
     */
    public String getSessionKey() {
	return this.session.getSessionKey();
    }

    /**
     * Updates the session in the memcache service
     * 
     * @param sessionData
     */
    public void setSession(String sessionData) {
	// updates the model
	this.session.setSession(sessionData);
	// updates the memcache with the value getted from the model
	this.cache.put(session.getSessionKey(), this.session.getSession());
    }
}
