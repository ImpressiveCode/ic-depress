package org.impressivecode.depress.mg.usm;

import org.impressivecode.depress.its.common.ITSDataType;

public class NumberOfAttachmentsMetricProcessor implements UserStoryMetricProcessor {
	
	@Override
	public Object computeMetric(final ITSDataType row) {
		return row.getAttachments().size();
	}

}
