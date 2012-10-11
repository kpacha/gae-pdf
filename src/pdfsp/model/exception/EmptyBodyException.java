/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when the html source of an order has not a body tag
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class EmptyBodyException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmptyBodyException() {
	super("Empty html source");
    }

    /**
     * @param message
     */
    public EmptyBodyException(String message) {
	super(message);
    }

}
