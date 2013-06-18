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

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
 * 
 * @author IcDepress
 */
public class SVNOnlineAdapterNodeModel extends NodeModel {

    private static final NodeLogger logger = NodeLogger.getLogger(SVNOnlineAdapterNodeModel.class);

    public static String SVN_REPOSITORY_DEFAULT = "";

    public static String SVN_REPOSITORY_ADDRESS = "depress.scm.svnonline.remoteaddress";

    public static String SVN_REGEXP = "org.";

    public static String SVN_PACKAGENAME = "depress.scm.svnonline.package";

    public static String SVN_PACKAGENAME_DEFAULT = "";

    public static String SVN_REGEXP_DEFAULT = "";

    private final SettingsModelString svnRepositoryAddress = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_REPOSITORY_ADDRESS, SVNOnlineAdapterNodeModel.SVN_REPOSITORY_DEFAULT);
    private final SettingsModelString svnRegExp = new SettingsModelString(SVNOnlineAdapterNodeModel.SVN_REGEXP,
            SVNOnlineAdapterNodeModel.SVN_REGEXP_DEFAULT);
    private final SettingsModelOptionalString svnPackageName = new SettingsModelOptionalString(
            SVNOnlineAdapterNodeModel.SVN_PACKAGENAME, SVNOnlineAdapterNodeModel.SVN_PACKAGENAME_DEFAULT, true);

    protected SVNOnlineAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        String svnPath = getSvnPath(this.svnRepositoryAddress.getStringValue());

        logger.info("Reading logs from repository " + svnPath);
        SVNOnlineLogParser parser = new SVNOnlineLogParser();

        List<SVNCommit> commits = parser.parseEntries(svnPath,
                SVNParserOptions.options(svnRegExp.getStringValue(), svnPackageName.getStringValue()));

        BufferedDataTable out = transform(commits, exec);
        logger.info("Reading git logs finished.");

        return new BufferedDataTable[] { out };
    }

    private BufferedDataTable transform(final List<SVNCommit> commits, final ExecutionContext exec)
            throws CanceledExecutionException {
        List<SCMDataType> data = Lists.newLinkedList();
        for (SVNCommit commit : commits) {
            for (SVNCommitFile file : commit.getFiles()) {
                progress(exec);
                data.add(scm(commit, file));
            }
        }
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(data, exec);
    }

    private SCMDataType scm(final SVNCommit commit, final SVNCommitFile file) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor(commit.getAuthor());
        scm.setCommitDate(commit.getDate());
        scm.setCommitID(commit.getId());
        scm.setMarkers(commit.getMarkers());
        scm.setMessage(commit.getMessage());
        scm.setOperation(file.getOperation());
        scm.setPath(file.getPath());
        scm.setResourceName(file.getJavaClass());
        return scm;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return SVNAdapterTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        svnRepositoryAddress.saveSettingsTo(settings);
        svnRegExp.saveSettingsTo(settings);
        svnPackageName.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        svnRepositoryAddress.loadSettingsFrom(settings);
        svnRegExp.loadSettingsFrom(settings);
        svnPackageName.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        svnRepositoryAddress.validateSettings(settings);
        svnRegExp.validateSettings(settings);
        svnPackageName.validateSettings(settings);
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
    }

    private String getSvnPath(final String repositoryPath) {
        return URI.create(repositoryPath).toString();
    }
}
