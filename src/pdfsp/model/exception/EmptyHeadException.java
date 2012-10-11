/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when the html source of an order has not a head tag
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class EmptyHeadException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmptyHeadException() {
	super("Empty html source");
    }

    /**
     * @param message
     */
    public EmptyHeadException(String message) {
	super(message);
    }

}
