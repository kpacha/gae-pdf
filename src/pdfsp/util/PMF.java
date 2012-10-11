package pdfsp.util;


/**
 * PersistenceManager wrapper
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance = JDOHelper
	    .getPersistenceManagerFactory("transactions-optional");

    private PMF() {
    }

    public static PersistenceManagerFactory get() {
	return pmfInstance;
    }
}