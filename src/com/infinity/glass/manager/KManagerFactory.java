/**
 * 
 */
package com.infinity.glass.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;

import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.ConfigurableDirCacheManager;

/**
 * @author kerry.baumer
 *
 */
@SuppressWarnings("unchecked")
public final class KManagerFactory {

	public static String DEFAULT_DSM_CLASS = "com.infinity.glass.manager.FauxDatasetManager";
	public static String DEFAULT_UIM_CLASS = "com.infinity.glass.manager.FauxUserIdentityManager";
	public static String DEFAULT_UPM_CLASS = "com.infinity.glass.manager.FauxUserPersistenceManager";
	
	/**
	 * Factory methods, no do not instantiate!
	 */
	private KManagerFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static CacheManager getCacheManager() {
		return ConfigurableDirCacheManager.getInstance();
	}
	
	public static DatasetManager getDatasetManager(ServletContext context) throws IllegalStateException {
        String className = (String) context.getAttribute("DSM_CLASS");
        if(className == null || className.length() == 0) {
        	className = DEFAULT_DSM_CLASS;
        }
        DatasetManager dm = null;
        try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> meth = (Constructor<DatasetManager>) clazz.getConstructor(ServletContext.class);
			dm = (DatasetManager) meth.newInstance(context);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (ClassCastException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
        return dm;
	}

	public static IdentityManager getUserIdentityManager(ServletContext context) throws IllegalStateException {
        String className = (String) context.getAttribute("UIM_CLASS");
        if(className == null || className.length() == 0) {
        	className = DEFAULT_UIM_CLASS;
        }
        IdentityManager dm = null;
        try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> meth = (Constructor<DatasetManager>) clazz.getConstructor(ServletContext.class);
			dm = (IdentityManager) meth.newInstance(context);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (ClassCastException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
        return dm;
	}
	
	public static UserPersistence getUserPersistenceManager(ServletContext context) throws IllegalStateException {
        String className = (String) context.getAttribute("UPM_CLASS");
        if(className == null || className.length() == 0) {
        	className = DEFAULT_UPM_CLASS;
        }
        UserPersistence dm = null;
        try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> meth = (Constructor<DatasetManager>) clazz.getConstructor(ServletContext.class);
			dm = (UserPersistence) meth.newInstance(context);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (ClassCastException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
        return dm;
	}
}
