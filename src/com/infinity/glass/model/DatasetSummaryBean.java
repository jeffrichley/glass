/**
 * 
 */
package com.infinity.glass.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kbaumer
 *
 */
public class DatasetSummaryBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<UserDatasetBean> personalData = new LinkedList<UserDatasetBean>();
	private List<UserDatasetBean> publicData = new LinkedList<UserDatasetBean>();
	
	/**
	 * 
	 */
	public DatasetSummaryBean() {
		// TODO Auto-generated constructor stub
	}

	public void addPersonalData(final UserDatasetBean bean) {
		personalData.add(bean);
	}
	
	public List<UserDatasetBean> getPersonalData() {
		return personalData;
	}

	public void setPersonalData(List<UserDatasetBean> personalData) {
		this.personalData = personalData;
	}

	public void addPublicData(final UserDatasetBean bean) {
		publicData.add(bean);
	}
	
	public List<UserDatasetBean> getPublicData() {
		return publicData;
	}

	public void setPublicData(List<UserDatasetBean> publicData) {
		this.publicData = publicData;
	}

}
