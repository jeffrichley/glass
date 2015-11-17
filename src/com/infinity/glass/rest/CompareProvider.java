package com.infinity.glass.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelLabelCompareData;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.StringDataColumn;

/**
 * This provider is the entry into comparing two metrics.  It will determine what types of columns are being compared
 * and give back the correct types of comparisons.
 * @author Jeffrey.Richley
 */
@Path("/compare")
public class CompareProvider extends GlassDataProvider<CompareData> {
	
//	private final CacheManager cacheManager = new HomeDirCacheManager();

	/**
	 * Create the comparison data for the two given columns.  This determines
	 * what types of columns are represented and what types of comparisons should
	 * be returned.
	 * @param firstColumnName The name of the first column to compare
	 * @param secondColumnName The name of the second column to compare
	 * @param context Information about the running environment
	 * @return Some version of the comparison data depending on what types of columns are requested
	 */
	@GET
	@Produces("application/json")
	@Path("/{fileName}/{firstColumnName}/{secondColumnName}/{uuid}")
	public CompareData getCompareData(@PathParam("fileName")final String fileName,
			@PathParam("firstColumnName")final String firstColumnName, 
			@PathParam("secondColumnName")final String secondColumnName, 
			@PathParam("uuid")final String uuid,
			@Context ServletContext context) {
		
//		String id = "compare-info-"+firstColumnName+"-"+secondColumnName+"-";
		
		CompareData data = null;
		
		if (data == null) {
			MatrixData matrix = null;
			matrix = ConfigurationUtils.getDatasetManager().getMatrixData(fileName);
			
			Type firstType = matrix.getDataColumn(firstColumnName).getType();
			Type secondType = matrix.getDataColumn(secondColumnName).getType();
			
			if (firstType == Type.LABEL && secondType == Type.LABEL) {
				StringDataColumn firstColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
				StringDataColumn secondColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
				LabelLabelCompareData tmpData = new LabelLabelComparer().compare(firstColumn, secondColumn, uuid);
				data = tmpData;
			} else if (firstType == Type.LABEL && secondType == Type.NUMERIC) {
				StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
				DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
				LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn, uuid);
				data = tmpData;
			} else if (firstType == Type.NUMERIC && secondType == Type.LABEL) {
				StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
				DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
				LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn, uuid);
				String tmpId = "compare-info-"+secondColumnName+"-"+firstColumnName+"-";
				data = tmpData;
			} else if (firstType == Type.NUMERIC && secondType == Type.NUMERIC) {
				DoubleDataColumn firstColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
				DoubleDataColumn secondColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
				NumericNumericCompareData tmpData = new NumericNumericComparer().compare(firstColumn, secondColumn, uuid);
				data = tmpData;
			} else {
				throw new IllegalStateException("Unable to compute metrics for " + firstColumnName + " and " + secondColumnName);
			}
		}
		
		return data;
	}
	
}
