package org.impressivecode.depress.its.jiraonline.filter;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public abstract class CustomFieldMapperFilter extends ITSFilter {

    protected final Mode uriMode;
    protected List<JiraOnlineFilterListItem> fieldList;

    public CustomFieldMapperFilter(List<JiraOnlineFilterListItem> fieldList) {
        this.fieldList = fieldList;
        this.uriMode = getURIMode();
    }

    @Override
    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = newArrayList();
        for (JiraOnlineFilterListItem item : fieldList) {
            DialogComponentStringSelection comp = new DialogComponentStringSelection(new SettingsModelString(getFilterModelId(), ""), item.getName(), getImplementedMappings());
            comp.setToolTipText(item.getDescription());
            dialogComponents.add(comp);
        }
        return dialogComponents;
    }

    protected abstract Mode getURIMode();
    
    protected abstract Collection<String> getImplementedMappings();

    @Override
    public String getName() {
        return uriMode.toString();
    }
}
