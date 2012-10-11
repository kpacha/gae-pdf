package pdfsp.connector;

import java.util.Collections;

import pdfsp.model.exception.MemcacheConnectorException;

public class MemcacheConnector {

    private final Cache cache;
    private boolean isInit = false;

    private static final String MEMCACHE_PREFIX = "1";

    /**
     * Default constructor
     * 
     * @throws MemcacheConnectorException
     * @throws CacheException
     */
    public MemcacheConnector() throws MemcacheConnectorException,
	    CacheException {
	cache = CacheManager.getInstance().getCacheFactory()
		.createCache(Collections.emptyMap());
	isInit = true;
    }

    /**
     * Get from the memcache the value as a string
     * 
     * @param key
     * @return the value stored in the key
     */
    public String getString(Object key) {
	return ((isInit) ? (String) get(key) : null);
    }

    /**
     * Get from the memcache the value as a byteArray
     * 
     * @param key
     * @return the value stored in the key
     */
    public byte[] getByteArray(Object key) {
	return ((isInit) ? (byte[]) get(key) : null);
    }

    /**
     * Get from the memcache the value as an object
     * 
     * @param key
     * @return the value stored in the key
     */
    private Object get(Object key) {
	return ((isInit) ? cache.get(MEMCACHE_PREFIX + "_" + key) : null);
    }

    /**
     * Default put proxy
     * 
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
	cache.put(MEMCACHE_PREFIX + "_" + key, value);
    }
}
