/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when someone tries to access to an invalid action
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class InvalidActionException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidActionException() {
	super("Invalid action");
    }

    /**
     * @param message
     */
    public InvalidActionException(String message) {
	super("Invalid action : " + message);
    }

}
