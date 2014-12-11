package com.infinity.glass.rest;

import java.util.List;

import javax.resource.spi.IllegalStateException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.manager.ManagerFactory;
import com.infinity.glass.model.UserIdentity;
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

	@SuppressWarnings("unused")
	@GET
	@Path("{fileId}")
	@Produces("application/json")
	public Response getUiConfiguration(@PathParam("fileId")final String fileId, @Context ServletContext context,
			@Context HttpServletRequest req) {
		HttpSession sess = req.getSession(true);
		try {
			UserIdentity ui = ManagerFactory.getUserIdentityManager(context).getUserIdentity(req);
			DatasetManager dsm = ManagerFactory.getDatasetManager(context);
			dsm.importDataset(ui, fileId);
			UiConfig conf = getCachedConfig("header-info-", fileId);
			if (conf == null) {
				throw new IllegalStateException("Failed to import CSV file!");
			}
			return Response.ok().entity(conf).build();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return Response.status(Status.NO_CONTENT).build();
		

		
	}

}
