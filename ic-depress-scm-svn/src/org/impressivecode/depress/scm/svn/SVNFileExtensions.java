package org.impressivecode.depress.scm.svn;

/**
 * @author Krystian Dabrowski, Capgemini Poland
 */

public enum SVNFileExtensions {
	JAVA("CFG_JAVA_EXTENSION", true, ".java");
	//XML("CFG_XML_EXTENSION", true, ".xml");
	
	private final String configName;
	private final boolean defaultSelected;
	private final String extension;
	
	SVNFileExtensions(String configName, boolean defaultSelected, String extension) {
		this.configName = configName;
		this.defaultSelected = defaultSelected;
		this.extension = extension;
	}
	
	public String getConfigName() {
		return configName;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

	public String getExtension() {
		return extension;
	}
	
	public static SVNFileExtensions getSVNFileExtensionsFromConfigName(String configName) {
        for (SVNFileExtensions supportedExtension : SVNFileExtensions.values()) {
            if (supportedExtension.getConfigName().equals(configName)) {
                return supportedExtension;
            }
        }
        return null;
    }
}
