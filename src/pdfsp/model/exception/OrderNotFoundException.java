/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when someone tries to access a non existent order
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class OrderNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public OrderNotFoundException() {
	super("Order not found");
    }

    /**
     * @param message
     */
    public OrderNotFoundException(String message) {
	super(message);
    }

}
