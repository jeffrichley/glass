package com.infinity.glass.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.MatrixData;

@Path("/describe")
public class DescribeProvider {

	@GET
	@Produces("application/json")
	@Path("/{fieldName}")
	public DescribeData getDescribeData(@PathParam("fieldName")final String fieldName, @Context ServletContext context) {
		MatrixData matrix = new DataProvider().getMatrixData(context);
		DataColumn<?> dataColumn = matrix.getDataColumn(fieldName);
		
		DescribeData data = null;
		if (dataColumn.getType() == Type.LABEL) {
			data = new LabelDescriber().describe((DataColumn<String>) dataColumn);
		} else {
			data = new NumericDescriber().describe((DataColumn<Double>) dataColumn);
		}
		
		data.setTitle(fieldName);
		
		return data;
	}
}
