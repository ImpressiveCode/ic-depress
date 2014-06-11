package org.impressivecode.depress.mr.astcompare;

import static org.impressivecode.depress.mr.astcompare.utils.AstMetricsTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.DATE_FROM;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.DATE_TO;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.DEFAULT_VALUE;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.PROJECTS_NAMES;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.WEEKS;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.getWeeksAsInteger;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.RepositoryProvider;
import org.impressivecode.depress.mr.astcompare.ast.AstController;
import org.impressivecode.depress.mr.astcompare.db.DbHandler;
import org.impressivecode.depress.mr.astcompare.svn.SvnHandler;
import org.impressivecode.depress.mr.astcompare.utils.AstMetricsTransformer;
import org.impressivecode.depress.mr.astcompare.utils.Utils;
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
public class AstComparePluginNodeModel extends NodeModel {

    // default values
    private final SettingsModelString m_projects = new SettingsModelString(PROJECTS_NAMES, DEFAULT_VALUE);
    private final SettingsModelString m_dateFrom = new SettingsModelString(DATE_FROM, Utils.getCurrentDayPlus(
            Calendar.MONTH, -2));
    private final SettingsModelString m_dateTo = new SettingsModelString(DATE_TO, Utils.getCurrentDate());
    private final SettingsModelString m_weeks = new SettingsModelString(WEEKS, "All");
    private final Shell shell = new Shell();
    private DbHandler db;
    private IProject selectedProject;

    protected AstComparePluginNodeModel() {
        super(0, 1);
        try {
            db = DbHandler.getInstance();
            db.connect();
        } catch (Exception e) {
            NodeLogger.getLogger(AstComparePluginNodeModel.class).error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        long revisionDateMin = Utils.getTime(m_dateFrom.getStringValue());
        long revisionDateMax = Utils.getTime(m_dateTo.getStringValue());

        if (selectedProject != null) {
            IJavaProject project = JavaCore.create(selectedProject);
            RepositoryProvider provider = RepositoryProvider.getProvider(project.getProject());
            SvnHandler svnHandler = new SvnHandler(exec, provider, revisionDateMin, revisionDateMax);

            AstController controller = new AstController(exec, db, svnHandler);
            if (!db.isDataExistInDb(selectedProject.getName(), revisionDateMin, revisionDateMax)) {
                controller.collectDataAndSaveInDb(project.getPackageFragments(), selectedProject.getName(),
                        revisionDateMin, revisionDateMax);
            }
        }
        AstMetricsTransformer astMetricsTransformer = new AstMetricsTransformer(createDataColumnSpec());
        BufferedDataTable out = astMetricsTransformer.transform(astMetricsTransformer.getMetricEntries(db,
                selectedProject.getName(), revisionDateMin, revisionDateMax,
                getWeeksAsInteger(m_weeks.getStringValue())), exec);

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_projects.saveSettingsTo(settings);
        m_dateFrom.saveSettingsTo(settings);
        m_dateTo.saveSettingsTo(settings);
        m_weeks.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

        if (settings.containsKey(PROJECTS_NAMES)) {
            m_projects.loadSettingsFrom(settings);
        }
        if (settings.containsKey(DATE_FROM)) {
            m_dateFrom.loadSettingsFrom(settings);
        }
        if (settings.containsKey(DATE_TO)) {
            m_dateTo.loadSettingsFrom(settings);
        }
        if (settings.containsKey(WEEKS)) {
            m_weeks.loadSettingsFrom(settings);
        }
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

        IWorkspace root = ResourcesPlugin.getWorkspace();

        if (root.getRoot().getProjects().length <= 0) {
            throw new InvalidSettingsException("Empty workspace. Please import a Java project into workspace.");
        }

        selectedProject = root.getRoot().getProject(settings.getString(PROJECTS_NAMES));

        try {
            if (selectedProject != null) {
                if (selectedProject.hasNature(JavaCore.NATURE_ID)) {
                    m_projects.validateSettings(settings);
                } else {
                    throw new InvalidSettingsException("\"" + selectedProject.getName() + "\" is not Java project.");
                }
            }
            Utils.validateDate(settings.getString(DATE_FROM));
            Utils.validateDate(settings.getString(DATE_TO));

        } catch (final CoreException e) {
            shell.getDisplay().asyncExec(new Runnable() {
                public void run() {
                    MessageDialog.openError(shell, "AST Compare Plugin", e.getMessage());
                }
            });
        }
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return new DataTableSpec[] { null };
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}
