package com.infinity.glass.config;

import java.util.Collections;
import java.util.Set;

public class GlassConfiguration {

	private final String importDirectory;
	private final String cacheDirectory;
	
	protected final Set<String> ignoredFeatures;
	
	protected GlassConfiguration(String importDirectory, String cacheDirectory, Set<String> ignoredFeatures) {
		this.importDirectory = importDirectory.endsWith("/") ? importDirectory : importDirectory + "/";
		this.cacheDirectory = cacheDirectory.endsWith("/") ? cacheDirectory : cacheDirectory + "/";
		this.ignoredFeatures = Collections.unmodifiableSet(ignoredFeatures);
	}

	public String getImportDirectory() {
		return importDirectory;
	}

	public String getCacheDirectory() {
		return cacheDirectory;
	}
	
	public Set<String> getIgnoredFeatures() {
		return ignoredFeatures;
	}
}
