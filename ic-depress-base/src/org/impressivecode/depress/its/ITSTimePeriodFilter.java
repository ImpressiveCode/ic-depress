package org.impressivecode.depress.its;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;

public class ITSTimePeriodFilter extends ITSFilter {

    @Override
    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = new ArrayList<>();
        dialogComponents.add(new DialogComponentDate(new SettingsModelDate("todo"), "From"));
        dialogComponents.add(new DialogComponentDate(new SettingsModelDate("todo2"), "To"));
        return dialogComponents;
    }

    @Override
    public SettingsModel[] getSettingModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getJQL() {
        // TODO Auto-generated method stub
        return null;
    }

}
