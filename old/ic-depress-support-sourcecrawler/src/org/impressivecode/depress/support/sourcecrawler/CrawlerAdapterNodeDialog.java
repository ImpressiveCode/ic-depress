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
package org.impressivecode.depress.support.sourcecrawler;

import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class CrawlerAdapterNodeDialog extends DefaultNodeSettingsPane {

	private static final String ADVANCED_TAB_NAME = "Advanced";

	private static final String FILE_EXTENSION = ".xml";
	private static final String HISTORY_ID_XML = "history_xml";
	private static final String HISTORY_ID_DIR = "history_dir";

	private static final int booleanComponentWidth = 40;
	private static final int booleanComponentHeight = 25;

	private final DialogComponentButtonGroup radioButton = new DialogComponentButtonGroup(
			CrawlerAdapterNodeModel.createRadioSettings(), null, false,
			CrawlerAdapterNodeModel.actions, CrawlerAdapterNodeModel.actions);
	private final DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
			CrawlerAdapterNodeModel.createFileSettingsXML(), HISTORY_ID_XML,
			JFileChooser.OPEN_DIALOG, false, FILE_EXTENSION);
	private final DialogComponentFileChooser directoryChooser = new DialogComponentFileChooser(
			CrawlerAdapterNodeModel.createFileSettingsDIR(), HISTORY_ID_DIR,
			JFileChooser.OPEN_DIALOG, true);

	protected CrawlerAdapterNodeDialog() {
		createOptionsTab();
		createAdvancedTab();
	}

	private void createOptionsTab() {
		createNewGroup("Input:");
		addDialogComponent(radioButton);
		addDialogComponent(fileChooser);
		addDialogComponent(directoryChooser);
		directoryChooser.getComponentPanel().setVisible(false);
		radioButton.getModel().addChangeListener(
				new RadioButtonChangeListener());
		closeCurrentGroup();
	}

	private void createAdvancedTab() {
		createNewTab(ADVANCED_TAB_NAME);
		setHorizontalPlacement(true);
		createNewGroup("Acces:");
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.PUBLIC,
				CrawlerAdapterNodeModel.PUBLIC_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.PRIVATE,
				CrawlerAdapterNodeModel.PRIVATE_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.PROTECTED,
				CrawlerAdapterNodeModel.PROTECTED_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.PACKAGE,
				CrawlerAdapterNodeModel.PACKAGE_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		closeCurrentGroup();

		createNewGroup("Type:");
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.CLASS,
				CrawlerAdapterNodeModel.CLASS_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.INTERFACE,
				CrawlerAdapterNodeModel.INTERFACE_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.ABSTRACT,
				CrawlerAdapterNodeModel.ABSTRACT_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.ENUM,
				CrawlerAdapterNodeModel.ENUM_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		closeCurrentGroup();

		createNewGroup("Other:");
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.EXCEPTION,
				CrawlerAdapterNodeModel.EXCEPTION_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.INNER,
				CrawlerAdapterNodeModel.INNER_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.TEST,
				CrawlerAdapterNodeModel.TEST_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.FINAL,
				CrawlerAdapterNodeModel.FINAL_CONFIG, true,
				booleanComponentWidth, booleanComponentHeight));
		setHorizontalPlacement(false);
		addDialogComponent(new DialogComponentString(
				CrawlerAdapterNodeModel.createPackageSettings(), "Package: ",
				false, 40));
		closeCurrentGroup();

		addDialogComponent(createBooleanComponent(
				CrawlerAdapterNodeModel.CREATE_XML,
				CrawlerAdapterNodeModel.CREATE_XML_CONFIG, false,
				booleanComponentWidth, booleanComponentHeight));
	}

	private DialogComponentBoolean createBooleanComponent(final String label,
			final String configLabel, final boolean defaultValue, int width,
			int height) {
		DialogComponentBoolean component = new DialogComponentBoolean(
				new SettingsModelBoolean(configLabel, defaultValue), label);
		component.getComponentPanel().setPreferredSize(
				new Dimension(width, height));
		return component;
	}

	private class RadioButtonChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent event) {
			SettingsModelString radioSettings = (SettingsModelString) (event
					.getSource());
			if (radioSettings.getStringValue().equals(
					CrawlerAdapterNodeModel.FILE)) {
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
