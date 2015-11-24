package com.infinity.glass.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.MatrixData;

/**
 * A <code>DataProvider</code> that specializes in describing one particular
 * column and all the information about it.
 * @author Jeffrey.Richley
 */
@Stateless
@Path("/describe")
public class DescribeProvider extends GlassDataProvider<DescribeData> {

	/**
	 * Determines what type of column the request is for and returns the correct
	 * type of description for it
	 * @param fieldName The name of the field to describe
	 * @param context The server state object
	 * @return A description of the requested column
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces("application/json")
	@Path("/{dataId}/{fieldName}")
	public DescribeData getDescribeData(@PathParam("dataId") String dataId, @PathParam("fieldName") String fieldName) {
		DescribeData data = getCachedConfig("describe-info-" + dataId + "-" + fieldName + "-");
		
		if (data == null) {
			MatrixData matrix = new DataProvider().getMatrixData(getDataFile(dataId));
			DataColumn<?> dataColumn = matrix.getDataColumn(fieldName);
			
			if (dataColumn.getType() == Type.LABEL) {
				LabelDescriber labelDescriber = ConfigurationUtils.getLabelDescriber();
				data = labelDescriber.describe((DataColumn<String>) dataColumn);
			} else {
				NumericDescriber numericDescriber = ConfigurationUtils.getNumericDescriber();
				data = numericDescriber.describe((DataColumn<Double>) dataColumn);
			}
			
			/*
			 * SEAN - THERES A DEPENDENCY ON THE CLIENT RIGHT NOW THAT SAYS THIS TITLE HAS TO BE EQUAL TO THE FIELD NAME.
			 * 
			 * MAKE SURE THAT THIS DOESN'T CHANGE UNTIL WE CLEAN THAT PIECE UP.  
			 * 
			 * I BELIEVE THERE'S A BETTER WAY TO DO THE CLIENT PIECE SO THAT IT DOESN'T DEPEND ON ANY UUID OR ANYTHING THAT
			 * FORCES US TO BIND TO A DOM ELEMENT IN THE JAVASCRIPT.  SHOULD BE ABLE TO RETURN THESE ARBITRARILY AND NOT HAVE
			 * TO WORRY ABOUT IT.
			 */
			data.setTitle(fieldName);
			
			cacheData(data, "describe-info-"+fieldName+"-");
		}
		
		return data;
	}
}
