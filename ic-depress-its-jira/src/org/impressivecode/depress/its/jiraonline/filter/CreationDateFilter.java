package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;

public class CreationDateFilter extends Filter {

    @Override
    List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = new ArrayList<>();
        dialogComponents.add(new DialogComponentDate(new SettingsModelDate("todo"), "From"));
        return dialogComponents;
    }

    @Override
    SettingsModel[] getSettingModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    String getName() {
        return "Creation date";
    }

    @Override
    String getJQL() {
        // TODO Auto-generated method stub
        return null;
    }
}
