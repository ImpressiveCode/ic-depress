package org.impressivecode.depress.its.jiraonline.filter;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterRsClient;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.knime.core.node.defaultnodesettings.DialogComponent;

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
<<<<<<< HEAD
    
    protected abstract Mode getURIMode();
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilterValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilterModelId() {
        // TODO Auto-generated method stub
        return null;
    }
=======

    protected abstract Mode getURIMode();

>>>>>>> 22fd2f25c44209c83c6e5b6830b243d522037899
}
