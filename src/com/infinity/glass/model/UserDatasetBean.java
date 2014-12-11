package com.infinity.glass.model;

import java.io.Serializable;
import java.util.Date;

public class UserDatasetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String datasetId;
	private String originalFileName;
	private UserIdentity user;
	private Date lastSaved;
	private int size;
	
	public UserDatasetBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the datasetId
	 */
	public String getDatasetId() {
		return datasetId;
	}

	/**
	 * @param datasetId the datasetId to set
	 */
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	/**
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the user
	 */
	public UserIdentity getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserIdentity user) {
		this.user = user;
	}

	/**
	 * @return the lastSaved
	 */
	public Date getLastSaved() {
		return lastSaved;
	}

	/**
	 * @param lastSaved the lastSaved to set
	 */
	public void setLastSaved(Date lastSaved) {
		this.lastSaved = lastSaved;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
