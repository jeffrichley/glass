package com.infinity.glass.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.infinity.glass.manager.CacheCleaner;
import com.infinity.glass.manager.DataPreProcessor;
import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.manager.FSDatasetManager;
import com.infinity.glass.manager.ThreadedDataPreProcessor;
import com.infinity.glass.manager.TimedFileCacheCleaner;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.ConfigurableDirCacheManager;

public class GlassModule extends AbstractModule {

	// C:/tmp/glass_temp_dir
	// C:/tmp/glass_data_dir
	
	@Override
	protected void configure() {
		String dataDirectory = System.getProperty("Glass-Data-Directory");
		String cacheDirectory = System.getProperty("Glass-Cache-Directory");
		String cacheCleanTime = System.getProperty("Glass-Cache-Clean-Time");
		String dataCleanTime = System.getProperty("Glass-Data-Clean-Time");
		String cacheMaxFiles = System.getProperty("Glass-Cache-Max-Files");
		// 3 hours
		Long cacheCleanTimeVal = (long) (1000 * 60 * 60 * 3);
		// 14 days
		Long dataCleanTimeVal = (long) (1000 * 60 * 60 * 24 * 14);
		// keep 25000 files
		Integer cacheMaxFilesVal = 25000;
		
		if (dataDirectory == null) {
			dataDirectory = System.getProperty("user.home") + "/.glass-data";
		}
		
		if (cacheDirectory == null) {
			cacheDirectory = System.getProperty("user.home") + "/.glass-cache";
		}
		
		if (cacheCleanTime != null) {
			cacheCleanTimeVal = Long.parseLong(cacheCleanTime);
		}
		
		if (dataCleanTime != null) {
			dataCleanTimeVal = Long.parseLong(dataCleanTime);
		}
		
		if (cacheMaxFiles != null) {
			cacheMaxFilesVal = Integer.parseInt(cacheMaxFiles);
		}
		
		// cache manager stuff
		bind(CacheManager.class).to(ConfigurableDirCacheManager.class);
		bind(String.class).annotatedWith(Names.named("Cache-Directory")).toInstance(cacheDirectory);
		bind(String.class).annotatedWith(Names.named("Data-Directory")).toInstance(dataDirectory);
		bind(CacheCleaner.class).to(TimedFileCacheCleaner.class);
		bind(Long.class).annotatedWith(Names.named("Cache-Clean-Time")).toInstance(cacheCleanTimeVal);
		bind(Long.class).annotatedWith(Names.named("Data-CleanTime")).toInstance(dataCleanTimeVal);
		bind(Integer.class).annotatedWith(Names.named("Cache-Max-Files")).toInstance(cacheMaxFilesVal);
		
		
		// data manager stuff
		bind(DatasetManager.class).to(FSDatasetManager.class);
		bind(String.class).annotatedWith(Names.named("DATA_DIR")).toInstance(dataDirectory);
		bind(String.class).annotatedWith(Names.named("TEMP_DIR")).toInstance(cacheDirectory);
		
		// preprocessing stuff
		bind(DataPreProcessor.class).to(ThreadedDataPreProcessor.class);
	}

}
