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

import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createTableRow;
import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createTableSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * 
 */
public class CrawlerAdapterNodeModel extends NodeModel {

    private static final String DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "depress.sourcecrawler.confname";

    private final SettingsModelString fileSettings = createFileSettings();

    protected CrawlerAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        CrawlerEntriesParser entriesParser = new CrawlerEntriesParser();
        BufferedDataContainer container = createDataContainer(exec);
        SourceCrawlerOutput result = entriesParser.parseSourceCrawlerResult(fileSettings.getStringValue());
        BufferedDataTable out = transform(container, result, exec);

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
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

    private BufferedDataTable transform(final BufferedDataContainer container, final SourceCrawlerOutput classes,
            final ExecutionContext exec) throws CanceledExecutionException {
        List<SourceFile> sourceFiles = classes.getSourceFiles();
        int size = sourceFiles.size();
        fillTable(container, exec, sourceFiles, size);
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private void fillTable(final BufferedDataContainer container, final ExecutionContext exec,
            final List<SourceFile> sourceFiles, final int size) throws CanceledExecutionException {
        for (int i = 0; i < size; i++) {
            progress(exec, size, i);
            SourceFile sourceFile = sourceFiles.get(i);
            List<Clazz> classesInSource = sourceFile.getClasses();
            addClassesToDatatable(container, sourceFile, classesInSource);

        }
    }

    private void addClassesToDatatable(final BufferedDataContainer container, final SourceFile sourceFile,
            final List<Clazz> classesInSource) {
        for (Clazz clazz : classesInSource) {

            addRowToTable(container, sourceFile.getSourcePackage() + "." + clazz.getName(), clazz.getName(),
                    clazz.getType(), clazz.isException(), clazz.isInner(), clazz.isTest(),
                    sourceFile.getSourcePackage(), sourceFile.getPath());
        }
    }

    private void addRowToTable(final BufferedDataContainer container, final String id, final String name, final String type, final boolean exception,
            final boolean inner, final boolean test, final String sourcePackage, final String path) {
        container.addRowToTable(createTableRow(id, name, type, exception, inner, test, sourcePackage, path));
    }

    private void progress(final ExecutionContext exec, final int size, final int i) throws CanceledExecutionException {
        exec.checkCanceled();
        exec.setProgress(i / size);
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        DataTableSpec outputSpec = createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        return container;
    }

    public SettingsModelString createFileSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }
}
