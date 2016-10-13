package org.impressivecode.depress.mg.usm;

import org.impressivecode.depress.its.common.ITSDataType;

/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */

public interface UserStoryMetricProcessor {
	
	Object computeMetric(final ITSDataType row);

}
