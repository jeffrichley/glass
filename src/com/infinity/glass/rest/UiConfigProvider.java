package com.infinity.glass.rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.UiConfig;

@Path("/uiconfig")
public class UiConfigProvider {
	
	@GET
	@Produces("application/json")
	public Response getUiConfiguration(@Context ServletContext context) {
		List<String> headings = new DataProvider().getHeaderData(context);
		UiConfig conf = new UiConfig();
		
		conf.setHeaders(headings);
		
		return Response.ok().entity(conf).build();
	}
	
}
