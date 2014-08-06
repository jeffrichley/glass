package com.infinity.glass.rest.utils;

public interface CacheManager {

	public void cache(String id, String data);
	public String getData(String id);
	
}
