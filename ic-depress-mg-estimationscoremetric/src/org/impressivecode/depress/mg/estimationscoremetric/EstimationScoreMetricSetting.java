package org.impressivecode.depress.mg.estimationscoremetric;


public class EstimationScoreMetricSetting {

	public static enum StorySize {
		//SMALL(3, 1.0, 0.5, 1.0), MEDIUM(7, 1.5, 0.3, 0.6), BIG(100, 3.0, 0.2, 0.4);
		SMALL(3, 1.0, 0.5, 1.0), MEDIUM(7, 2.0, 0.3, 0.6), BIG(100, 4.0, 0.2, 0.4);
		
		private Integer sizeInDays;
		private Double weight;
		private Double difM;
		private Double difH;

		private StorySize(Integer days, Double weight, Double difM, Double difH) {
			this.sizeInDays = days;
			this.weight = weight;
			this.difM = difM;
			this.difH = difH;
		}

		public Double getWeight() {
			return weight;
		}

		public Integer getMaxSizeInDays() {
			return sizeInDays;
		}

		public Integer getMaxSizeInMinutes() {
			return sizeInDays * 8 * 60;
		}
		

		public Double getDifM() {
			return difM;
		}

		public Double getDifH() {
			return difH;
		}

		public static StorySize getStorySize(Integer estimatedTimeInSeconds) {
			if (estimatedTimeInSeconds <= SMALL.getMaxSizeInMinutes()) {
				return SMALL;
			} else if (estimatedTimeInSeconds <= MEDIUM.getMaxSizeInMinutes()) {
				return MEDIUM;
			} else {
				return BIG;
			}
		}

	}

	public static enum MissEstimateWeight {
		CORRECT(1.0), MODERATE(1.5), HIGH(3.0);

		private Double weight;

		private MissEstimateWeight(Double weight) {
			this.weight = weight;
		}

		public Double getWeight() {
			return weight;
		}

		public static MissEstimateWeight getMissEstimateWeight(StorySize storySize, Integer timeEstimate, Integer timeSpent) {
			Double dif =  Math.abs((double)timeSpent/(double)timeEstimate  - 1.0);
			Double difM = storySize.getDifM();
			Double difH = storySize.getDifH();
			if(dif < difM) {
				return CORRECT;
			} else if (dif < difH) {
				return MODERATE;
			} else {
				return HIGH;
			}
		}
	}

	public static enum EstimationResult {
		//OVER_ESTIMATE(0.5), CORRECT_ESTIMATE(1.0), UNDER_ESTIMATE(1.0);
		OVER_ESTIMATE(0.5), CORRECT_ESTIMATE(1.0), UNDER_ESTIMATE(2.0);

		private Double weight;

		private EstimationResult(Double weight) {
			this.weight = weight;
		}

		public Double getWeight() {
			return weight;
		}
		
		public static EstimationResult getEstimationResult(MissEstimateWeight meWeight, Integer timeEstimate, Integer timeSpent){
			if(MissEstimateWeight.CORRECT.equals(meWeight)){
				return CORRECT_ESTIMATE;
			} else if (timeSpent < timeEstimate) {
				return OVER_ESTIMATE;
			} else {
				return UNDER_ESTIMATE;
			}
		}

	}
}
