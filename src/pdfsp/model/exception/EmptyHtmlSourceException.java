/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when the html source of an order is empty or is not an
 * html code
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class EmptyHtmlSourceException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmptyHtmlSourceException() {
	super("Empty html source");
    }

    /**
     * @param message
     */
    public EmptyHtmlSourceException(String message) {
	super(message);
    }

}
