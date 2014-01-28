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

import static com.google.common.collect.Lists.newArrayList;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineAdapterNodeModel.DEFAULT_COMBOBOX_ANY_VALUE;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.ITSNodeDialog;
import org.impressivecode.depress.its.ITSPriority;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author MichaÅ‚ Negacz, WrocÅ‚aw University of Technology
 * @author Piotr WrÃ³blewski, WrocÅ‚aw University of Technology
 * @author Bartosz Skuza, WrocÅ‚aw University of Technology
 * 
 */
public class BugzillaOnlineAdapterNodeDialog extends ITSNodeDialog {

    public static final String UNKNOWN_ENUM_NAME = "UNKNOWN";

    public static final String BUGS_PER_TASK_LABEL = "Bugs per thread:";

    public static final String DATE_FROM_LABEL = "Date from:";

    public static final String ASSIGNED_TO_LABEL = "Assigned to:";

    public static final String LIMIT_LABEL = "Limit:";

    public static final String OFFSET_LABEL = "Offset:";

    public static final String REPORTER_LABEL = "Reporter:";

    public static final String PRIORITY_LABEL = "Priority:";

    public static final String VERSION_LABEL = "Version:";

    private DialogComponentString projectName;
    private DialogComponentOptionalString limit;
    private DialogComponentOptionalString offset;
    private DialogComponentDate date;
    private DialogComponentOptionalString assignedTo;
    private DialogComponentOptionalString reporter;
    private DialogComponentOptionalString version;
    private DialogComponentStringSelection priority;
    private DialogComponentNumberEdit bugsPerTask;

    @Override
    protected SettingsModelString createURLSettings() {
        return BugzillaOnlineAdapterNodeModel.createURLSettings();
    }

    @Override
    protected SettingsModelString createProjectSettings() {
        return BugzillaOnlineAdapterNodeModel.createProductSettings();
    }

    @Override
    protected ActionListener getButtonConnectionCheckListener() {
        return null;
    }

    @Override
    protected Component createConnectionTab() {
        JPanel panel = (JPanel) super.createConnectionTab();
        panel.add(createProjectChooser());
        return panel;
    }

    protected Component createProjectChooser() {
        projectName = new DialogComponentString(createProjectSettings(), PROJECT_LABEL, true, COMPONENT_WIDTH);
        return projectName.getComponentPanel();
    };

    @Override
    protected SettingsModelString createLoginSettings() {
        return BugzillaOnlineAdapterNodeModel.createUsernameSettings();
    }

    @Override
    protected SettingsModelString createPasswordSettings() {
        return BugzillaOnlineAdapterNodeModel.createPasswordSettings();
    }

    @Override
    protected Component createFiltersTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createAndAddLimitFilter());
        panel.add(createAndAddOffsetFilter());
        panel.add(createAndAddDateFromFilter());
        panel.add(createAndAddAssignedToFilter());
        panel.add(createAndAddReporterFilter());
        panel.add(createAndAddVersionFilter());
        panel.add(createAndAddPriorityFilter());

        return panel;
    }

    private Component createAndAddLimitFilter() {
        limit = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createLimitSettings(), LIMIT_LABEL,
                COMPONENT_WIDTH);
        return limit.getComponentPanel();
    }

    private Component createAndAddOffsetFilter() {
        offset = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createOffsetSettings(), OFFSET_LABEL,
                COMPONENT_WIDTH);
        return offset.getComponentPanel();
    }

    private Component createAndAddDateFromFilter() {
        date = new DialogComponentDate(BugzillaOnlineAdapterNodeModel.createDateSettings(), DATE_FROM_LABEL);
        return date.getComponentPanel();
    }

    private Component createAndAddAssignedToFilter() {
        assignedTo = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createAssignedToSettings(),
                ASSIGNED_TO_LABEL);
        return assignedTo.getComponentPanel();
    }

    private Component createAndAddReporterFilter() {
        reporter = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createReporterSettings(),
                REPORTER_LABEL);
        return reporter.getComponentPanel();
    }

    private Component createAndAddVersionFilter() {
        version = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createVersionSettings(),
                VERSION_LABEL);
        return version.getComponentPanel();
    }

    private Component createAndAddPriorityFilter() {
        priority = new DialogComponentStringSelection(BugzillaOnlineAdapterNodeModel.createPrioritySettings(),
                PRIORITY_LABEL, prepareEnumValuesToComboBox(ITSPriority.values()));
        return priority.getComponentPanel();
    }

    private Collection<String> prepareEnumValuesToComboBox(Enum<?>[] enums) {
        List<String> strings = newArrayList();

        for (Enum<?> value : enums) {
            if (UNKNOWN_ENUM_NAME.equals(value.name())) {
                strings.add(DEFAULT_COMBOBOX_ANY_VALUE);
            } else {
                strings.add(value.name());
            }
        }

        return strings;
    }

    @Override
    protected SettingsModelInteger createThreadsCountSettings() {
        return BugzillaOnlineAdapterNodeModel.createThreadsCountSettings();
    }

    @Override
    protected Component createAdvancedTab() {
        JPanel panel = (JPanel) super.createAdvancedTab();
        panel.add(createAndAddBugsPerTaskComponent());
        return panel;
    }

    private Component createAndAddBugsPerTaskComponent() {
        bugsPerTask = new DialogComponentNumberEdit(BugzillaOnlineAdapterNodeModel.createBugsPerTaskSettings(),
                BUGS_PER_TASK_LABEL, COMPONENT_WIDTH);
        return bugsPerTask.getComponentPanel();
    }

    @Override
    protected void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        projectName.saveSettingsTo(settings);
        limit.saveSettingsTo(settings);
        offset.saveSettingsTo(settings);
        date.saveSettingsTo(settings);
        assignedTo.saveSettingsTo(settings);
        reporter.saveSettingsTo(settings);
        version.saveSettingsTo(settings);
        priority.saveSettingsTo(settings);
        bugsPerTask.saveSettingsTo(settings);
    }

    @Override
    protected void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException {

        projectName.loadSettingsFrom(settings, specs);
        limit.loadSettingsFrom(settings, specs);
        offset.loadSettingsFrom(settings, specs);
        date.loadSettingsFrom(settings, specs);
        assignedTo.loadSettingsFrom(settings, specs);
        reporter.loadSettingsFrom(settings, specs);
        version.loadSettingsFrom(settings, specs);
        priority.loadSettingsFrom(settings, specs);
        bugsPerTask.loadSettingsFrom(settings, specs);
    }

    @Override
    protected SettingsModelStringArray createFilterSettings() {
        // NOOP
        return new SettingsModelStringArray("noop", new String[] {});
    }

    @Override
    protected Collection<ITSFilter> getFilters() {
        return new ArrayList<>();
    }
}
