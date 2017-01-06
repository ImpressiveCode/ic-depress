package org.impressivecode.depress.mg.usm;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.def.IntCell;
/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */

public class UserStoryMetricAdapterTableFactory {
	public static final String COMMENTS_COUNT = "Comments Count";
	public static final String COMMENTS_LINKS = "Comments Links";
	public static final String COMMENTS_CODE = "Comments Code listings";
	public static final String ATTACHMENTS_COUNT = "Attachments Count";
	
	 public static final DataColumnSpec[] COL_SPEC = {
			 new DataColumnSpecCreator(COMMENTS_COUNT, IntCell.TYPE).createSpec(),
			 new DataColumnSpecCreator(COMMENTS_LINKS, IntCell.TYPE).createSpec(),
			 new DataColumnSpecCreator(COMMENTS_CODE, IntCell.TYPE).createSpec(),
			 new DataColumnSpecCreator(ATTACHMENTS_COUNT, IntCell.TYPE).createSpec()
	 };
}
