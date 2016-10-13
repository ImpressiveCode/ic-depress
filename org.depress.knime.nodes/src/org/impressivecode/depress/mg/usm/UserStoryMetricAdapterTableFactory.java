package org.impressivecode.depress.mg.usm;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.StringCell;;

/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */

public class UserStoryMetricAdapterTableFactory {
	public static final String COMMENTS_COUNT = "Comments Count";
	
	public static final DataColumnSpec COMMENTS_COUNTS_COLSPEC = new DataColumnSpecCreator(COMMENTS_COUNT,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();
}
