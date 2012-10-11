package pdfsp.model.exception;

/**
 * Exception to throw when an error occurs accessing the memcache connector
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class MemcacheConnectorException extends Exception {
    public MemcacheConnectorException(String msg) {
	super(msg);
    }

    MemcacheConnectorException(Exception e) {
	super(e);
    }
}
