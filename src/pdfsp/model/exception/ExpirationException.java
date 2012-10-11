/**
 * 
 */
package pdfsp.model.exception;

import java.util.Date;

/**
 * Exception to throw when someone tries to access to an expired order
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class ExpirationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ExpirationException() {
	super("The order has expired");
    }

    /**
     * @param message
     */
    public ExpirationException(String message) {
	super(message);
    }

    /**
     * @param expirationTime
     */
    public ExpirationException(long expirationTime) {
	super("This file expired on " + new Date(expirationTime));
    }

    /**
     * @param expirationDate
     */
    public ExpirationException(Date expirationDate) {
	super("This file expired on " + expirationDate);
    }

}
