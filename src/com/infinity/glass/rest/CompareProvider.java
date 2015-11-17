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
		
		// TODO: we don't have a good way to know what type of CompareData is to be created
		// may be stored firstName-secondName or secondName-firstName
//		CompareData data = getCachedConfig("compare-info-"+firstColumnName+"-"+secondColumnName+"-", fileName);
//		if (data == null) {
//			data = getCachedConfig("compare-info-"+secondColumnName+"-"+firstColumnName+"-", fileName);
//		}
		
		CompareData data = null;
		
		if (data == null) {
			MatrixData matrix = ConfigurationUtils.getDatasetManager().getMatrixData(fileName);
			data = ComputationUtil.calculateData(firstColumnName, secondColumnName, uuid, matrix);
			
//			cacheData(data, "compare-info-"+firstColumnName+"-"+secondColumnName+"-", fileName);
		}
		
		return data;
	}

	
}
