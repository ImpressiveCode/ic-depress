package org.impressivecode.depress.mg.estimationscoremetric;

import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.EstimationResult;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.MissEstimateWeight;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.StorySize;

public class EsmIssueType {

	private MissEstimateWeight missEstimateWeight;
	private StorySize storySize;
	private EstimationResult estimationResult;

	public void setSize(StorySize storySize) {
		this.storySize = storySize;
	}

	public void setMissEstimateWeight(MissEstimateWeight missEstimateWeight) {
		this.missEstimateWeight = missEstimateWeight;
	}

	public MissEstimateWeight getMissEstimateWeight() {
		return missEstimateWeight;
	}

	public void setEstimationResult(EstimationResult estimationResult) {
		this.estimationResult = estimationResult;
	}
	
	public EstimationResult getEstimationResult() {
		return estimationResult;
	}

	public StorySize getStorySize() {
		return this.storySize;
	}

}
