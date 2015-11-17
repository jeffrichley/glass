package com.infinity.glass.manager;

import com.infinity.glass.rest.data.MatrixData;

public interface DataPreProcessor {

	void preProcessMatrixData(MatrixData md, String uuid);
	
}
