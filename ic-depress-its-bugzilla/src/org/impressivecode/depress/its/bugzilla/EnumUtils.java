package org.impressivecode.depress.its.bugzilla;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  @author Piotr Wróblewski, Wroc³aw University of Technology
 * 
 */

public class EnumUtils {

	@SuppressWarnings("rawtypes")
	public static List<String> getAsStringCollection(Enum[] values) {
		List<String> names = new ArrayList<String>(6);
		for (Enum value : values) {
			names.add(value.name());
		}
		return names;
	}
}
