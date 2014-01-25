package org.impressivecode.depress.its.jiraonline;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public abstract class MapperAbstractCustomField {

    protected final Mode uriMode;
    protected List<JiraOnlineFilterListItem> fieldList;

    public MapperAbstractCustomField(List<JiraOnlineFilterListItem> fieldList) {
        this.fieldList = fieldList;
        this.uriMode = getURIMode();
    }

    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = newArrayList();
        for (JiraOnlineFilterListItem item : fieldList) {
            DialogComponentStringSelection comp = new DialogComponentStringSelection(new SettingsModelString(getMapperModelString(), ""), item.getName(), getImplementedMappings());
            comp.setToolTipText(item.getDescription());
            dialogComponents.add(comp);
        }
        return dialogComponents;
    }

    protected abstract String getMapperModelString();

    protected abstract Mode getURIMode();
    
    protected abstract Collection<String> getImplementedMappings();
    
    public String getName() {
        return uriMode.toString();
    }
}
