package com.infinity.glass.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.manager.IdentityManager;
import com.infinity.glass.rest.LabelDescriber;
import com.infinity.glass.rest.NumericDescriber;
import com.infinity.glass.rest.utils.CacheManager;

/**
 * Responsible for creating components and stitching them all together correctly.
 * @author Jeffrey.Richley
 */
public class ConfigurationUtils {
	
	private static Injector injector = null;
	
	static {
		// create the injector only once
		injector = Guice.createInjector(new GlassModule());
	}

	/**
	 * Create a <code>LabelDescriber</code>
	 * @return A correctly wired <code>LabelDescriber</code>
	 */
	public static LabelDescriber getLabelDescriber() {
		return createObject(LabelDescriber.class);
	}

	/**
	 * Create a <code>NumericDescriber</code>
	 * @return A correctly wired <code>NumericDescriber</code>
	 */
	public static NumericDescriber getNumericDescriber() {
		return createObject(NumericDescriber.class);
	}

	public static DatasetManager getDatasetManager() {
		return createObject(DatasetManager.class);
	}

	public static CacheManager getCacheManager() {
		return createObject(CacheManager.class);
	}

	public static IdentityManager getUserIdentityManager() {
		return createObject(IdentityManager.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> T createObject(Class<?> clazz) {
	    T instance = (T) injector.getInstance(clazz);
	    return instance;
	}


}
