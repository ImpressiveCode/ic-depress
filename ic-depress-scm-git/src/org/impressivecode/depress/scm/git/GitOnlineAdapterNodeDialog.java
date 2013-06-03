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
package org.impressivecode.depress.scm.git;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger(GitOnlineAdapterNodeDialog.class);

    private final SettingsModelString branch = new SettingsModelString(GitOnlineAdapterNodeModel.GIT_BRANCH, GitOnlineAdapterNodeModel.GIT_BRANCH_DEFAULT);
    private final SettingsModelString repoPath = new SettingsModelString(GitOnlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS,
            GitOnlineAdapterNodeModel.GIT_REPOSITORY_DEFAULT);
    private final SettingsModelString remoteRepo = new SettingsModelString(GitOnlineAdapterNodeModel.GIT_REMOTE_REPOSITORY_ADDRESS,
            GitOnlineAdapterNodeModel.GIT_REMOTE_REPOSITORY_DEFAULT);
    private final List<String> branchList = new ArrayList<String>();
    private final String initialProgressInfo = "Cloning progress: not running";
    final DialogComponentLabel progressInfoLabel = new DialogComponentLabel(initialProgressInfo);


    DialogComponentStringSelection comboBox;

    protected GitOnlineAdapterNodeDialog() {
        super();

        final DialogComponentString remoteRepoAddress = new DialogComponentString(remoteRepo, "Remote repository address: ");
        final DialogComponentButton cloneButton = new DialogComponentButton("Clone repository");

        cloneButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker<Void, String>() {
                    Boolean cloneResult;
                    @SuppressWarnings("deprecation")
                    @Override
                    protected Void doInBackground() throws Exception {
                        cloneButton.setText("Cloning...");
                        cloneButton.setEnabled(false);
                        cloneResult = cloneRepository();
                        return null;
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    protected void done() {
                        cloneButton.setText("Clone repository");
                        cloneButton.setEnabled(true);
                        progressInfoLabel.setText(initialProgressInfo);
                        if (cloneResult)
                            getBranchesList();
                    }
                }.execute();
            }

        });

        branchList.add("<Click on \"Get Branches\">");

        DialogComponentFileChooser comp = new DialogComponentFileChooser(repoPath, GitOnlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS, JFileChooser.OPEN_DIALOG, true);
        comp.setBorderTitle("Choose project directory:");

        if (branch.isEnabled()) {
            branch.setEnabled(false);
        }
        comboBox = new DialogComponentStringSelection(branch, "Choose branch:", branchList);


        DialogComponentButton button = new DialogComponentButton("Get branches");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getBranchesList();
            }
        });

        createNewGroup("Choose repository");
        setHorizontalPlacement(true);
        addDialogComponent(remoteRepoAddress);
        addDialogComponent(cloneButton);
        setHorizontalPlacement(false);
        addDialogComponent(progressInfoLabel);
        addDialogComponent(comp);
        setHorizontalPlacement(true);
        addDialogComponent(comboBox);
        addDialogComponent(button);
        setHorizontalPlacement(false);

        createNewGroup("Other settings");
        addDialogComponent(new DialogComponentString(new SettingsModelString(GitOnlineAdapterNodeModel.GIT_REGEXP,
                GitOnlineAdapterNodeModel.GIT_REGEXP_DEFAULT), "Issue marker: "));

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitOnlineAdapterNodeModel.GIT_PACKAGENAME,
                GitOnlineAdapterNodeModel.GIT_PACKAGENAME_DEFAULT), "Package: "));
    }


    private Boolean cloneRepository(){
        try {
            String gitRemote = remoteRepo.getStringValue();
            String gitPath = repoPath.getStringValue();
            File localRepo = new File(gitPath);
            if (gitRemote.length() != 0 && gitPath.length() != 0){
                if (localRepo.isDirectory() == false){
                    logger.error("Local path should be empty");
                    return false;
                } else {
                    if (localRepo.list().length > 0){
                        gitPath = gitPath+File.separatorChar+this.getLocalPathName(gitRemote);
                        localRepo = new File(gitPath);
                        repoPath.setStringValue(gitPath);
                    }
                    //NodeLoggerProgressMonitor monitor = new NodeLoggerProgressMonitor(logger);
                    LabelProgressMonitor monitor = new LabelProgressMonitor(progressInfoLabel, "Cloning progress: ");
                    progressInfoLabel.setText("Clonning progress: starting");
                    GitOnlineLogParser.cloneRepository(gitRemote, gitPath, monitor);
                    return true;
                }
            } else {
                logger.error("Remote repository and local path should be given.");
                return false;
            }
        } catch (Exception e1){
            logger.error("Failed to get list of branches from repository", e1);
            return false;
        }
    }

    private void getBranchesList(){
        try {
            String gitPath = GitOnlineAdapterNodeModel.getGitPath(repoPath.getStringValue());
            comboBox.replaceListItems(GitOnlineLogParser.getBranches(gitPath), GitOnlineLogParser.getCurrentBranch(gitPath));
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

    private String getLocalPathName(final String url){
        String folderName;
        String[] splittedUrl = url.split("/");
        String possiblePathName = splittedUrl[splittedUrl.length - 1];
        if (possiblePathName.length() == 0)
            possiblePathName = splittedUrl[splittedUrl.length - 2];

        folderName = possiblePathName.replace(".git", "");
        return folderName;
    }
}
