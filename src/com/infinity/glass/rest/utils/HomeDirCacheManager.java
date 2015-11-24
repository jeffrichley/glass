package com.infinity.glass.rest.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.infinity.glass.config.GlassConfiguration;
import com.infinity.glass.config.GlassConfigurationFactory;
import com.infinity.glass.rest.data.MatrixData;

public class HomeDirCacheManager implements CacheManager {

	@Override
	public void cleanupStaleData() {
		final GlassConfiguration conf = GlassConfigurationFactory.getConfiguration();
		cleanupDirectory(conf.getCacheDirectory(), ".glass.cache");
		cleanupDirectory(conf.getImportDirectory(), ".csv");
	}
	
	
	protected void cleanupDirectory(String directoryName, String extensionToDelete) {
		final long cutoff = System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
		final File dir = new File(directoryName);
		if (dir.isDirectory()) {
			final File[] files = dir.listFiles();
			for (final File file : files) {
				if (file.isFile() && file.lastModified() < cutoff && file.getName().endsWith(extensionToDelete)) {
					final boolean deletedSuccessfully = file.delete();
					if (!deletedSuccessfully) {
						// handle error?
					}
				}
			}
		}
	}
	
	@Override
	public void cache(String id, String data) {
		FileWriter out = null;
		String fileName = getFileName(id);
		
		try {
			out = new FileWriter(fileName);
			out.write(data);
		} catch (IOException e) {
			throw new RuntimeException("Unable to save " + fileName, e);
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to save " + fileName, e);
				}
			}
		}
	}

	private String getFileName(String id) {
		return GlassConfigurationFactory.getConfiguration().getCacheDirectory() + id + ".glass.cache";
	}

	@Override
	public String getData(String id) {
		StringBuilder buff = null;
		
		LineNumberReader in = null;
		String fileName = getFileName(id);
		
		File file = new File(fileName);
		if (file.exists()) {
			buff = new StringBuilder();
			try {
				in = new LineNumberReader(new FileReader(file));
				
				String line = in.readLine();
				while (line != null) {
					buff.append(line);
					line = in.readLine();
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to read " + fileName, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						throw new RuntimeException("Unable to read " + fileName, e);
					}
				}
			}
		}
			
		String answer = null;
		if (buff != null) {
			answer = buff.toString();
		}
		return answer;
	}

	// may want to stream this in.
	@Override
	public MatrixData getData(String id, Class<MatrixData> clazz) {
		String data = getData(id);
		return new Gson().fromJson(data, clazz);
	}
}
