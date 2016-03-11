package org.impressivecode.depress.mg.estimationscoremetric;

import java.util.ArrayList;
import java.util.List;

public class EstimationScoreMetricType {

	private List<EstimationScoreMetricSingleType> metrics = new ArrayList<EstimationScoreMetricSingleType>();

	private String className;
	private Integer highOverEstimate = 0;
	private Integer lowOverEstimate = 0;
	private Integer correctEstimate = 0;
	private Integer lowUnderEstimate = 0;
	private Integer highUnderEstimate = 0;
	private Integer numberOfIssues=0;;
	private Double estimationScore = 0.0;
	private Double weightEstimationScore = 0.0;

	private Double wesm2;

	private Double wesm3;

	private Double wesm1;

	private Double ems;

	public EstimationScoreMetricType() {
		super();

	}

	public void addMetric(Integer spent, Integer estimated, Double value,
			Integer type) {
		metrics.add(new EstimationScoreMetricSingleType(spent, estimated, value));
		this.numberOfIssues++;
		switch (type) {
		case 1:
			addHighOverEstimate();
			break;
		case 2:
			addLowOverEstimate();
			break;
		case 3:
			addOk();
			break;
		case 4:
			addLowUnderEstimate();
			break;
		case 5:
			addHighUnderEstimate();
			break;

		default:
			break;
		}

	}
	public void addEmptyMetric(){
		this.numberOfIssues=0;
	}

	public void evaluate() {
		if(this.metrics!=null && this.metrics.size()>0){
		this.estimationScore = ((double) correctEstimate)
				/ ((double) (this.metrics.size()));
		Double weightScoreDenominator = 0.0;
		for (EstimationScoreMetricSingleType singleMetric : this.metrics) {
			int interval = EstimationScoreMetricProcessor.checkInterval(singleMetric
					.getSingleMetricValue());
			if (interval == 1 || interval == 5) {
				weightScoreDenominator += EstimationScoreMetricProcessor.getWeightScoreForHighValues();
			} else
				weightScoreDenominator += 1;
		}
		this.weightEstimationScore = ((double) correctEstimate)
				/ (weightScoreDenominator);
		}

	}

	private void addHighUnderEstimate() {
		this.highUnderEstimate += 1;
	}

	private void addLowUnderEstimate() {
		this.lowUnderEstimate += 1;
	}

	private void addOk() {
		this.correctEstimate += 1;
	}

	private void addHighOverEstimate() {
		this.highOverEstimate += 1;
	}

	private void addLowOverEstimate() {
		this.lowOverEstimate += 1;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<EstimationScoreMetricSingleType> getMetrics() {
		return metrics;
	}

	public Integer getHighOverEstimate() {
		return highOverEstimate;
	}

	public Integer getLowOverEstimate() {
		return lowOverEstimate;
	}

	public Integer getCorrectEstimate() {
		return correctEstimate;
	}

	public Integer getLowUnderEstimate() {
		return lowUnderEstimate;
	}

	public Integer getHighUnderEstimate() {
		return highUnderEstimate;
	}

	public Double getEstimationScore() {
		return estimationScore;
	}

	public Double getWeightEstimationScore() {
		return weightEstimationScore;
	}

	public Integer getNumberOfIssues() {
		return numberOfIssues;
	}

	public void setESM(Double esm) {
		this.ems = esm;
	}
	
	public Double getEsm() {
		return ems;
	}
	
	public void setWESM1(Double wesm1) {
		this.wesm1 = wesm1;
	}
	
	public Double getWesm1() {
		return wesm1;
	}
	
	
	
	public void setWESM2(Double wesm2) {
		this.wesm2 = wesm2;
	}
	
	public Double getWesm2() {
		return wesm2;
	}
	
	public void setWESM3(Double wesm3) {
		this.wesm3 = wesm3;
	}
	
	public Double getWesm3() {
		return wesm3;
	}

}
