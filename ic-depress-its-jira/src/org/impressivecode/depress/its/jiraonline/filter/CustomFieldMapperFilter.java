package org.impressivecode.depress.its.jiraonline.filter;

import java.util.List;
import static com.google.common.collect.Lists.newArrayList;
import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterRsClient;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModel;

public abstract class CustomFieldMapperFilter extends ITSFilter {

    private static final String JIRA_URL_LABEL = "Jira URL:";
    protected JiraOnlineAdapterUriBuilder uriBuilder;
    protected JiraOnlineAdapterRsClient client;
    
    public CustomFieldMapperFilter() {
        init();
    }
    
    private void init() {
        client = new JiraOnlineAdapterRsClient();
        uriBuilder = new JiraOnlineAdapterUriBuilder();
        uriBuilder.setMode(getURIMode());
    }

    @Override
    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = newArrayList();
        //dialogComponents.add(new DialogComponentString(JiraOnlineAdapterNodeModel.createSettingsURL(), JIRA_URL_LABEL, true, 32));
        return dialogComponents;
    }

    @Override
    public SettingsModel[] getSettingModels() {
        // TODO Auto-generated method stub
        return null;
    }
    
    protected abstract Mode getURIMode();


    @Override
    public String getJQL() {
        return "";
    }

}
