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

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.scm.git.GitParserOptions.options;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.scm.SCMAdapterTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Marek Majchrzak, ImpressiveCode
 */
public class GitOnlineAdapterNodeModel extends NodeModel {
    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger(GitOnlineAdapterNodeModel.class);

    static final String GIT_REPOSITORY_ADDRESS = "filename";
    static final String GIT_REMOTE_REPOSITORY_ADDRESS = "remote address";
    static final String GIT_PACKAGENAME = "package";
    static final String GIT_BRANCH = "branch";
    
    static final String GIT_REPOSITORY_DEFAULT = "";
    static final String GIT_REMOTE_REPOSITORY_DEFAULT = "";
    static final String GIT_BRANCH_DEFAULT = "";
    static final String GIT_PACKAGENAME_DEFAULT = "";
    static final Boolean GIT_PACKAGENAME_ACTIVE_STATE = false;

    // example value: the models count variable filled from the dialog
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString gitRepositoryAddress = new SettingsModelString(GitOnlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS,
            GitOnlineAdapterNodeModel.GIT_REPOSITORY_DEFAULT);
    private final SettingsModelOptionalString gitBranch = new SettingsModelOptionalString(GitOnlineAdapterNodeModel.GIT_BRANCH,
            GitOnlineAdapterNodeModel.GIT_BRANCH_DEFAULT, true);
    private final SettingsModelOptionalString gitPackageName = new SettingsModelOptionalString(
            GitOnlineAdapterNodeModel.GIT_PACKAGENAME, GitOnlineAdapterNodeModel.GIT_PACKAGENAME_DEFAULT, true);

    protected GitOnlineAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        String gitPath = getGitPath(this.gitRepositoryAddress.getStringValue());

        logger.info("Reading logs from repository " + gitPath);
        GitOnlineLogParser parser = new GitOnlineLogParser();
        
        List<GitCommit> commits = parser.parseEntries(gitPath,
                options(gitPackageName.getStringValue(), gitBranch.getStringValue()));

        BufferedDataTable out = transform(commits, exec);
        logger.info("Reading git logs finished.");

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return GitAdapterTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        gitRepositoryAddress.saveSettingsTo(settings);
        gitBranch.saveSettingsTo(settings);
        gitPackageName.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        gitRepositoryAddress.loadSettingsFrom(settings);
        gitBranch.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        gitRepositoryAddress.validateSettings(settings);
        gitBranch.loadSettingsFrom(settings);
        gitPackageName.validateSettings(settings);
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    private BufferedDataTable transform(final List<GitCommit> commits, final ExecutionContext exec)
            throws CanceledExecutionException {
        List<SCMDataType> data = Lists.newLinkedList();
        for (GitCommit commit : commits) {
            for (GitCommitFile file : commit.getFiles()) {
                progress(exec);
                data.add(scm(commit, file));
            }
        }
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(data, exec);
    }

    private SCMDataType scm(final GitCommit commit, final GitCommitFile file) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor(commit.getAuthor());
        scm.setCommitDate(commit.getDate());
        scm.setCommitID(commit.getId());
        scm.setMessage(commit.getMessage());
        scm.setOperation(file.getOperation());
        scm.setPath(file.getPath());
        scm.setExtension(file.getExtension());
        scm.setResourceName(file.getJavaClass());
        return scm;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }

    public static String getGitPath(final String repositoryPath) {
        File repoFile = new File(repositoryPath);
        if (!repoFile.getName().equals(".git")) {
            return repoFile.getAbsolutePath() + File.separatorChar + ".git";
        }
        return repoFile.getAbsolutePath();
    }
}
