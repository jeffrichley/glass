package com.infinity.glass.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestApplication extends Application {
	
	private final Set<Object> singletons = new HashSet<Object>();
	private final Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public RestApplication(){
	     singletons.add(new UiConfigProvider());
	     singletons.add(new DescribeProvider());
	     singletons.add(new CompareProvider());
	}
	
	@Override
	public Set<Class<?>> getClasses() {
	     return empty;
	}
	
	@Override
	public Set<Object> getSingletons() {
	     return singletons;
	}
}
