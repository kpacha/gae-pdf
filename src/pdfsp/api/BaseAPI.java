package pdfsp.api;


/**
 * Base API to work with model objects
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class BaseAPI {

    /**
     * The max range for the datastore queries
     */
    protected static final int MAX_SELECT_RANGE = 100;

    /**
     * Delete the received objects
     * 
     * @param iter
     * @param pm
     * @return true if success
     */
    public static boolean deleteObjects(java.util.Iterator<Object> iter,
	    PersistenceManager pm) {
	while (iter.hasNext()) {
	    pm.deletePersistent(iter.next());
	}
	return true;
    }
}
