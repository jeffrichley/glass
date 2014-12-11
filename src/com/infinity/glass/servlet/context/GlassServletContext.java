package com.infinity.glass.servlet.context;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinity.glass.manager.ManagerFactory;
import com.infinity.glass.rest.utils.CacheManager;


public final class GlassServletContext implements ServletContextListener {
	private static Logger LOGGER = LoggerFactory.getLogger(GlassServletContext.class);

	private ServletContext context;
   
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Context Created");
        context = contextEvent.getServletContext();
        // set variable to servlet context
        final String configDirName = System.getProperty("sys.conf.dir");
        if(configDirName == null || configDirName.length() == 0) {
        	loadDefaults();
        } else {
        	// load configuration file
        	final String configFileName = String.format("%s%sglass.conf", configDirName, 
        			System.getProperty("file.separator"));
    		FileInputStream fis = null;
    		final Properties tempProps = new Properties();
    		try {
    			fis = new FileInputStream(configFileName);
    			tempProps.loadFromXML(fis);
    			Enumeration<Object> keys = tempProps.keys();
    			while(keys.hasMoreElements()) {
    				String key = (String)keys.nextElement();
    				String value = tempProps.getProperty(key);
    				if(value.startsWith("${")) {
    					String envKey = value.substring(2, value.lastIndexOf('}'));
    					value = System.getProperty(envKey);
    				}
    				context.setAttribute(key, value);
    			}
    		} catch (final FileNotFoundException e) {
    			LOGGER.error("FileNotFoundException loading properties file: " + e.getMessage() + ". Using defaults.");
            	loadDefaults();
    		} catch (final InvalidPropertiesFormatException e) {
    			LOGGER.error("InvalidPropertiesFormatException loading properties file: " + e.getMessage() + ". Using defaults.");
            	loadDefaults();
    		} catch (final IOException e) {
    			LOGGER.error("IOException loading properties file: " + e.getMessage() + ". Using defaults.");
            	loadDefaults();
    		} finally {
    			final CacheManager cacheManager = ManagerFactory.getCacheManager();
    			context.setAttribute("CACHE_MANAGER", cacheManager);
    			try {
    				if (fis != null) {
    					fis.close();
    				}
    			} catch (final IOException ignore) {
    				// nothing we can do there.
    			}
    		}
        }
        
    }
    
    private void loadDefaults() {
    	context.setAttribute("DSM_CLASS", ManagerFactory.DEFAULT_DSM_CLASS);
    	context.setAttribute("UIM_CLASS", ManagerFactory.DEFAULT_UIM_CLASS);
    	context.setAttribute("UPM_CLASS", ManagerFactory.DEFAULT_UPM_CLASS);
    }
    
    public void contextDestroyed(ServletContextEvent contextEvent) {
        context = contextEvent.getServletContext();
        System.out.println("Context Destroyed");
    }
}
