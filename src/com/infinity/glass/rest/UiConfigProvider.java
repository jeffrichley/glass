package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.UiConfig;
import com.infinity.glass.rest.data.UiConfig.ColumnHeader;

@Stateless
@Path("/uiconfig")
public class UiConfigProvider extends GlassDataProvider<UiConfig> {
	
	@GET
	@Path("/{dataId}")
	@Produces("application/json")
	public Response getUiConfiguration(@PathParam("dataId") String dataId) {
		UiConfig uiConfig = getCachedConfig("header-info-" + dataId + "-");
		if (uiConfig == null) {
			final MatrixData matrix = new DataProvider().getMatrixData(getDataFile(dataId));
			uiConfig = getUiConfig(matrix);
			cacheData(uiConfig, "header-info-" + dataId + "-");
			cacheMatrix(matrix, dataId);
		}
		cleanupCache();
		
		return Response.ok().entity(uiConfig).build();
	}
	
	private UiConfig getUiConfig(MatrixData matrix) {
		final UiConfig uiConfig = new UiConfig();
		
		final List<String> headers = matrix.getHeadings();
		if (headers != null) {
			final List<ColumnHeader> columnHeaders = new ArrayList<ColumnHeader>(headers.size());
			for (final String header : headers) {
				final DataColumn<?> dataColumn = matrix.getDataColumn(header);
				columnHeaders.add(new ColumnHeader(dataColumn.getType(), dataColumn.getLabel()));
			}
			uiConfig.setHeaders(columnHeaders);
		}
		
		return uiConfig;
	}
	
	private void cacheMatrix(MatrixData matrix, String dataId) {
		for (final String header : matrix.getHeadings()) {
			final DataColumn<?> dataColumn = matrix.getDataColumn(header);
			cacheData(dataColumn, "data-column-" + dataId + "-" + header);
		}
	}
}
