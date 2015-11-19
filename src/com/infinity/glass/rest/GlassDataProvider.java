package com.infinity.glass.rest;

import java.lang.reflect.ParameterizedType;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.utils.CacheManager;

public class GlassDataProvider<T> {

	private final CacheManager cacheManager = ConfigurationUtils.getCacheManager();
	
	protected String getCacheId(String id) {
		int start = DataProvider.WEB_INF_DATA_XTRACT_CSV.lastIndexOf("\\");
		if (start < 0) {
			start = DataProvider.WEB_INF_DATA_XTRACT_CSV.lastIndexOf("/");
		}
		return id+DataProvider.WEB_INF_DATA_XTRACT_CSV.substring(start+1);
	}

	protected String getCacheId(final String key, final String id) {
		return key.concat(id);
	}

	protected String getDataFilePath(final ServletContext context, final String token) {
		final String fileSep = System.getProperty("file.separator");
		final String dataDir = (String) context.getAttribute("FSDatasetManager.temp.dir");
		final String dataFileName = String.format("%s%s%s%s", dataDir, fileSep, token,
				token.endsWith(".csv") ? "" : ".csv");
		return dataFileName;
	}
	
	@SuppressWarnings("unchecked")
	protected T getCachedConfig(String id) {
		String cacheId = getCacheId(id);
		String cachedData = cacheManager.getData(cacheId);
		
		T answer = null;
		if (cachedData != null) {
			Class<T> genericClass = (Class<T>) ((ParameterizedType) getClass()
                    				.getGenericSuperclass()).getActualTypeArguments()[0];
			answer = (T) new Gson().fromJson(cachedData, genericClass);
		}
		
		return answer;
	}
	
	@SuppressWarnings("unchecked")
	protected T getCachedConfig(final String key, String id) {
		String cacheId = getCacheId(key, id);
		String cachedData = cacheManager.getData(cacheId);
		
		T answer = null;
		if (cachedData != null) {
			Class<T> genericClass = (Class<T>) ((ParameterizedType) getClass()
                    				.getGenericSuperclass()).getActualTypeArguments()[0];
			answer = (T) new Gson().fromJson(cachedData, genericClass);
		}
		
		return answer;
	}
	
	protected CompareData getCachedConfig(final String key, String id, Class<? extends CompareData> clazz) {
		String cacheId = getCacheId(key, id);
		String cachedData = cacheManager.getData(cacheId);
		
		CompareData answer = null;
		if (cachedData != null) {
			answer = new Gson().fromJson(cachedData, clazz);
		}
		
		return answer;
	}
	
	protected void cacheData(T data, String id) {
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
		cacheManager.cache(getCacheId(id), gson.toJson(data));		
	}

	protected void cacheData(T data, String key, final String id) {
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
		cacheManager.cache(getCacheId(key, id), gson.toJson(data));
	}

}
