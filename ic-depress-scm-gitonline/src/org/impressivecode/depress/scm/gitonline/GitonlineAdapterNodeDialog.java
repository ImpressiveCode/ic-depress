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
package org.impressivecode.depress.scm.gitonline;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitonlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger(GitonlineAdapterNodeDialog.class);

    private final SettingsModelString branch = new SettingsModelString(GitonlineAdapterNodeModel.GIT_BRANCH, GitonlineAdapterNodeModel.GIT_BRANCH_DEFAULT);
    private final SettingsModelString repoPath = new SettingsModelString(GitonlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS,
            GitonlineAdapterNodeModel.GIT_REPOSITORY_DEFAULT);
    private final List<String> branchList = new ArrayList<String>();

    DialogComponentStringSelection comboBox;

    protected GitonlineAdapterNodeDialog() {
        super();

        branchList.add("<Click on \"Get Branches\">");

        DialogComponentFileChooser comp = new DialogComponentFileChooser(repoPath, GitonlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS, JFileChooser.OPEN_DIALOG, true);
        comp.setBorderTitle("Choose project directory:");

        if (branch.isEnabled()) {
            branch.setEnabled(false);
        }
        comboBox = new DialogComponentStringSelection(branch, "Choose branch:", branchList);


        DialogComponentButton button = new DialogComponentButton("Get branches");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String gitPath = GitonlineAdapterNodeModel.getGitPath(repoPath.getStringValue());
                    comboBox.replaceListItems(GitonlineLogParser.getBranches(gitPath), GitonlineLogParser.getCurrentBranch(gitPath));
                    if (!branch.isEnabled()) {
                        branch.setEnabled(true);
                    }
                } catch (NoWorkTreeException | IOException | GitAPIException e1) {
                    logger.error("Failed to get list of branches from repository", e1);
                    List<String> errorBranchList = new ArrayList<String>();
                    errorBranchList.add("<Error>");
                    comboBox.replaceListItems(errorBranchList, null);
                }
            }
        });

        addDialogComponent(comp);
        setHorizontalPlacement(true);
        addDialogComponent(comboBox);
        addDialogComponent(button);
        setHorizontalPlacement(false);

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitonlineAdapterNodeModel.GIT_REGEXP,
                GitonlineAdapterNodeModel.GIT_REGEXP_DEFAULT), "Issue marker: "));

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitonlineAdapterNodeModel.GIT_PACKAGENAME,
                GitonlineAdapterNodeModel.GIT_PACKAGENAME_DEFAULT), "Package: "));
    }
}
