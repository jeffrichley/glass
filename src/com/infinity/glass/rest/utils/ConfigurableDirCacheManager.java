package com.infinity.glass.rest.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.infinity.glass.manager.CacheCleaner;
import com.infinity.glass.rest.data.MatrixData;

public class ConfigurableDirCacheManager implements CacheManager {

	private final String cacheDirectory;
	private final CacheCleaner cacheCleaner;

	@Inject
	public ConfigurableDirCacheManager(@Named("Cache-Directory") String cacheDirectory, CacheCleaner cacheCleaner) {
		if (!cacheDirectory.endsWith("/") || !cacheDirectory.endsWith("\\")) {
			cacheDirectory += "/";
		}
		
		this.cacheDirectory = cacheDirectory;
		this.cacheCleaner = cacheCleaner;
	}
	
	public static CacheManager getInstance() {
		return new ConfigurableDirCacheManager(System.getProperty("user.home") + "/.glass-cache/", null);
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
			
			// oh by the way, make sure to clean stuff up periodically
			if (cacheCleaner != null) {
				cacheCleaner.cleanCache();
			}
		}
	}

	private String getFileName(String id) {
		String fileName;
		String cacheDir = cacheDirectory;
		new File(cacheDir).mkdirs();
		fileName = cacheDir + id + ".txt";
		return fileName;
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

	@Override
	public MatrixData getData(String id, Class<MatrixData> clazz) {
		String data = getData(id);
		return new Gson().fromJson(data, clazz);
	}

	
}
