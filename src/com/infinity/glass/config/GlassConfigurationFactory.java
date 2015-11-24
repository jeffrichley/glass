package com.infinity.glass.config;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GlassConfigurationFactory {

	private static final String CONFIGURATION_FILE = "/WEB-INF/glass.conf";
	
	private static GlassConfiguration glassConfiguration;

	public static void create(ServletContext context) {
		if (glassConfiguration == null) {
			try {
				final PropertiesConfiguration properties = new PropertiesConfiguration(context.getResource(CONFIGURATION_FILE));
				final String importDirectory = properties.getString("import.directory");
				final String cacheDirectory = properties.getString("cache.directory");
				final Set<String> ignoredFeatures = new HashSet<String>();
				if (properties.containsKey("ignored.features")) {
					for (final String ignoredFeature : properties.getString("ignored.features").split(",\\s*")) {
						ignoredFeatures.add(ignoredFeature);
					}
				}
				glassConfiguration = new GlassConfiguration(importDirectory, cacheDirectory, ignoredFeatures);
			} catch (ConfigurationException e) {
				throw new IllegalStateException("glass conf file was not found in classpath", e);
			} catch (MalformedURLException e) {
				throw new IllegalStateException("glass conf file could not be loaded from the servlet context", e);
			}
		}
	}
	
	public static GlassConfiguration getConfiguration() {
		return glassConfiguration;
	}
}
