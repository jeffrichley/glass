package com.infinity.glass.rest.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import com.google.gson.Gson;
import com.infinity.glass.rest.data.MatrixData;

public class HomeDirCacheManager implements CacheManager {

	private static HomeDirCacheManager instance = null;
	
	private HomeDirCacheManager() {}
	
	public static CacheManager getInstance() {
		if(instance == null) {
			instance = new HomeDirCacheManager();
		}
		return instance;
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
		String fileName;
		String cacheDir = System.getProperty("user.home") + "/.glass-cache/";
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
