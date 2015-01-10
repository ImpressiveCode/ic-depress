package org.impressivecode.depress.its.oschangemanagement;

import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class OsChangeManagementAdapterNodeModel extends ITSOnlineNodeModel {

	private static final int NUMBER_OF_INPUT_PORTS = 0;
    private static final int NUMBER_OF_OUTPUT_PORTS = 2;
    
    private OsChangeManagementAdapterUriBuilder uriBuilder;
    private OsChangeManagementRestClient client;

	
	protected OsChangeManagementAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
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
