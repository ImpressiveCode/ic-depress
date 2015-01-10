package org.impressivecode.depress.its.oschangemanagement;

import java.awt.Component;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.oschangemanagement.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.JiraOnlineFilterListItem;
import org.impressivecode.depress.its.oschangemanagement.model.JiraOnlineProjectListItem;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectList;
import org.impressivecode.depress.its.oschangemanagement.parser.OsChangeManagementRationalAdapterParser;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

public class OsChangeManagementAdapterNodeDialog extends ITSOnlineNodeDialog {

	private OsChangeManagementRestClient client;
	public OsChangeManagementAdapterNodeDialog(){
		super();
		removeTab(ADVANCED_TAB_NAME);
	}
	
	protected Component createPluginComponent(){
		DialogComponentStringSelection pluginComponent = new DialogComponentStringSelection(createPluginComponentSettings(), "", new String[2]);
		return null;
	}
	
	protected SettingsModelString createPluginComponentSettings(){
		return OsChangeManagementAdapterNodeModel.createPluginSettings();
	}
	@Override
	protected void updateProjectsList() {
		List<OsChangeManagementProject> projects;
		List<String> projectNames = new ArrayList<String>();
		try {
			projects = getList(Mode.PROJECT_LIST, OsChangeManagementRationalAdapterProjectList.class);
			projectSelection.getModel().setEnabled(true);
			for (OsChangeManagementProject item : projects) {
				projectNames.add(item.getName());
			}
			projectSelection.replaceListItems(projectNames, null);
		} catch (Exception e) {
			Logger.getLogger("Error").severe("Error during connection, list could not be downloaded");
		}
	}

	@Override
	protected void createMappingManager() {
		//Ta metoda w klasie dla OS ma byæ
        mappingManager = OsChangeManagementAdapterNodeModel.createMapping();
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
    	OsChangeManagementJiraRationalAdapterUriBuilder builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
        String urlString = ((SettingsModelString) (url.getModel())).getStringValue();
        String login = ((SettingsModelString) (loginComponent.getModel())).getStringValue();
        String password = ((SettingsModelString) (passwordComponent.getModel())).getStringValue();
        String pluginName = "TestPlugin";
        builder.setHostname(urlString);
       // builder.setMode(mode);
        OsChangeManagementRestClient client = new OsChangeManagementRestClient();
        URI t = builder.build();
        String rawData = client.getJSON(builder.build(), login, password);
        switch (pluginName){
        case "TestPlugin":
        	OsChangeManagementRationalAdapterParser parser = new OsChangeManagementRationalAdapterParser();
        	parser.getProjectList(rawData);
        default:
        	return null;
        }
       
    }
}


