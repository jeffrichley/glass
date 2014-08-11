package com.infinity.glass.config;

import com.google.inject.AbstractModule;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.NullCacheManager;

public class GlassModule extends AbstractModule {

	@Override
	protected void configure() {
		//bind(CacheManager.class).to(HomeDirCacheManager.class);
		bind(CacheManager.class).to(NullCacheManager.class);
	}

}
