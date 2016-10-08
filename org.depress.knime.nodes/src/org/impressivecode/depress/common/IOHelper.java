package org.impressivecode.depress.common;

import java.io.File;
import java.net.URL;


public class IOHelper {
	public static File getFile(String path) {
		try {
			URL url = new URL(path);
			return new File(url.toURI());
		} catch (Exception e) {
			return new File(path);
		} 
	}
}
