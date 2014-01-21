package org.impressivecode.depress.its.jiraonline.filter;

import java.util.List;

import org.impressivecode.depress.its.ITSNodeDialog;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;

public abstract class Filter {

    private List<DialogComponent> dialogComponents;

    Filter() {
        dialogComponents = createDialogComponents();
    }

    abstract List<DialogComponent> createDialogComponents();

    abstract SettingsModel[] getSettingModels();

    abstract String getName();

    abstract String getJQL();

    @Override
    public String toString() {
        return getName();
    }

    public void addComponents(ITSNodeDialog nodeDialog) {
        for (DialogComponent component : dialogComponents) {
            nodeDialog.addDialogComponent(component);
        }
    }

    public void removeComponents(ITSNodeDialog nodeDialog) {
        for (DialogComponent component : dialogComponents) {
            nodeDialog.removeDialogComponent(component);
        }
    }
}
