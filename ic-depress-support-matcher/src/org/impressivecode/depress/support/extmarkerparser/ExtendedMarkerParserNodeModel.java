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
package org.impressivecode.depress.support.extmarkerparser;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.EXT_CONFIDENCE_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.EXT_MARKER_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MESSAGE_COLNAME;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.base.data.append.column.AppendedColumnTable;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ExtendedMarkerParserNodeModel extends NodeModel {

    static final String CFG_REGEXP_ID = "depress.support.matcher.extmarkerparser.idregexp";
    static final String REGEXP_ID_DEFAULT = "([0-9]+)";

    static final String CFG_REGEXP_KEYWORDS = "depress.support.matcher.extmarkerparser.keywordsregexp";
    static final String REGEXP_KEYWORDS_DEFAULT = "(?i)^.*\\b(bugs?|fix(e[ds])?|defects?|patch|pr)\\b.*$";

    static final String CFG_KEYWORDS = "depress.support.matcher.extmarkerparser.keywords";
    static final String KEYWORDS_DEFAULT = "exception";

    static final String CFG_IDBUILDER = "depress.support.matcher.extmarkerparser.builder";
    static final String IDBUILDER_DEFAULT = "%s";

    static final String CFG_REGEXP_ONLYIDS = "depress.support.matcher.extmarkerparser.onlyids";
    static final String REGEXP_ONLYIDS_DEFAULT = "^[,0-9 ]+$";

    private final SettingsModelString regExpID = new SettingsModelString(CFG_REGEXP_ID, REGEXP_ID_DEFAULT);
    private final SettingsModelString regExpKeywords = new SettingsModelString(CFG_REGEXP_KEYWORDS, REGEXP_KEYWORDS_DEFAULT);
    private final SettingsModelString keywords = new SettingsModelString(CFG_KEYWORDS, KEYWORDS_DEFAULT);
    private final SettingsModelString builder = new SettingsModelString(CFG_IDBUILDER, IDBUILDER_DEFAULT);
    private final SettingsModelString regExpOnlyIds = new SettingsModelString(CFG_REGEXP_ONLYIDS, REGEXP_ONLYIDS_DEFAULT);

    protected ExtendedMarkerParserNodeModel() {
        super(1, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        AppendedColumnTable table = new AppendedColumnTable(inData[0], markerCellFactory(inData[0]),
                EXT_MARKER_COLSPEC, EXT_CONFIDENCE_COLSPEC);

        return new BufferedDataTable[] { preapreTable(table, exec) };
    }

    private ExtMarkerCellFactory markerCellFactory(final BufferedDataTable inData) {
        return new ExtMarkerCellFactory(new Configuration(regExpID, regExpKeywords, keywords, regExpOnlyIds, builder),
                inData.getSpec().findColumnIndex(MESSAGE_COLNAME));
    }

    private BufferedDataTable preapreTable(final AppendedColumnTable table, final ExecutionContext exec)
            throws CanceledExecutionException {
        return exec.createBufferedDataTable(table, exec);
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 1);
        validate(inSpecs[0]);

        final DataTableSpec dts = AppendedColumnTable.getTableSpec(inSpecs[0], EXT_MARKER_COLSPEC, EXT_CONFIDENCE_COLSPEC);

        return new DataTableSpec[] { dts };
    }

    private void validate(final DataTableSpec spec) throws InvalidSettingsException {
        checkNotNull(spec, "DataTableSpec hat to be set");
        DataColumnSpec columnSpec = spec.getColumnSpec(MESSAGE_COLNAME);
        if (columnSpec == null) {
            throw new InvalidSettingsException("Missing SCM column: " + MESSAGE_COLNAME);
        }

        if (hasSameStructure(columnSpec)) {
            throw new InvalidSettingsException("Inlvalid type of column: " + MESSAGE_COLNAME);
        }
    }

    private boolean hasSameStructure(final DataColumnSpec columnSpec) {
        return columnSpec.equalStructure(SCMAdapterTableFactory.MESSAGE_COLSPEC);
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        regExpID.saveSettingsTo(settings);
        builder.saveSettingsTo(settings);
        regExpKeywords.saveSettingsTo(settings);
        keywords.saveSettingsTo(settings);
        regExpOnlyIds.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        regExpID.loadSettingsFrom(settings);
        builder.loadSettingsFrom(settings);
        regExpKeywords.loadSettingsFrom(settings);
        keywords.loadSettingsFrom(settings);
        regExpOnlyIds.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        regExpID.validateSettings(settings);
        builder.validateSettings(settings);
        regExpKeywords.validateSettings(settings);
        keywords.validateSettings(settings);
        regExpOnlyIds.validateSettings(settings);
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
}
