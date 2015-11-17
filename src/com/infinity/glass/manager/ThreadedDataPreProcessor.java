package com.infinity.glass.manager;

import com.infinity.glass.rest.data.MatrixData;

/**
 * Pre-computes all comparisons
 * @author Jeffrey.Richley
 */
public class ThreadedDataPreProcessor implements DataPreProcessor {

	@Override
	public void preProcessMatrixData(final MatrixData md, final String uuid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO: the system only caches the descriptions right now, need to do all of it and then precompute
				
				// compute the descriptions
				
				// compute the comparisons
//				List<String> headings = md.getHeadings();
//				for (int i = 0; i < headings.size() - 1; i++) {
//					String firstColumnName = headings.get(i);
//					String secondColumnName = headings.get(i+1);
//					ComputationUtil.calculateData(firstColumnName, secondColumnName, uuid, md);
//				}
			}
		}).start();
	}

}
