package com.infinity.glass.rest.utils;

import com.infinity.glass.rest.data.MatrixData;

public class NullCacheManager implements CacheManager {

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

}
