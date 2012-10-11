package pdfsp.model;

/**
 * Session Model
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class Session {
    private String sessionKey;
    private String session;

    /**
     * Creates a new session
     */
    public Session() {
    }

    /**
     * Creates a session with the received params
     * 
     * @param sessionKey
     * @param session
     */
    public Session(String sessionKey, String session) {
	super();
	this.sessionKey = sessionKey;
	this.session = session;
    }

    /**
     * @return the sessionKey
     */
    public final String getSessionKey() {
	return sessionKey;
    }

    /**
     * @return the session
     */
    public final String getSession() {
	return session;
    }

    /**
     * @param sessionKey
     *            the sessionKey to set
     */
    public final void setSessionKey(String sessionKey) {
	this.sessionKey = sessionKey;
    }

    /**
     * @param session
     *            the session to set
     */
    public final void setSession(String session) {
	this.session = session;
    }
}
