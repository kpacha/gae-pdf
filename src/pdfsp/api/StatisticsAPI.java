package pdfsp.api;

import java.util.List;

import javax.management.Query;

import pdfsp.model.Order;

/**
 * API to generate statistics
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class StatisticsAPI {

    /**
     * Calculate the encoding time average by user
     * 
     * @param userKey
     * @param pm
     *            the persistence manager
     * @return the encodingTime average
     */
    public static float getEncodingAvg(String userKey, PersistenceManager pm) {
	// search the order
	Query query = pm.newQuery(Order.class);
	query.setFilter("userKey == userKeyParam");
	query.declareParameters("String userKeyParam");
	StatisticsAPI stats = new StatisticsAPI();
	return stats.encodingAvg((List<Order>) query.execute(userKey));
    }

    /**
     * Calculate the encoding time average of all the processed orders
     * 
     * @param pm
     *            the persistence manager
     * @return the encodingTime average
     */
    public static float getEncodingAvg(PersistenceManager pm) {
	// search the order
	Query query = pm.newQuery(Order.class);
	query.setFilter("encodingTime != null && encodingTime != 0");
	StatisticsAPI stats = new StatisticsAPI();
	return stats.encodingAvg((List<Order>) query.execute());
    }

    /**
     * Calculate the encodingTime average of a list of orders
     * 
     * @param orders
     * @return the encodingTime average
     */
    protected float encodingAvg(List<Order> orders) {
	int total = 0;
	for (Order order : orders) {
	    total += order.getEncodingTime();
	}
	return total / orders.size();
    }
}
