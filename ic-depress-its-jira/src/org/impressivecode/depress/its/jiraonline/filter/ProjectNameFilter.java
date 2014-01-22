package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSFilter;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ProjectNameFilter extends ITSFilter {

    @Override
    public String getName() {
        return "Project name";
    }

    @Override
    public String getFilterValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = new ArrayList<>();
        dialogComponents.add(new DialogComponentString(new SettingsModelString("todo2", "works"), "From"));
        return dialogComponents;
    }

    @Override
    public String getFilterModelId() {
        // TODO Auto-generated method stub
        return null;
    }

}
