/**
 * 
 */
package com.infinity.glass.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.infinity.glass.model.DatasetSummaryBean;
import com.infinity.glass.model.UserDatasetBean;
import com.infinity.glass.model.UserIdentity;
import com.infinity.glass.rest.data.MatrixData;

/**
 * @author kbaumer
 *
 */
public class FauxDatasetManager extends AbstractManager implements DatasetManager {

	private static final Map<String, Map<String, FauxUserDatasetBean>> fauxData = new HashMap<String,
			Map<String, FauxUserDatasetBean>>();

	/**
	 * 
	 */
	public FauxDatasetManager(ServletContext context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#getDatasetsForUser(java.lang.String)
	 */
	@Override
	public DatasetSummaryBean getDatasetsForUser(final UserIdentity userId) {
		DatasetSummaryBean result = new DatasetSummaryBean();
		Map<String, FauxUserDatasetBean> userData = fauxData.get(userId.getUserId());
		if(userData != null) {
			for(FauxUserDatasetBean bean : userData.values()) {
				result.addPersonalData(bean.getUserDataset());
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#SaveDataset(java.io.OutputStream)
	 */
	@Override
	public UserDatasetBean saveDataset(UserIdentity userIdentity, String origFileName, InputStream stream) throws IOException {
		
		UserDatasetBean result = new UserDatasetBean();
		result.setDatasetId(UUID.randomUUID().toString());
		result.setLastSaved(new Date(System.currentTimeMillis()));
		result.setOriginalFileName(origFileName);
		result.setSize(stream.available());
		result.setUser(userIdentity);
		
		Map<String, FauxUserDatasetBean> userData = fauxData.get(userIdentity.getUserId());
		if(userData == null) {
			userData = new HashMap<String, FauxUserDatasetBean>();
			fauxData.put(userIdentity.getUserId(), userData);
		}
		FauxUserDatasetBean fud = new FauxUserDatasetBean();
		fud.setUserDataset(result);
		byte[] b = new byte[stream.available()];
		stream.read(b);
		fud.setData(b);
		userData.put(result.getDatasetId(), fud);
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#LoadDataset(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public void LoadDataset(String userId, String datasetId, OutputStream stream) throws IOException {
		Map<String, FauxUserDatasetBean> userData = fauxData.get(userId);
		if(userData != null) {
			FauxUserDatasetBean ds = userData.get(datasetId);
			if(ds != null) {
				stream.write(ds.getData());
				stream.flush();
			}
		}
	}
	
	private class FauxUserDatasetBean {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private UserDatasetBean udb;
		private byte[] data;
		
		public UserDatasetBean getUserDataset() { return udb; }
		public void setUserDataset(UserDatasetBean udb) { this.udb = udb; }
		public byte[] getData() { return data; }
		public void setData(byte[] newData) { data = newData; }
		
	}

	@Override
	public UserDatasetBean importDataset(UserIdentity userIdentity, String datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatrixData getMatrixData(String datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

}