package com.infinity.glass.manager;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	private final Integer cacheMaxFiles;

	@Inject
	public TimedFileCacheCleaner(@Named("Cache-Directory") String cacheDirectory, 
								 @Named("Cache-Clean-Time") Long cacheCleanTime,
								 @Named("Data-Directory") String dataDirectory,
								 @Named("Data-CleanTime") Long dataCleanTime,
								 @Named("Cache-Max-Files") Integer cacheMaxFiles) {
		
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
		this.cacheMaxFiles = cacheMaxFiles;
	}

	@Override
	public void cleanCache() {
		cleanCacheFiles();
		cleanDataFiles();
//		pruneToMaxFiles();
	}

	private void pruneToMaxFiles() {
		File dir = new File(cacheDirectory);
		File[] files = dir.listFiles();
		
		if (files.length > cacheMaxFiles) {
			List<File> fileList = Arrays.asList(files);
			Collections.sort(fileList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					Long oneTime = o1.lastModified();
					Long twoTime = o2.lastModified();
					return twoTime.compareTo(oneTime);
				}
			});

			int numFilesToDelete = files.length - cacheMaxFiles;
			for (int i = 0; i < numFilesToDelete && numFilesToDelete > 0; i++) {
				numFilesToDelete--;
				File deleteMe = fileList.get(i);
				// make sure we only delete cache files and ones that are at least 5 minutes old
				long now = System.currentTimeMillis();
				if (deleteMe.getName().endsWith(".txt") && now - deleteMe.lastModified() > 1000 * 60 * 1) {
					deleteMe.delete();
				}
			}
		}
		
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
