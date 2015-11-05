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
package org.impressivecode.depress.support.activitymatcher;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.ISSUE_ID_COLSPEC;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.RESOLVED_DATE_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.DATE_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.DATE_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MESSAGE_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.AM_MARKER_COLSPEC;
import static org.knime.base.data.append.column.AppendedColumnTable.getTableSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSInputTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMInputTransformer;
import org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory;
import org.knime.base.data.append.column.AppendedColumnTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ActivityMatcherParserNodeModel extends NodeModel {

    static final String CFG_INTERVAL = "regexp";
    static final String CFG_IDBUILDER = "builder";
    
    static final Integer INTERVAL_DEFAULT = 15;
    static final String IDBUILDER_DEFAULT = "%s";

    private final SettingsModelInteger interval = new SettingsModelInteger(CFG_INTERVAL, INTERVAL_DEFAULT);
    private final SettingsModelString builder = new SettingsModelString(CFG_IDBUILDER, IDBUILDER_DEFAULT);

    private InputTransformer<ITSDataType> itsTransfomer;
    private InputTransformer<SCMDataType> scmTransfomer;

    protected ActivityMatcherParserNodeModel() {
        super(2, 1);
        this.itsTransfomer = new ITSInputTransformer();
        this.scmTransfomer = new SCMInputTransformer();
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        List<ITSDataType> issues = this.itsTransfomer.transform(inData[1]);
        AppendedColumnTable table = new AppendedColumnTable(
                inData[0], 
                markerCellFactory(issues, 
                        inData[0].getSpec().findColumnIndex(DATE_COLNAME)),
                        MarkerAdapterTableFactory.AM_MARKER_COLSPEC);
        return new BufferedDataTable[] { preapreTable(table, exec) };
    }

    private ActivityMarkerCellFactory markerCellFactory(final List<ITSDataType> issues, final int dateIndex) {
        return new ActivityMarkerCellFactory(new Configuration(interval, builder, issues), dateIndex);
    }

    private BufferedDataTable preapreTable(final AppendedColumnTable table, final ExecutionContext exec)
            throws CanceledExecutionException {
        return exec.createBufferedDataTable(table, exec);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 2);

        this.scmTransfomer.setMinimalSpec(new DataTableSpec(MESSAGE_COLSPEC, DATE_COLSPEC)).setInputSpec(inSpecs[0]).validate();
        this.itsTransfomer.setMinimalSpec(new DataTableSpec(ISSUE_ID_COLSPEC, RESOLVED_DATE_COLSPEC)).setInputSpec(inSpecs[1]).validate();

        final DataTableSpec dts = getTableSpec(inSpecs[0], AM_MARKER_COLSPEC);

        return new DataTableSpec[] { dts };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        interval.saveSettingsTo(settings);
        builder.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        interval.loadSettingsFrom(settings);
        builder.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        interval.validateSettings(settings);
        builder.validateSettings(settings);
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
