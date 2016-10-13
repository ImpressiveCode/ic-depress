package org.impressivecode.depress.mg.usm;

import org.impressivecode.depress.its.common.ITSDataType;
import org.knime.core.data.DataRow;
import org.knime.core.data.collection.ListCell;
/*
 * Stworzyc interface do komunikacji UserStoryMetricProcessor z metoda computeMetric
 * dostaje to samo i zwraca Object
 * tu implementowac go
 */

/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */

public class UserStoryMetricProcessorImplement implements UserStoryMetricProcessor{

	@Override
	public Object computeMetric(final ITSDataType row) {
        return row.getComments().size();
	}
	
}
