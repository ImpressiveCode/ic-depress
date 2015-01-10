package org.impressivecode.depress.its.oschangemanagement;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.oschangemanagement.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.JiraOnlineFilterListItem;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

public class OsChangeManagementAdapterNodeDialog extends ITSOnlineNodeDialog {

	private OsChangeManagementRestClient client;
	
	public OsChangeManagementAdapterNodeDialog(){
		super();
		removeTab(ADVANCED_TAB_NAME);
	}
	
	
	@Override
	protected void updateProjectsList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createMappingManager() {
		//Ta metoda w klasie dla OS ma byæ
        mappingManager = JiraOnlineAdapterNodeModel.createMapping();
		mappingManager.createFilterPriority(new RefreshCaller(Mode.PRIORITY_LIST));
        mappingManager.createFilterType(new RefreshCaller(Mode.TYPE_LIST));
        mappingManager.createFilterResolution(new RefreshCaller(Mode.RESOLUTION_LIST));
        mappingManager.createFilterStatus(new RefreshCaller(Mode.STATE_LIST));
	}

	@Override
	protected Component createAdvancedTab() {
		return new JPanel();		
	}

	@Override
	protected void loadSpecificSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveSpecificSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
		
	}
	
	private class RefreshCaller implements Callable<List<String>> {
        private final Mode mode;

        RefreshCaller(final Mode mode) {
            this.mode = mode;
        }

        //Trzeba zmieniæ klase modelow¹ dla JSON'a
        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            List<JiraOnlineFilterListItem> items = getList(mode, JiraOnlineFilterListItem.class);
            for (JiraOnlineFilterListItem item : items) {
                list.add(item.getName());
            }
            return list;
        }
    }

    private <T> List<T> getList(Mode mode, Class<?> elem) throws Exception {
		return null;
    }
}


