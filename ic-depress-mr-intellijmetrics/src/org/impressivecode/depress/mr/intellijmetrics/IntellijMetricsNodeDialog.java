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
package org.impressivecode.depress.mr.intellijmetrics;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 *
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 *
 */
public class IntellijMetricsNodeDialog extends DefaultNodeSettingsPane {

  private static final String FILE_EXTENSION = ".xml";
  private static final String HISTORY_ID_XML = "history_xml";
  private static final String HISTORY_ID_DIR = "history_dir";

  private final DialogComponentButtonGroup radioButton = new DialogComponentButtonGroup(
      IntellijMetricsNodeModel.createRadioSettings(), null, false,
      IntellijMetricsNodeModel.actions, IntellijMetricsNodeModel.actions);
  private final DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
      IntellijMetricsNodeModel.createFileSettingsXML(), HISTORY_ID_XML,
      JFileChooser.OPEN_DIALOG, false, FILE_EXTENSION);
  private final DialogComponentFileChooser directoryChooser = new DialogComponentFileChooser(
      IntellijMetricsNodeModel.createFileSettingsDIR(), HISTORY_ID_DIR,
      JFileChooser.OPEN_DIALOG, true);

  protected IntellijMetricsNodeDialog() {
    createNewGroup("Input:");
    addDialogComponent(radioButton);
    addDialogComponent(fileChooser);
    addDialogComponent(directoryChooser);
    directoryChooser.getComponentPanel().setVisible(false);
    radioButton.getModel().addChangeListener(
        new RadioButtonChangeListener());
    closeCurrentGroup();
  }

  private class RadioButtonChangeListener implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent event) {
      SettingsModelString radioSettings = (SettingsModelString) (event
          .getSource());
      if (radioSettings.getStringValue().equals(
          IntellijMetricsNodeModel.FILE)) {
        fileChooser.getComponentPanel().setVisible(true);
        directoryChooser.getComponentPanel().setVisible(false);
        fileChooser.getModel().setEnabled(true);
        directoryChooser.getModel().setEnabled(false);
      } else {
        fileChooser.getComponentPanel().setVisible(false);
        directoryChooser.getComponentPanel().setVisible(true);
        fileChooser.getModel().setEnabled(false);
        directoryChooser.getModel().setEnabled(true);
      }
    }
  }

}
