/**
 * 
 */
package com.infinity.glass.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kerry.baumer
 *
 */
public class UserIdentity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String idName;
	private String emailAddress;
	private String userName;
	private Date created;
	private Date lastUpdated;
	private Date lastLogin;
	private boolean guest;
	
	/**
	 * 
	 */
	public UserIdentity() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the name
	 */
	public String getIdName() {
		return idName;
	}

	/**
	 * @param name the name to set
	 */
	public void setIdName(String name) {
		this.idName = name;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the guest
	 */
	public boolean isGuest() {
		return guest;
	}

	/**
	 * @param guest the guest to set
	 */
	public void setGuest(boolean guest) {
		this.guest = guest;
	}

}
