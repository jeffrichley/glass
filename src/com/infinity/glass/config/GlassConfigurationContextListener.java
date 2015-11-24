package com.infinity.glass.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GlassConfigurationContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// do nothing
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		final ServletContext context = contextEvent.getServletContext();
		GlassConfigurationFactory.create(context);
	}
}
