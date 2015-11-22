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
package org.impressivecode.depress.its.bugzillaonline;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.Integer.parseInt;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.its.bugzilla.BugzillaAdapterTableFactory.createTableSpec;

import java.util.Date;
import java.util.List;

import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class BugzillaOnlineAdapterNodeModel extends ITSOnlineNodeModel {
    private static final int NUMBER_OF_INPUT_PORTS = 0;
    private static final int NUMBER_OF_OUTPUT_PORTS = 1;

    public static final String DEFAULT_COMBOBOX_ANY_VALUE = "Any";

    private static final String BUGZILLA_DATE = "date";
    private static final String BUGZILLA_LIMIT = "limit";
    private static final String BUGZILLA_OFFSET = "offset";
    private static final String BUGZILLA_ASSIGNED_TO = "assignedTo";
    private static final String BUGZILLA_REPORTER = "creator";
    private static final String BUGZILLA_VERSION = "version";
    private static final String BUGZILLA_PRIORITY = "priority";

    private final SettingsModelDate dateFromSettings = createDateSettings();
    private final SettingsModelOptionalString limitSettings = createLimitSettings();
    private final SettingsModelOptionalString offsetSettings = createOffsetSettings();
    private final SettingsModelOptionalString assignedToSettings = createAssignedToSettings();
    private final SettingsModelOptionalString reporterSettings = createReporterSettings();
    private final SettingsModelOptionalString versionSettings = createVersionSettings();
    private final SettingsModelString prioritySettings = createPrioritySettings();

    protected BugzillaOnlineAdapterNodeModel() {
        super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext context)
            throws Exception {
        LOGGER.info("Preparing to read bugzilla entries.");
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(getURL(), context);

        if (isUsernameProvided(getLogin())) {
            LOGGER.info("Logging to bugzilla as: " + getLogin());
            clientAdapter.setCredentials(getLogin(), getPassword());
        }

        LOGGER.info("Reading entries from bugzilla instance: " + getURL() + " and product: " + getProductName());
        List<ITSDataType> entries = clientAdapter.listEntries(getBugzillaOptions());

        LOGGER.info("Transforming to bugzilla entries.");
        BufferedDataTable out = transform(entries, context);

        LOGGER.info("Bugzilla table created.");
        return new BufferedDataTable[] { out };
    }

    private BugzillaOnlineOptions getBugzillaOptions() {
        BugzillaOnlineOptions options = new BugzillaOnlineOptions();
        options.setProductName(getProductName());
        options.setDateFrom(getDateFrom());
        options.setAssignedTo(getAssignedTo());
        options.setReporter(getReporter());
        options.setVersion(getVersion());
        options.setPriority(getPriority());
        options.setLimit(getLimit());
        options.setOffset(getOffset());
        return options;
    }

    private Date getDateFrom() {
        return dateFromSettings.getDate();
    }

    private String getAssignedTo() {
        return returnActiveStringSetting(assignedToSettings);
    }

    private String getReporter() {
        return returnActiveStringSetting(reporterSettings);
    }

    private String getVersion() {
        return returnActiveStringSetting(versionSettings);
    }

    private String returnActiveStringSetting(SettingsModelOptionalString model) {
        if (model.isActive()) {
            return !isNullOrEmpty(model.getStringValue()) ? model.getStringValue() : null;
        } else {
            return null;
        }
    }

    private String getPriority() {
        return isComboboxChoosen(prioritySettings.getStringValue()) ? prioritySettings.getStringValue() : null;
    }

    private boolean isComboboxChoosen(String value) {
        return !DEFAULT_COMBOBOX_ANY_VALUE.equals(value);
    }

    private Integer getLimit() {
        return getOptionalIntegerValue(limitSettings);
    }

    private Integer getOffset() {
        return getOptionalIntegerValue(offsetSettings);
    }

    private Integer getOptionalIntegerValue(SettingsModelOptionalString settings) {
        Integer result = null;
        if (settings.isActive()) {
            try {
                result = parseInt(settings.getStringValue());
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }

    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        checkArgument(inSpecs.length == 0);
        return createTableSpec();
    }

    static SettingsModelDate createDateSettings() {
        return new SettingsModelDate(BUGZILLA_DATE);
    }

    static SettingsModelOptionalString createLimitSettings() {
        return new SettingsModelOptionalString(BUGZILLA_LIMIT, DEFAULT_STRING_VALUE, false);
    }

    static SettingsModelOptionalString createOffsetSettings() {
        return new SettingsModelOptionalString(BUGZILLA_OFFSET, DEFAULT_STRING_VALUE, false);
    }

    static SettingsModelOptionalString createAssignedToSettings() {
        return new SettingsModelOptionalString(BUGZILLA_ASSIGNED_TO, DEFAULT_STRING_VALUE, false);
    }

    static SettingsModelOptionalString createReporterSettings() {
        return new SettingsModelOptionalString(BUGZILLA_REPORTER, DEFAULT_STRING_VALUE, false);
    }

    static SettingsModelOptionalString createVersionSettings() {
        return new SettingsModelOptionalString(BUGZILLA_VERSION, DEFAULT_STRING_VALUE, false);
    }

    static SettingsModelString createPrioritySettings() {
        return new SettingsModelString(BUGZILLA_PRIORITY, DEFAULT_COMBOBOX_ANY_VALUE);
    }

    @Override
    protected void saveSpecificSettingsTo(final NodeSettingsWO settings) {
        dateFromSettings.saveSettingsTo(settings);
        limitSettings.saveSettingsTo(settings);
        offsetSettings.saveSettingsTo(settings);
        assignedToSettings.saveSettingsTo(settings);
        reporterSettings.saveSettingsTo(settings);
        versionSettings.saveSettingsTo(settings);
        prioritySettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadSpecificSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        dateFromSettings.loadSettingsFrom(settings);
        limitSettings.loadSettingsFrom(settings);
        offsetSettings.loadSettingsFrom(settings);
        assignedToSettings.loadSettingsFrom(settings);
        reporterSettings.loadSettingsFrom(settings);
        versionSettings.loadSettingsFrom(settings);
        prioritySettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSpecificSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        dateFromSettings.validateSettings(settings);
        limitSettings.validateSettings(settings);
        offsetSettings.validateSettings(settings);
        assignedToSettings.validateSettings(settings);
        reporterSettings.validateSettings(settings);
        prioritySettings.validateSettings(settings);
        versionSettings.validateSettings(settings);
    }

}
