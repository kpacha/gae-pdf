/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when someone tries to access to the pdf of a non encoded
 * order
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class PdfNotCreatedYetException extends Exception {

    private static final long serialVersionUID = 1L;

    public PdfNotCreatedYetException() {
	super("The pdf of this order has not been created yet");
    }

    /**
     * @param message
     */
    public PdfNotCreatedYetException(String message) {
	super(message);
    }

}
