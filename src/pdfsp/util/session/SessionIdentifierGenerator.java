package pdfsp.util.session;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Random session identifier generator
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public final class SessionIdentifierGenerator {

    private final SecureRandom random = new SecureRandom();

    public String nextSessionId() {
	return new BigInteger(130, random).toString(32);
    }

}