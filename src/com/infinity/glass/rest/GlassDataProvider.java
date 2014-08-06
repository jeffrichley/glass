package com.infinity.glass.rest;

import java.lang.reflect.ParameterizedType;

import com.google.gson.Gson;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.HomeDirCacheManager;

public class GlassDataProvider<T> {

	private final CacheManager cacheManager = new HomeDirCacheManager();
	
	protected String getCacheId(String id) {
		int start = DataProvider.WEB_INF_DATA_XTRACT_CSV.lastIndexOf("\\");
		if (start < 0) {
			start = DataProvider.WEB_INF_DATA_XTRACT_CSV.lastIndexOf("/");
		}
		return id+DataProvider.WEB_INF_DATA_XTRACT_CSV.substring(start+1);
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
	
	protected void cacheData(T data, String id) {
		cacheManager.cache(getCacheId(id), new Gson().toJson(data));		
	}

}
