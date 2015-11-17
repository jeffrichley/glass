package com.infinity.glass.manager;

/**
 * Delegated class for cleaning caches
 * @author Jeffrey.Richley
 */
public interface CacheCleaner {

	/**
	 * Clean any part of the cache that needs to be cleaned
	 */
	void cleanCache();
	
}
