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

package org.impressivecode.depress.scm.svn;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

public class SVNNodeDialog extends DefaultNodeSettingsPane {

	private DialogComponentFileChooser pathChooserDialog;
	private DialogComponentString issueMakerDialog;
	private DialogComponentString packageDialog;

	/**
	 * New pane for configuring MyNode01 node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected SVNNodeDialog() {
		super();

		pathChooserDialog = new DialogComponentFileChooser(
				SVNSettings.SVN_PATH, SVNSettings.SVN_PATH_CFG, ".xml");

		issueMakerDialog = new DialogComponentString(
				SVNSettings.SVN_ISSUE_MARKER, SVNLocale.nIssueMarker());

		packageDialog = new DialogComponentString(SVNSettings.SVN_PACKAGE,
				SVNLocale.nPackage());

		addDialogComponent(pathChooserDialog);
		addDialogComponent(issueMakerDialog);
		addDialogComponent(packageDialog);
	}

	@Override
	public void saveAdditionalSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		super.saveAdditionalSettingsTo(settings);
	}
}
