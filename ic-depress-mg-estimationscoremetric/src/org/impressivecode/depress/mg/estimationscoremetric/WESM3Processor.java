package org.impressivecode.depress.mg.estimationscoremetric;

import static org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.StorySize.getStorySize;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.EstimationResult;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricSetting.MissEstimateWeight;

public class WESM3Processor {

	public Double computeMetric(final List<ITSDataType> issues){
		List<ITSDataType> checkedIssues = getIssuesWithTimeValues(issues);
		if(checkedIssues.size() > 0){
			return computeMetricInternal(checkedIssues);
		} else {
			return null;
		}
	}

	private Double computeMetricInternal(List<ITSDataType> issues) {
		List<EsmIssueType> esms = new ArrayList<>();
		for (ITSDataType issue : issues) {
			EsmIssueType esm = computeEsmInput(issue);
			esms.add(esm);
		}
		
		return doComputeWESM(esms);
	}

	private Double doComputeWESM(List<EsmIssueType> esms) {
		Double correctWeightSum = computeCorrectSum(esms);
		Double allWeightSum = computeAllWeightSum(esms);
		
		return correctWeightSum/allWeightSum;
	}

	private Double computeAllWeightSum(List<EsmIssueType> esms) {
		Double weightSum = 0.0;
		for(EsmIssueType esm : esms){
			weightSum += esm.getMissEstimateWeight().getWeight() * esm.getEstimationResult().getWeight()*esm.getStorySize().getWeight();
		}
		return weightSum;
	}

	private Double computeCorrectSum(List<EsmIssueType> esms) {
		Double weightSum = 0.0;
		for(EsmIssueType esm : esms){
			if(esm.getEstimationResult().equals(EstimationResult.CORRECT_ESTIMATE) || esm.getEstimationResult().equals(EstimationResult.OVER_ESTIMATE))
			weightSum += esm.getMissEstimateWeight().getWeight() * esm.getEstimationResult().getWeight()*esm.getStorySize().getWeight();
		}
		return weightSum;
	}

	private EsmIssueType computeEsmInput(ITSDataType issue) {
		EsmIssueType esm = new EsmIssueType();
		esm.setSize(getStorySize(issue.getTimeEstimate()));
		esm.setMissEstimateWeight(MissEstimateWeight.getMissEstimateWeight(esm.getStorySize(),
				issue.getTimeEstimate(), issue.getTimeSpent()));
		esm.setEstimationResult(EstimationResult.getEstimationResult(esm.getMissEstimateWeight(),issue.getTimeEstimate(), issue.getTimeSpent()));
		return esm;
	}

	private List<ITSDataType> getIssuesWithTimeValues(List<ITSDataType> issues) {
		List<ITSDataType> issuesToUse = new ArrayList<>();
		for(ITSDataType issue : issues) {
			if(hasTimeValues(issue)){
				issuesToUse.add(issue);
			}
		}
		return issuesToUse;
	}

	private boolean hasTimeValues(ITSDataType issue) {
		return issue.getTimeEstimate() != null
				&& issue.getTimeSpent() != null
				&& issue.getTimeEstimate() != 0
				&& issue.getTimeSpent() != 0;
	}
}
