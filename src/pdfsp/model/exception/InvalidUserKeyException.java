/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when someone tries to access with an invalid userKey
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class InvalidUserKeyException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidUserKeyException() {
	super("Invalid user key");
    }

    /**
     * @param message
     */
    public InvalidUserKeyException(String message) {
	super(message);
    }

}
