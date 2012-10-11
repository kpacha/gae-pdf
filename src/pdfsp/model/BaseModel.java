package pdfsp.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pdfsp.util.Base64;

/**
 * The Base Model.<br/>
 * 
 * Offers some common methods.
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class BaseModel {

    private final char concatenator = '|';

    /**
     * Creates a hash and encoded with the Base64 class and with the URLEncoder
     * 
     * @param classType
     * @param userKey
     * @param creationTime
     * @return the encoded hash
     */
    protected String createHash(String classType, String userKey,
	    String creationTime) {
	String hash = Base64.encode(classType + concatenator + userKey
		+ concatenator + creationTime);
	try {
	    hash = URLEncoder.encode(hash, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return hash;
    }
}
