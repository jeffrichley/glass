/**
 * 
 */
package com.infinity.glass.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.infinity.glass.model.DatasetSummaryBean;
import com.infinity.glass.model.UserDatasetBean;
import com.infinity.glass.model.UserIdentity;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.UiConfig;
import com.infinity.glass.rest.utils.CacheManager;

/**
 * @author kerry.baumer
 *
 */
public class FSDatasetManager extends AbstractManager implements DatasetManager {

	private static String FILE_SEP = System.getProperty("file.separator");
	private final String DATA_DIR;
	private final String TEMP_DIR;

	/**
	 * 
	 */
	public FSDatasetManager(ServletContext context) {
		super(context);
		DATA_DIR = (String) context.getAttribute("FSDatasetManager.data.dir");
		TEMP_DIR = (String) context.getAttribute("FSDatasetManager.temp.dir");
	}
	
	private String getDatasetPath(final String datasetId) {
		final String dataFileName = String.format("%s%s%s%s", DATA_DIR, FILE_SEP, datasetId,
				datasetId.endsWith(".csv") ? "" : ".csv");
		return dataFileName;
	}

	private String getMatrixCachePath(final String datasetId) {
		final String dataFileName = String.format("%s%smatrix-data-%s%s", TEMP_DIR, FILE_SEP, datasetId,
				datasetId.endsWith(".csv") ? "" : ".ser");
		return dataFileName;
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#getDatasetsForUser(java.lang.String)
	 */
	@Override
	public DatasetSummaryBean getDatasetsForUser(final UserIdentity userId) {
		DatasetSummaryBean result = new DatasetSummaryBean();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#SaveDataset(java.io.OutputStream)
	 */
	@Override
	public UserDatasetBean saveDataset(UserIdentity userIdentity, String origFileName, InputStream stream) throws IOException {
		final String datasetId = UUID.randomUUID().toString().replaceAll("=", "");
		final String dataSetName = getDatasetPath(datasetId);
		FileWriter fw = new FileWriter(dataSetName);
		LineNumberReader dataIn = null;
		UserDatasetBean result = new UserDatasetBean();
		try {
			result.setDatasetId(datasetId);
			result.setLastSaved(new Date());
			result.setOriginalFileName(origFileName);
			result.setSize(stream.available());
			result.setUser(userIdentity);
			dataIn = new LineNumberReader(new InputStreamReader(stream));
			String line;
			while ((line = dataIn.readLine()) != null) {
				fw.write(line);
				fw.write("\n");;
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to read " + dataSetName, e);
		} finally {
			try {
				dataIn.close();
				fw.close();
			} catch (IOException e) {
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.infinity.glass.manager.DatasetManager#LoadDataset(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public void LoadDataset(String userId, String datasetId, OutputStream stream) throws IOException {
	}

	@Override
	public UserDatasetBean importDataset(UserIdentity userIdentity, String datasetId) {
		CacheManager cacheManager = ManagerFactory.getCacheManager(); 
		final String dataFileName = getDatasetPath(datasetId);
		File dataFile = new File(dataFileName);
		DataProvider dp = new DataProvider();
		UiConfig conf = new UiConfig();
		List<String> headings = dp.getHeaderData(dataFile);
		conf.setHeaders(headings);
		cacheManager.cache("header-info-" + datasetId,  new Gson().toJson(conf));
		return null;
	}

	@Override
	public MatrixData getMatrixData(String datasetId) {
		MatrixData md = readMatrixData(datasetId);
		if(md == null) {
			final String dataFileName = getDatasetPath(datasetId);
			File dataFile = new File(dataFileName);
			md = new DataProvider().getMatrixData(dataFile);
			writeMatrixData(datasetId, md);
		}
		return md;
	}

	private MatrixData readMatrixData(final String datasetId) {
		final String matrixDataPath = getMatrixCachePath(datasetId);
		final String inputDataPath = getDatasetPath(datasetId);
		MatrixData data = null;
		File matrixFile, dataFile;
		try {
			matrixFile = new File(matrixDataPath);
			dataFile = new File(inputDataPath);
			if(matrixFile.exists() && matrixFile.canRead() && !isNewer(dataFile, matrixFile)) {
				InputStream in = new FileInputStream(matrixFile);
				ObjectInput oi = new ObjectInputStream(in);
				data = (MatrixData) oi.readObject();
				oi.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return data;
	}
	
	/**
	 * Returns true if file2 is newer than file1
	 * @param file1
	 * @param file2
	 * @return
	 */
	private boolean isNewer(final File file1, final File file2) {
		long file1LastMod = file1.lastModified();
		long file2LastMod = file2.lastModified();
		return file1LastMod > file2LastMod;
	}

	private void writeMatrixData(final String datasetId, final MatrixData md) {
		final String matrixDataPath = getMatrixCachePath(datasetId); 
		File file;
		try {
			file = new File(matrixDataPath);
			OutputStream out = new FileOutputStream(file);
			ObjectOutput os = new ObjectOutputStream(out);
			os.writeObject(md);
			os.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

	}

}