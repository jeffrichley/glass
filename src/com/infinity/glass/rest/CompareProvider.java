package com.infinity.glass.rest;

import java.lang.reflect.ParameterizedType;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.google.gson.Gson;
import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelLabelCompareData;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.StringDataColumn;
import com.infinity.glass.rest.utils.ComputationUtil;

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
		
		// TODO: this is really ugly, need to refactor
		// try all the combinations of NUMERIC, LABEL and first,second
		CompareData data = getCachedConfig("compare-info-LABEL-LABEL-"+firstColumnName+"-"+secondColumnName+"-", fileName, LabelLabelCompareData.class);
		if (data == null) {
			data = getCachedConfig("compare-info-NUMERIC-NUMERIC-"+firstColumnName+"-"+secondColumnName+"-", fileName, NumericNumericCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("compare-info-NUMERIC-LABEL-"+firstColumnName+"-"+secondColumnName+"-", fileName, LabelNumericCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("compare-info-LABEL-NUMERIC-"+firstColumnName+"-"+secondColumnName+"-", fileName, LabelNumericCompareData.class);
		}
		
		if (data == null) {
			data = getCachedConfig("compare-info-LABEL-LABEL-"+secondColumnName+"-"+firstColumnName+"-", fileName, LabelLabelCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("compare-info-NUMERIC-NUMERIC-"+secondColumnName+"-"+firstColumnName+"-", fileName, NumericNumericCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("compare-info-NUMERIC-LABEL-"+secondColumnName+"-"+firstColumnName+"-", fileName, LabelNumericCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("compare-info-LABEL-NUMERIC-"+secondColumnName+"-"+firstColumnName+"-", fileName, LabelNumericCompareData.class);
		}
		
		
		// ok ok we really don't have it cached
		if (data == null) {
			MatrixData matrix = ConfigurationUtils.getDatasetManager().getMatrixData(fileName);
			data = ComputationUtil.calculateData(firstColumnName, secondColumnName, uuid, matrix);
			
			Type firstColumn = matrix.getDataColumn(firstColumnName).getType();
			Type secondColumn = matrix.getDataColumn(secondColumnName).getType();
			String type = firstColumn + "-" + secondColumn;
			
			cacheData(data, "compare-info-" + type + "-" +firstColumnName+"-"+secondColumnName+"-", fileName);
		} else {
			// if we got one that was cached, we need to set the uuid to make the browser think it was from this request
			data.setRequestUUID(uuid);
		}
		
		return data;
	}

}
