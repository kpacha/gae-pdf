package pdfsp.model.exception;

/**
 * Exception to throw when someone tries to access a non existent template
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class TemplateNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public TemplateNotFoundException() {
	super("Template not found");
    }

    /**
     * @param message
     */
    public TemplateNotFoundException(String message) {
	super(message);
    }

}
