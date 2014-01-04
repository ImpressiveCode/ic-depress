package org.impressivecode.depress.its.bugzilla;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  @author Piotr Wróblewski, Wroc³aw University of Technology
 * 
 */

public class EnumUtils {

	//Enum as string collections is needed to create filter in filter panel. 
	@SuppressWarnings("rawtypes")
	public static List<String> getAsStringCollection(Enum[] values) {
		List<String> names = new ArrayList<String>(6);// 6 cause we do not have more than 6 elements in our enums
		for (Enum value : values) {
			names.add(value.name());
		}
		return names;
	}
}
