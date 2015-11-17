package com.infinity.glass.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.manager.FSDatasetManager;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.ConfigurableDirCacheManager;

public class GlassModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(CacheManager.class).to(ConfigurableDirCacheManager.class);
		bind(String.class).annotatedWith(Names.named("Cache-Directory")).toInstance("C:/tmp/glass_boo_dir");
		
		// data manager stuff
		bind(DatasetManager.class).to(FSDatasetManager.class);
		bind(String.class).annotatedWith(Names.named("DATA_DIR")).toInstance("C:/tmp/glass_data_dir");
		bind(String.class).annotatedWith(Names.named("TEMP_DIR")).toInstance("C:/tmp/glass_temp_dir");
		
	}

}
