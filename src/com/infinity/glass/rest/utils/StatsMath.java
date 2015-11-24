package com.infinity.glass.rest.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.commons.math3.stat.inference.TestUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.infinity.glass.rest.data.RegressionData;
import com.infinity.glass.rest.data.DataPoint;

public class StatsMath {

	public static double getCorrelation(List<Double> xList, List<Double> yList) {
		double answer = 0;

		double[] x = new double[xList.size()];
		double[] y = new double[yList.size()];

		for (int i = 0; i < xList.size(); i++) {
			if (xList.get(i) != null && yList.get(i) != null) {
				x[i] = xList.get(i);
				y[i] = yList.get(i);
			}
		}

		answer = new PearsonsCorrelation().correlation(x, y);

		return answer;
	}

	public static RegressionData getRegressionData(List<DataPoint> points) {
		final SimpleRegression simpleRegression = new SimpleRegression(true);
		for (final DataPoint point : points) {
			simpleRegression.addData(point.getX(), point.getY());
		}
		final RegressionData regressionData = new RegressionData();
		regressionData.setIntercept(simpleRegression.getIntercept());
		regressionData.setSlope(simpleRegression.getSlope());
		return regressionData;
	}
	
	public static double getCorrelation(List<DataPoint> points) {
		List<Double> xs = new ArrayList<Double>();
		List<Double> ys = new ArrayList<Double>();

		for (int i = 0; i < points.size(); i++) {
			DataPoint xy = points.get(i);
			xs.add(xy.getX());
			ys.add(xy.getY());
		}

		return getCorrelation(xs, ys);
	}

	public static double getAnovaF(List<double[]> means) {
		return new OneWayAnova().anovaPValue(means);
	}

	public static void main(String[] args) {
		double[] classA = { 93.0, 103.0, 95.0, 101.0, 91.0, 105.0, 96.0, 94.0, 101.0 };
		double[] classB = { 99.0, 92.0, 102.0, 100.0, 102.0, 89.0 };
		double[] classC = { 110.0, 115.0, 111.0, 117.0, 128.0, 117.0 };
		List<double[]> classes = new ArrayList<double[]>();
		classes.add(classA);
		classes.add(classB);
		classes.add(classC);

		double fStatistic = TestUtils.oneWayAnovaFValue(classes); // F-value
		double pValue = TestUtils.oneWayAnovaPValue(classes); // P-value
		
		System.out.println(fStatistic + " " + pValue);
	}
}
