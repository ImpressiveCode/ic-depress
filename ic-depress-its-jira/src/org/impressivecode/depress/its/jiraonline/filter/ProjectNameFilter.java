package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ProjectNameFilter extends Filter {

    public ProjectNameFilter() {

    }

    @Override
    SettingsModel[] getSettingModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    String getName() {
        return "Project name";
    }

    @Override
    String getJQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = new ArrayList<>();
        dialogComponents.add(new DialogComponentString(new SettingsModelString("todo2", "works"), "From"));
        return dialogComponents;
    }

}
