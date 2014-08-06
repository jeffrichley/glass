package com.infinity.glass.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.google.gson.Gson;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelLabelCompareData;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.StringDataColumn;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.HomeDirCacheManager;

@Path("/compare")
public class CompareProvider extends GlassDataProvider<CompareData> {
	
	private final CacheManager cacheManager = new HomeDirCacheManager();

	@GET
	@Produces("application/json")
	@Path("/{firstColumnName}/{secondColumnName}")
	public CompareData getCompareData(@PathParam("firstColumnName")final String firstColumnName, 
										@PathParam("secondColumnName")final String secondColumnName, 
										@Context ServletContext context) {
		
		String id = "compare-info-"+firstColumnName+"-"+secondColumnName+"-";
		
		CompareData data = getCachedData(firstColumnName, secondColumnName);
		if (data == null) {
			data = getCachedData(secondColumnName, firstColumnName);
		}
		
		if (data == null) {
			MatrixData matrix = new DataProvider().getMatrixData(context);
			
			Type firstType = matrix.getDataColumn(firstColumnName).getType();
			Type secondType = matrix.getDataColumn(secondColumnName).getType();
			
			if (firstType == Type.LABEL && secondType == Type.LABEL) {
				StringDataColumn firstColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
				StringDataColumn secondColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
				LabelLabelCompareData tmpData = new LabelLabelComparer().compare(firstColumn, secondColumn);
				cacheManager.cache("LABEL_LABEL-"+getCacheId(id), new Gson().toJson(tmpData));
				data = tmpData;
			} else if (firstType == Type.LABEL && secondType == Type.NUMERIC) {
				StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
				DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
				LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn);
				cacheManager.cache("LABEL_NUMERIC-"+getCacheId(id), new Gson().toJson(tmpData));
				data = tmpData;
			} else if (firstType == Type.NUMERIC && secondType == Type.LABEL) {
				StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
				DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
				LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn);
				String tmpId = "compare-info-"+secondColumnName+"-"+firstColumnName+"-";
				cacheManager.cache("LABEL_NUMERIC-"+getCacheId(tmpId), new Gson().toJson(tmpData));
				data = tmpData;
			} else if (firstType == Type.NUMERIC && secondType == Type.NUMERIC) {
				DoubleDataColumn firstColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
				DoubleDataColumn secondColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
				NumericNumericCompareData tmpData = new NumericNumericComparer().compare(firstColumn, secondColumn);
				cacheManager.cache("NUMERIC_NUMERIC-"+getCacheId(id), new Gson().toJson(tmpData));
				data = tmpData;
			} else {
				throw new IllegalStateException("Unable to compute metrics for " + firstColumnName + " and " + secondColumnName);
			}
		}
		
		return data;
	}

	private CompareData getCachedData(String firstColumnName, String secondColumnName) {
		String id = "compare-info-"+firstColumnName+"-"+secondColumnName+"-";
		CompareData data = getCachedConfig("LABEL_LABEL-"+id, LabelLabelCompareData.class);
		if (data == null) {
			data = getCachedConfig("LABEL_NUMERIC-"+id, LabelNumericCompareData.class);
		}
		if (data == null) {
			data = getCachedConfig("NUMERIC_NUMERIC-"+id, NumericNumericCompareData.class);
		}
		return data;
	}
	
	protected CompareData getCachedConfig(String id, Class<? extends CompareData> clazz) {
		String cacheId = getCacheId(id);
		String cachedData = cacheManager.getData(cacheId);
		
		CompareData answer = null;
		if (cachedData != null) {
			answer = new Gson().fromJson(cachedData, clazz);
		}
		
		return answer;
	}
	
}
