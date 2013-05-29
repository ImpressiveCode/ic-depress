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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */
package org.impressivecode.depress.data.anonymisation.objects;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.impressivecode.depress.data.anonymisation.AnonymisationNodeModel;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class AnonymisationFileChooser extends DialogComponentFileChooser {

    String SelectedPath;

    public AnonymisationFileChooser(SettingsModelString stringModel, String historyID, String... validExtensions) {
        super(stringModel, historyID, validExtensions);
        
        SelectedPath = stringModel.getStringValue();
        ((SettingsModelString) getModel()).setStringValue(SelectedPath);

        super.updateComponent();

        getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                SelectedPath = ((SettingsModelString) e.getSource()).getStringValue();
                UpdateComponent();
            }
        });
        getComponentPanel().revalidate();
    }

    public void UpdateComponent() {
        super.updateComponent();
        AnonymisationNodeModel.keyPathSetting.setStringValue(SelectedPath);
    }

    public void SetSelectedPath(String newPath) {
        SelectedPath = newPath;
        ((SettingsModelString) getModel()).setStringValue(SelectedPath);
    }
}
