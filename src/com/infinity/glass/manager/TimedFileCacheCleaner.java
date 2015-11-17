package com.infinity.glass.manager;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Cleans the cache of files that are older than the given time
 * @author Jeffrey.Richley
 */
public class TimedFileCacheCleaner implements CacheCleaner {
	
	private final String cacheDirectory;
	private final Long cacheCleanTime;
	private final String dataDirectory;
	private final Long dataCleanTime;

	@Inject
	public TimedFileCacheCleaner(@Named("Cache-Directory") String cacheDirectory, 
								 @Named("Cache-Clean-Time") Long cacheCleanTime,
								 @Named("Data-Directory") String dataDirectory,
								 @Named("Data-CleanTime") Long dataCleanTime) {
		
		if (!cacheDirectory.endsWith("/") || !cacheDirectory.endsWith("\\")) {
			cacheDirectory += "/";
		}
		
		if (!dataDirectory.endsWith("/") || !dataDirectory.endsWith("\\")) {
			dataDirectory += "/";
		}
		
		this.cacheDirectory = cacheDirectory;
		this.cacheCleanTime = cacheCleanTime;
		this.dataDirectory = dataDirectory;
		this.dataCleanTime = dataCleanTime;
	}

	@Override
	public void cleanCache() {
		cleanCacheFiles();
	}

	private void cleanCacheFiles() {
		File dir = new File(cacheDirectory);
		File[] files = dir.listFiles();
		long now = System.currentTimeMillis();
		
		for (File file : files) {
			long age = now - file.lastModified();
			if (age > cacheCleanTime) {
				file.delete();
			}
		}
	}
	
	private void cleanDataFiles() {
		File dir = new File(dataDirectory);
		File[] files = dir.listFiles();
		long now = System.currentTimeMillis();
		
		for (File file : files) {
			long age = now - file.lastModified();
			if (age > dataCleanTime) {
				file.delete();
			}
		}
	}

}
