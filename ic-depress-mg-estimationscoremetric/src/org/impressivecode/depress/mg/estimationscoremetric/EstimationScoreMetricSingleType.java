package org.impressivecode.depress.mg.estimationscoremetric;

public class EstimationScoreMetricSingleType {
	
	private Integer spent;
	private Integer estimated;	
	private Double singleMetricValue;
	
	public EstimationScoreMetricSingleType(Integer spent, Integer estimated, Double value) {
		super();
		this.spent = spent;
		this.estimated = estimated;
		this.singleMetricValue = value;
	}

	public int getSpent() {
		return spent;
	}
	
	public int getEstimated() {
		return estimated;
	}

	public Double getSingleMetricValue() {
		return singleMetricValue;
	}

}
