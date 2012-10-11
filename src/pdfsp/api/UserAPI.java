package pdfsp.api;

import pdfsp.model.User;
import pdfsp.model.exception.InvalidUserKeyException;

/**
 * API to work with the User Model
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class UserAPI {

    /**
     * The User to work with
     */
    private User user;

    /**
     * Creates an API for a given userKey
     * 
     * @param userKey
     * @throws InvalidUserKeyException
     */
    public UserAPI(String userKey) throws InvalidUserKeyException {
	if (userKey == null || userKey.equals("")) {
	    throw new InvalidUserKeyException("User key is empty!");
	}
	// TODO find the user by his userKey and set to this.user
    }

    public UserAPI() {
    }

    public UserAPI(User user) {
	this.setUser(user);
    }

    /**
     * @return the user
     */
    public final User getUser() {
	return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public final void setUser(User user) {
	this.user = user;
    }

    /*
     * PASSWORD RELATED SECTION
     */

    /**
     * Creates a random salt and set in the user model
     * 
     * @return the created salt
     */
    private String createSalt() {
	// TODO create a random salt
	// TODO set the salt
	return "";
    }

    /**
     * Creates a random password with the received salt and set in the user
     * model
     * 
     * @param salt
     * @return the created password
     */
    private String createPassword(String salt) {
	if (this.getUser() == null) {
	    // TODO throw a new exception!!!
	}
	// TODO create a random password with the received salt
	// TODO set the password
	return "";
    }

    /**
     * Creates a salt & password pair and set it in the User Model
     * 
     * @return the new password
     */
    private String createPassword() {
	return this.createPassword(this.createSalt());
    }
}
