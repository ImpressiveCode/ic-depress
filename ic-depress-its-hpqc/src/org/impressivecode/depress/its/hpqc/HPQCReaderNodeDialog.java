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
package org.impressivecode.depress.its.hpqc;

import java.awt.Container;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NotConfigurableException;
import org.knime.ext.poi.node.read2.XLSReaderNodeDialog;
import org.knime.ext.poi.node.read2.XLSUserSettings;

/**
 * The dialog to the HPQC reader.
 *
 * @author Łukasz Leśniczek, Wrocław, Poland
 * @author Mariusz Mulka, Wrocław, Poland
 * 
 */
public class HPQCReaderNodeDialog extends XLSReaderNodeDialog {

	private static final String TAB_NAME="HPQC Reader Settings";
	static String filename;
	public HPQCReaderNodeDialog() {
		super();
		super.renameTab("XLS Reader Settings", TAB_NAME);
		Container hpTab= (Container)this.getTab(TAB_NAME);
		Container secondContainer =(Container)((Container)((Container)hpTab.getComponent(0)).getComponent(0)).getComponent(1);
		//hide component: Column names
		secondContainer.getComponent(1).setVisible(false);
		secondContainer.getComponent(2).setVisible(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings,
			final DataTableSpec[] specs) throws NotConfigurableException {
		XLSUserSettings s;
		try {
			s = XLSUserSettings.load(settings);
			filename =s.getFileLocation();
		} catch (InvalidSettingsException e) {
			s = new XLSUserSettings();
		}
		s.setHasColHeaders(true);
		NodeSettings clone = new NodeSettings("clone");
		s.save(clone);
		super.loadSettingsFrom(clone, specs);
	}

}
