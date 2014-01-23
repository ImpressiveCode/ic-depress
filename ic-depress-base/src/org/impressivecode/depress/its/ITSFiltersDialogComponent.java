/*
ImpressiveCode Depress Framework
Copyright (C) 2013  ImpressiveCode contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.its;

import java.util.Collection;

import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

import pl.enofod.shuttlelist.ShuttleList;
import pl.enofod.shuttlelist.ShuttleList.ListItemSelectionListener;

/**
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * 
 */
public class ITSFiltersDialogComponent extends DialogComponent {

    private JPanel panel;
    private ShuttleList<ITSFilter> list;

    public ITSFiltersDialogComponent(Collection<ITSFilter> filters, SettingsModelStringArray model) {
        super(model);
        panel = new JPanel();
        list = new ShuttleList<>();

        for (ITSFilter filter : filters) {
            boolean added = false;
            for (String id : model.getStringArrayValue()) {
                if (filter.getFilterModelId().equals(id)) {
                    // filter selected
                    list.addElementToRight(filter);
                    added = true;
                    break;
                }
            }
            if(!added) {
                list.addElementToLeft(filter);
            }
        }

        panel.add(list);
    }

    public void addListItemSelectionListener(final ListItemSelectedListener listener) {
        list.addListItemSelectionListener(new ListItemSelectionListener<ITSFilter>() {

            @Override
            public void listItemSelected(ITSFilter elementSelected, int tableId) {
                listener.listItemSelected(elementSelected, tableId);
            }
        });
    }

    @Override
    public JPanel getComponentPanel() {
        return panel;
    }

    @Override
    protected void updateComponent() {
        // NOOP
    }

    @Override
    protected void validateSettingsBeforeSave() throws InvalidSettingsException {
        // NOOP
    }

    @Override
    protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {
        // NOOP
    }

    @Override
    protected void setEnabledComponents(boolean enabled) {
        // NOOP
    }

    @Override
    public void setToolTipText(String text) {
        // NOOP
    }

    public interface ListItemSelectedListener {
        void listItemSelected(ITSFilter elementClicked, int tableId);
    }

}
