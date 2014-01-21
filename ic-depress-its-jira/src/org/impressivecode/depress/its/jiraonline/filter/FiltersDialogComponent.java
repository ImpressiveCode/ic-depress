package org.impressivecode.depress.its.jiraonline.filter;

import java.util.Collection;

import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;

import pl.enofod.shuttlelist.ShuttleList;
import pl.enofod.shuttlelist.ShuttleList.OnListItemClickListener;

public class FiltersDialogComponent extends DialogComponent {

    private JPanel panel;
    private ShuttleList<Filter> list;

    public FiltersDialogComponent(SettingsModel model) {
        super(model);
        panel = new JPanel();
        list = new ShuttleList<>();
        panel.add(list);
    }

    public void addOnListItemClickListener(OnListItemClickListener<Filter> listener) {
        list.addOnListItemClickListener(listener);
    }

    public void addFilters(Collection<Filter> filters) {
        list.addElements(filters);
    }

    @Override
    public JPanel getComponentPanel() {
        return panel;
    }

    @Override
    protected void updateComponent() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void validateSettingsBeforeSave() throws InvalidSettingsException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setEnabledComponents(boolean enabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setToolTipText(String text) {
        // TODO Auto-generated method stub

    }

}
