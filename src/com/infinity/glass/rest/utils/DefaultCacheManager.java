package com.infinity.glass.rest.utils;

import com.infinity.glass.rest.data.MatrixData;

public class DefaultCacheManager implements CacheManager {

	@Override
	public void cache(String id, String data) {
		
	}

	@Override
	public String getData(String id) {
		return null;
	}

	@Override
	public MatrixData getData(String string, Class<MatrixData> clazz) {
		return null;
	}

	@Override
	public void cleanupStaleData() {
		
	}
}
