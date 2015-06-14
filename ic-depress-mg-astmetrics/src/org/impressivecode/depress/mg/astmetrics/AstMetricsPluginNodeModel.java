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
package org.impressivecode.depress.mg.astmetrics;

import static org.impressivecode.depress.mg.astmetrics.AstMetricsTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstMetricsPluginNodeModel extends NodeModel {
    private static final NodeLogger logger = NodeLogger.getLogger(AstMetricsPluginNodeModel.class);

    static final String BOTTOM_COMMIT = "bottom_commit";
    static final String TOP_COMMIT = "top_commit";
    static final String GIT_REPOSITORY_ADDRESS = "dirname";
    static final String GIT_PACKAGENAME = "package";
    static final String DEFAULT_GIT_PACKAGENAME = "org.";
    static final String DEFAULT_VALUE = "";

    private final SettingsModelString gitRepositoryAddress = new SettingsModelString(GIT_REPOSITORY_ADDRESS,
            DEFAULT_VALUE);
    private final SettingsModelString gitPackageName = new SettingsModelString(GIT_PACKAGENAME, DEFAULT_GIT_PACKAGENAME);
    private final SettingsModelString bottomCommit = new SettingsModelString(BOTTOM_COMMIT, DEFAULT_VALUE);
    private final SettingsModelString topCommit = new SettingsModelString(TOP_COMMIT, DEFAULT_VALUE);

    protected AstMetricsPluginNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        String gitPath = getGitPath(gitRepositoryAddress.getStringValue());

        logger.info("Reading from repository " + gitPath);
        AstLogParser parser = new AstLogParser(gitPath, gitPackageName.getStringValue(), new DbHandler(), exec);

        Map<String, MetricEntry> metrics = parser.getMetrics(topCommit.getStringValue(), bottomCommit.getStringValue());
        logger.info("Reading metrics finished.");

        AstMetricsTransformer astMetricsTransformer = new AstMetricsTransformer(exec, createDataColumnSpec());
        List<MetricEntry> metricList = astMetricsTransformer.getMetricsEntriesFromMap(metrics);
        BufferedDataTable[] out = new BufferedDataTable[] { astMetricsTransformer.transformMetrics(metricList) };
        logger.info("Transforming data finished.");

        return out;
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return AstMetricsTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        bottomCommit.saveSettingsTo(settings);
        topCommit.saveSettingsTo(settings);
        gitRepositoryAddress.saveSettingsTo(settings);
        gitPackageName.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        bottomCommit.loadSettingsFrom(settings);
        topCommit.loadSettingsFrom(settings);
        gitRepositoryAddress.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        bottomCommit.validateSettings(settings);
        topCommit.validateSettings(settings);
        gitRepositoryAddress.validateSettings(settings);
        gitPackageName.validateSettings(settings);
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    public static String getGitPath(final String repositoryPath) throws IOException {
        File repoFile = new File(repositoryPath);
        if (!repoFile.exists()) {
            throw new IOException("Specified directory does not exist");
        }
        if (!repoFile.getName().equals(".git")) {
            return repoFile.getAbsolutePath() + File.separatorChar + ".git";
        }
        return repoFile.getAbsolutePath();
    }

}
