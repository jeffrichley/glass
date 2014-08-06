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
public class UiConfigProvider extends GlassDataProvider<UiConfig> {
	
	@GET
	@Produces("application/json")
	public Response getUiConfiguration(@Context ServletContext context) {
		UiConfig conf = getCachedConfig("header-info-");

		if (conf == null) {
			conf = new UiConfig();
			List<String> headings = new DataProvider().getHeaderData(context);
			conf.setHeaders(headings);
			cacheData(conf, "header-info-");
		}
		
		return Response.ok().entity(conf).build();
	}

}
