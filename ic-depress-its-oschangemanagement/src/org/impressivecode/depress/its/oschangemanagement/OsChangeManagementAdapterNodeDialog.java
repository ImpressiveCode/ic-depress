package org.impressivecode.depress.its.oschangemanagement;

import java.awt.Component;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.oschangemanagement.OsChangeManagementUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.JiraOnlineFilterListItem;
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

	protected DialogComponentStringSelection pluginComponent;
	private OsChangeManagementRestClient client;
	public OsChangeManagementAdapterNodeDialog(){
		super();
		RemoveAdvancedTab();
		AddPluginComponent();
	}
	
	protected void RemoveAdvancedTab(){
		removeTab(ADVANCED_TAB_NAME);
	}
	
	protected void AddPluginComponent(){
		JPanel connectionTab = (JPanel) getTab(ITSOnlineNodeDialog.CONNECTION_TAB_NAME);
		connectionTab.add(createPluginComponent());
	}
	
	protected Component createPluginComponent(){
		pluginComponent = new DialogComponentStringSelection(createPluginComponentSettings(), "Plugin: ", getPluginsName());
		return pluginComponent.getComponentPanel();
	}
	
	protected SettingsModelString createPluginComponentSettings(){
		return OsChangeManagementAdapterNodeModel.createPluginSettings();
	}
	
	protected String[] getPluginsName(){
		ArrayList<String> plugins = new ArrayList<String>();
		for(PluginEnum.Plugin plugin : PluginEnum.Plugin.values()){
			plugins.add(plugin.toString());
		}
		return plugins.toArray(new String[plugins.size()]);
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
        String pluginName = ((SettingsModelString) (pluginComponent.getModel())).getStringValue();
        builder.setHostname(urlString);
        builder.setMode(mode);
        OsChangeManagementRestClient client = new OsChangeManagementRestClient();
        URI t = builder.build();
        String rawData = client.getJSON(builder.build(), login, password);
        switch (pluginName){
        case "OSLCCM":
        	OsChangeManagementRationalAdapterParser parser = new OsChangeManagementRationalAdapterParser();
        	return (List<T>) parser.getProjectList(rawData);
        default:
        	return null;
        }
       
    }
}


