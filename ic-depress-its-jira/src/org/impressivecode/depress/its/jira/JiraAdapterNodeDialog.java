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
package org.impressivecode.depress.its.jira;

import static org.impressivecode.depress.its.jira.JiraAdapterNodeModel.createFileChooserSettings;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.ITSResolution;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeDialog extends NodeDialogPane {

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.its.jira.historyid";

    private DialogComponentFileChooser chooser;
    private MultiFilterComponent multiFilterComponentPriority;
    private MultiFilterComponent multiFilterComponentType;
    private MultiFilterComponent multiFilterComponentResolution;

    protected JiraAdapterNodeDialog() {
        createSettingsTab();
        createPriorityTab();
        createTypeTab();
        createResolutionTab();
    }

    private void createSettingsTab() {
        chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
        addTab("Settings", chooser.getComponentPanel());
    }

    private void createPriorityTab() {
        multiFilterComponentPriority = new MultiFilterComponent(JiraAdapterNodeModel.PRIORITY_CONFIG_NAME,
                ITSPriority.labels(), new refreshPriorityCaller());
        addTab("Priority", multiFilterComponentPriority.getPanel());
    }

    private void createTypeTab() {
        multiFilterComponentType = new MultiFilterComponent(JiraAdapterNodeModel.TYPE_CONFIG_NAME, ITSType.labels(),
                new refreshTypeCaller());
        addTab("Type", multiFilterComponentType.getPanel());
    }

    private void createResolutionTab() {
        multiFilterComponentResolution = new MultiFilterComponent(JiraAdapterNodeModel.RESOLUTION_CONFIG_NAME,
                ITSResolution.labels(), new refreshResolutionCaller());
        addTab("Resolution", multiFilterComponentResolution.getPanel());
    }

    private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtension) {
        return new DialogComponentFileChooser(createFileChooserSettings(), historyId, fileExtension);
    }

    @Override
    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        chooser.loadSettingsFrom(settings, specs);
        multiFilterComponentPriority.loadSettingsFrom(settings, specs, null);
        multiFilterComponentType.loadSettingsFrom(settings, specs, null);
        multiFilterComponentResolution.loadSettingsFrom(settings, specs, null);
    }

    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        chooser.saveSettingsTo(settings);
        multiFilterComponentPriority.saveSettingsTo(settings);
        multiFilterComponentType.saveSettingsTo(settings);
        multiFilterComponentResolution.saveSettingsTo(settings);
    }

    private class refreshPriorityCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/priority[not(preceding::priority/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshTypeCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/type[not(preceding::type/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshResolutionCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/resolution[not(preceding::resolution/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

}
