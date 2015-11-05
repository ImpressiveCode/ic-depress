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
package org.impressivecode.depress.support.markerparser;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MESSAGE_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MESSAGE_COLSPEC;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.impressivecode.depress.common.Cells;
import org.impressivecode.depress.scm.SCMInputTransformer;
import org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory;
import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.base.data.append.column.AppendedColumnTable;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
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
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerParserNodeModel extends NodeModel {

    static final String CFG_REGEXP = "regexp";
    static final String REGEXP_DEFAULT = "";

    private final SettingsModelString regExp = new SettingsModelString(CFG_REGEXP, REGEXP_DEFAULT);

    protected MarkerParserNodeModel() {
        super(1, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        AppendedColumnTable table = new AppendedColumnTable(inData[0], markerCellFactory(inData[0]), MarkerAdapterTableFactory.MARKER_COLSPEC);

        return new BufferedDataTable[] { preapreTable(table, exec) };
    }

    private MarkerCellFactory markerCellFactory(final BufferedDataTable inData) {
        return new MarkerCellFactory(regExp.getStringValue(), inData.getSpec().findColumnIndex(MESSAGE_COLNAME));
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

        final DataTableSpec dts = AppendedColumnTable.getTableSpec(inSpecs[0], MarkerAdapterTableFactory.MARKER_COLSPEC);

        return new DataTableSpec[] { dts };
    }

    private void validate(final DataTableSpec spec) throws InvalidSettingsException {
        checkNotNull(spec, "DataTableSpec hat to be set");
        new SCMInputTransformer().setMinimalSpec(new DataTableSpec(MESSAGE_COLSPEC)).setInputSpec(spec).validate();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        regExp.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        regExp.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        regExp.validateSettings(settings);
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

    public class MarkerCellFactory implements AppendedCellFactory {

        private final Pattern markerRegexp;
        private final int msgCellIndex;

        public MarkerCellFactory(final String regexp, final int msgCellIndex) {
            if (!Strings.isNullOrEmpty(regexp)) {
                markerRegexp = Pattern.compile(regexp);
            } else {
                markerRegexp = null;
            }

            this.msgCellIndex = msgCellIndex;
        }

        @Override
        public DataCell[] getAppendedCell(final DataRow row) {
            Set<String> markers = Sets.newHashSet();
            if (this.markerRegexp != null) {
                String message = ((StringCell) row.getCell(msgCellIndex)).getStringValue();
                Matcher matcher = this.markerRegexp.matcher(message);
                while (matcher.find()) {
                    if (matcher.groupCount() >= 1) {
                        markers.add(matcher.group(1));
                    } else {
                        markers.add(matcher.group());
                    }
                }
            }
            return new DataCell[] { Cells.stringSetCell(markers) };
        }
    }
}
