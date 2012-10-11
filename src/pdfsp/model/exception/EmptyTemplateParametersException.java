/**
 * 
 */
package pdfsp.model.exception;

/**
 * Exception to throw when something was grong with the template parameters
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class EmptyTemplateParametersException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmptyTemplateParametersException() {
	super("Empty template parameters");
    }

    /**
     * @param params
     */
    public EmptyTemplateParametersException(String params) {
	super("Empty template parameters: " + params);
    }

}
