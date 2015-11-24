package com.infinity.glass.rest.utils;

import com.infinity.glass.rest.data.MatrixData;

public interface CacheManager {

	public void cleanupStaleData();
	public void cache(String id, String data);
	public String getData(String id);
	public MatrixData getData(String string, Class<MatrixData> clazz);
	
}
