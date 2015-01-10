package org.impressivecode.depress.its.oschangemanagement;

import org.impressivecode.depress.its.ITSMappingManager;
import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class OsChangeManagementAdapterNodeModel extends ITSOnlineNodeModel {

	private static final int NUMBER_OF_INPUT_PORTS = 0;
    private static final int NUMBER_OF_OUTPUT_PORTS = 1;
    private static final String CFG_ITS_PLUGIN = "plugin";
    
    private OsChangeManagementAdapterUriBuilder uriBuilder;
    private OsChangeManagementRestClient client;

	
	protected OsChangeManagementAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}
	
	public static ITSMappingManager createMapping() {
        return new ITSMappingManager();
    }

	public static SettingsModelString createPluginSettings() {
        return new SettingsModelString(CFG_ITS_PLUGIN, DEFAULT_STRING_VALUE);
    }
	
	@Override
	protected void saveSpecificSettingsTo(NodeSettingsWO settings) {
	}

	@Override
	protected void validateSpecificSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	@Override
	protected void loadSpecificSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

}
