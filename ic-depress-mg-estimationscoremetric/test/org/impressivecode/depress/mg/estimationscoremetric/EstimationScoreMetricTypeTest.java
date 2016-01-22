package org.impressivecode.depress.mg.estimationscoremetric;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricProcessor;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricType;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;
import org.junit.Test;

public class EstimationScoreMetricTypeTest {
	EstimationScoreMetricType processMetricType;

	@Test
	public void calculateMetricSum() {
		List<ITSDataType> itsData =new ArrayList<>();
		List<IssuesMetricType> issuesMetric =new ArrayList<>();
		EstimationScoreMetricProcessor processor=new EstimationScoreMetricProcessor(itsData, issuesMetric, 15, 50, 1.5);
		processMetricType=new EstimationScoreMetricType();
		processMetricType.addMetric(8, 20, 8.0/20.0, EstimationScoreMetricProcessor.checkInterval(8.0/20.0));
		processMetricType.addMetric(19, 20, 19.0/20.0, EstimationScoreMetricProcessor.checkInterval(19.0/20.0));
		processMetricType.addMetric(15, 20, 15.0/20.0, EstimationScoreMetricProcessor.checkInterval(15.0/20.0));

		processMetricType.evaluate();
		Double estimationScore=round(processMetricType.getEstimationScore(),1);
		Double weightEstimationScore=round(processMetricType.getWeightEstimationScore(),2);
		assertEquals(0,estimationScore.compareTo(new Double(0.3)));
		assertEquals(0,weightEstimationScore.compareTo(new Double(0.29)));
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
