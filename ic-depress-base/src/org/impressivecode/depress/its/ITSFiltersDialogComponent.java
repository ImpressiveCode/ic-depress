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
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;

import pl.enofod.shuttlelist.ShuttleList;

/**
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * 
 */
public class ITSFiltersDialogComponent extends DialogComponent {

    private JPanel panel;
    private ShuttleList<ITSFilter> list;

    public ITSFiltersDialogComponent(SettingsModel model) {
        super(model);
        panel = new JPanel();
        list = new ShuttleList<>();
        panel.add(list);
    }

    public void addOnListItemClickListener(final OnListItemClickListener listener) {
        list.addOnListItemClickListener(new ShuttleList.OnListItemClickListener<ITSFilter>() {

            @Override
            public void listItemClicked(ITSFilter elementClicked, int count, int tableId) {
                listener.listItemClicked(elementClicked, count, tableId);
            }
        });
    }

    public void addFilters(Collection<ITSFilter> filters) {
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

    public interface OnListItemClickListener {
        void listItemClicked(ITSFilter elementClicked, int count, int tableId);
    }

}
