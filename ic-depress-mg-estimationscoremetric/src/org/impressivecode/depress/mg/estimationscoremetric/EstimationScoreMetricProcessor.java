package org.impressivecode.depress.mg.estimationscoremetric;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;

import com.google.common.collect.Maps;

public class EstimationScoreMetricProcessor {
	private Map<String, ITSDataType> issues;
	private Map<String, Set<ITSDataType>> ipa;
	private Integer low;
	private Integer high;
	private static Double highOverEstimate;
	private static Double lowOverEstimate;
	private static Double lowUnderEstimate;
	private static Double highUnderEstimate;
	private static Double weightScoreForHighValues;

	public EstimationScoreMetricProcessor(List<ITSDataType> issues,
			List<IssuesMetricType> ipa, Integer correctEstimateMargin,
			Integer highOverUnderEstimateMargin,Double weightScore) {
		super();
		this.issues = convertIssues2Map(checkNotNull(issues,
				"Issues has to be set"));
		this.ipa = convertIPA2Map(checkNotNull(ipa, "IPA has to be set"));
		EstimationScoreMetricProcessor.highOverEstimate=1.0-((double)highOverUnderEstimateMargin)/100.0;
		EstimationScoreMetricProcessor.highUnderEstimate=1.0+((double)highOverUnderEstimateMargin)/100.0;
		EstimationScoreMetricProcessor.lowOverEstimate=1.0-((double)correctEstimateMargin)/100.0;
		EstimationScoreMetricProcessor.lowUnderEstimate=1.0+((double)correctEstimateMargin)/100.0;
		EstimationScoreMetricProcessor.weightScoreForHighValues=weightScore;
	}

	public List<EstimationScoreMetricType> transform() {
		List<EstimationScoreMetricType> metricResult = new ArrayList<EstimationScoreMetricType>();
		for (String className : ipa.keySet()) {
			EstimationScoreMetricType metric = new EstimationScoreMetricType();
			metric.setClassName(className);
			boolean ifExistsIssue = false;
			List<ITSDataType> wesmIssues = new ArrayList<ITSDataType>();
			for (ITSDataType itsType : ipa.get(className)) {
				ITSDataType issue = issues.get(itsType.getIssueId());
				wesmIssues.add(issue);
				if (issue.getTimeEstimate() != null
						&& issue.getTimeSpent() != null
						&& issue.getTimeEstimate() != 0
						&& issue.getTimeSpent() != 0) {
					Double value = ((double) issue.getTimeSpent() / (double) issue
							.getTimeEstimate());
					metric.addMetric(issue.getTimeSpent(),
							issue.getTimeEstimate(), value,
							checkInterval(value));
				}
					ifExistsIssue = true;
			}
			if (ifExistsIssue) {
				metric.evaluate();
				metric.setESM(new ESMProcessor().computeMetric(wesmIssues));
				metric.setWESM1(new WESM1Processor().computeMetric(wesmIssues));
				metric.setWESM2(new WESM2Processor().computeMetric(wesmIssues));
				metric.setWESM3(new WESM3Processor().computeMetric(wesmIssues));
				metricResult.add(metric);
			}
		}
		return metricResult;
	}

	public static Integer checkInterval(Double value) {

		if (value < highOverEstimate) {
			return 1;
		} else if (value < lowOverEstimate) {
			return 2;
		} else if (value < lowUnderEstimate) {
			return 3;
		} else if (value < highUnderEstimate) {
			return 4;
		} else
			return 5;

	}

	private static Map<String, ITSDataType> convertIssues2Map(
			final List<ITSDataType> listOfIssues) {
		Map<String, ITSDataType> issuesMap = Maps.newHashMap();
		for (ITSDataType its : listOfIssues) {
			issuesMap.put(its.getIssueId().trim(), its);
		}

		return issuesMap;
	}

	private static Map<String, Set<ITSDataType>> convertIPA2Map(
			final List<IssuesMetricType> listOfIpa) {
		Map<String, Set<ITSDataType>> ipaMap = Maps.newHashMap();
		for (IssuesMetricType imt : listOfIpa) {
			ipaMap.put(imt.getResourceName(), imt.getUniqueIssues());
		}
		return ipaMap;
	}

	public static Double getWeightScoreForHighValues() {
		return weightScoreForHighValues;
	}
	

}
