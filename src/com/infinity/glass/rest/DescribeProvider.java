package com.infinity.glass.rest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.MatrixData;

/**
 * A <code>DataProvider</code> that specializes in describing one particular
 * column and all the information about it.
 * @author Jeffrey.Richley
 */
@Path("/describe")
public class DescribeProvider extends GlassDataProvider<DescribeData> {

	/**
	 * Determines what type of column the request is for and returns the correct
	 * type of description for it
	 * @param fieldName The name of the field to describe
	 * @param context The server state object
	 * @return A description of the requested column
	 */
	@GET
	@Produces("application/json")
	@Path("/{fileName}/{fieldName}")
	public DescribeData getDescribeData(@PathParam("fileName")final String fileName, @PathParam("fieldName")final String fieldName, 
			@Context ServletContext context, @Context HttpServletRequest req) {
		DescribeData data = getCachedConfig("describe-info-"+fieldName+"-", fileName);
		
		if (data == null) {
			MatrixData matrix;
			DatasetManager datasetManager = ConfigurationUtils.getDatasetManager();
			matrix = datasetManager.getMatrixData(fileName);
			DataColumn<?> dataColumn = matrix.getDataColumn(fieldName);
			
			if (dataColumn.getType() == Type.LABEL) {
				LabelDescriber labelDescriber = ConfigurationUtils.getLabelDescriber();
				data = labelDescriber.describe((DataColumn<String>) dataColumn);
			} else {
				NumericDescriber numericDescriber = ConfigurationUtils.getNumericDescriber();
				data = numericDescriber.describe((DataColumn<Double>) dataColumn);
			}
			
			data.setTitle(fieldName);
			
			cacheData(data, "describe-info-"+fieldName+"-", fileName);
		}
		
		return data;
	}
}
