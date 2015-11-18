package com.infinity.glass.manager;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.rest.LabelDescriber;
import com.infinity.glass.rest.NumericDescriber;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.utils.CacheManager;
import com.infinity.glass.rest.utils.ComputationUtil;

/**
 * Pre-computes all comparisons
 * @author Jeffrey.Richley
 */
public class ThreadedDataPreProcessor implements DataPreProcessor {
	
	private final CacheManager cacheManager;

	@Inject
	public ThreadedDataPreProcessor(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void preProcessMatrixData(final MatrixData md, final String uuid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO: the system only caches the descriptions right now, need to do all of it and then precompute
				
				// need to create the gson a special way because NaN isn't supported normally
				Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
				List<String> headings = md.getHeadings();
				
				// compute the descriptions
				for (String heading : headings) {
					DescribeData data = null;
					DataColumn<?> dataColumn = md.getDataColumn(heading);
					
					if (dataColumn.getType() == Type.LABEL) {
						LabelDescriber labelDescriber = ConfigurationUtils.getLabelDescriber();
						data = labelDescriber.describe((DataColumn<String>) dataColumn);
					} else {
						NumericDescriber numericDescriber = ConfigurationUtils.getNumericDescriber();
						data = numericDescriber.describe((DataColumn<Double>) dataColumn);
					}

					if (data != null) {
						data.setTitle(heading);
	//					cacheData(data, "describe-info-"+heading+"-", heading);
						String json = gson.toJson(data);
						cacheManager.cache("describe-info-"+heading+"-"+uuid, json);
					}
				}
				
				// compute the comparisons
				for (int i = 0; i < headings.size() - 1; i++) {
					for (int j = 1; j < headings.size(); j++) {
						String firstColumnName = headings.get(i);
						String secondColumnName = headings.get(j);
						CompareData data = null;
						
						data = ComputationUtil.calculateData(firstColumnName, secondColumnName, uuid, md);
						
						Type firstColumn = md.getDataColumn(firstColumnName).getType();
						Type secondColumn = md.getDataColumn(secondColumnName).getType();
						String type = firstColumn + "-" + secondColumn;
						
						String id = uuid;
						String key = "compare-info-" + type + "-" +firstColumnName+"-"+secondColumnName+"-";
						try {
							String json = gson.toJson(data);
							cacheManager.cache(key.concat(id), json);
						} catch (IllegalArgumentException e) {
							System.out.println("Unable to cache: " + firstColumnName + "~" + secondColumnName);
							; // yes, we are swallowing anything that happens bad.  we need to cache other data if this one fails
						}
					}
				}
			}
		}).start();
	}

}
