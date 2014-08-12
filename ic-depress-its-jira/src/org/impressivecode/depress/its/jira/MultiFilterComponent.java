package org.impressivecode.depress.its.jira;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.ColumnFilterPanel;

public class MultiFilterComponent {
    private final JPanel panel = new JPanel();

    private final JButton refreshButton;
    private final DialogComponentButtonGroup radioButton;
    private final ColumnFilterPanel filterPanel;
    private final SettingsModelString radioButtonSettings;
    private final String[] radioLabels;
    private final Callable<List<String>> refreshCaller;

    private final Map<String, List<String>> includeLists = new LinkedHashMap<String, List<String>>();

    public JPanel getPanel() {
        return panel;
    }

    public MultiFilterComponent(final SettingsModelString radioButtonSettings,
            final SettingsModelStringArray filterSettings, final String[] radioLabels, Callable<List<String>> refreshCall) {
        this.radioButtonSettings = radioButtonSettings;
        this.radioLabels = radioLabels;
        this.refreshCaller = refreshCall;
        
        JPanel north = new JPanel();

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new RefreshListener());
        north.add(refreshButton);

        radioButton = new DialogComponentButtonGroup(radioButtonSettings, null, false, radioLabels, radioLabels);
        radioButton.getModel().addChangeListener(new RadioButtonChangeListener());
        north.add(radioButton.getComponentPanel());

        filterPanel = new ColumnFilterPanel(false);
        filterPanel.addChangeListener(new FilteringChangeListener());

        panel.add(north, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.CENTER);
    }
    
    private DataTableSpec createTableSpec(List<String> list) {
        DataColumnSpec[] columns = new DataColumnSpec[list.size()];
        int index = 0;
        for (String s : list) {
            columns[index++] = new DataColumnSpecCreator(s, StringCell.TYPE).createSpec();
        }
        return new DataTableSpec(columns);
    }

    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs, List<String> list)
            throws NotConfigurableException {
        radioButton.loadSettingsFrom(settings, specs);
        loadFilter(list);
    }

    public final void loadFilter(List<String> list) throws NotConfigurableException {
        if (null == list) {
            if (includeLists.isEmpty()) {
                list = initLists();
                filterPanel.update(createTableSpec(list), false, Collections.<String> emptySet());
            } else {
                list = new LinkedList<String>();
                list.addAll(filterPanel.getIncludedColumnSet());
                list.addAll(filterPanel.getExcludedColumnSet());
                filterPanel.update(createTableSpec(list), filterPanel.getIncludedColumnSet(),
                        filterPanel.getExcludedColumnSet());
            }
        } else {
            filterPanel.update(createTableSpec(list), false, Collections.<String> emptySet());
        }
    }

    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        radioButton.saveSettingsTo(settings);
        // refactor
        Set<String> set = filterPanel.getIncludedColumnSet();
        settings.addStringArray(JiraAdapterNodeModel.UNKNOWN_CONFIG_NAME, set.toArray(new String[set.size()]));
        List<String> list;
        list = includeLists.get("Blocker");
        settings.addStringArray(JiraAdapterNodeModel.BLOCKER_CONFIG_NAME, list.toArray(new String[list.size()]));
        list = includeLists.get("Critical");
        settings.addStringArray(JiraAdapterNodeModel.CRITICAL_CONFIG_NAME, list.toArray(new String[list.size()]));
        list = includeLists.get("Major");
        settings.addStringArray(JiraAdapterNodeModel.MAJOR_CONFIG_NAME, list.toArray(new String[list.size()]));
        list = includeLists.get("Minor");
        settings.addStringArray(JiraAdapterNodeModel.MINOR_CONFIG_NAME, list.toArray(new String[list.size()]));
        list = includeLists.get("Trivial");
        settings.addStringArray(JiraAdapterNodeModel.TRIVIAL_CONFIG_NAME, list.toArray(new String[list.size()]));
    }

    private List<String> initLists() {
        for (String action : radioLabels) {
            includeLists.put(action, new LinkedList<String>());
        }
        List<String> priorityList = new LinkedList<String>();
        return priorityList;
    }

    private class FilteringChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            List<String> memorizedIncludeList = includeLists.get(radioButtonSettings.getStringValue());
            memorizedIncludeList.clear();
            memorizedIncludeList.addAll(filterPanel.getIncludedColumnSet());
        }
    }

    private class RadioButtonChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            SettingsModelString radioSettings = (SettingsModelString) (event.getSource());
            List<String> newIncludeList = includeLists.get(radioSettings.getStringValue());
            List<String> priorityList = new LinkedList<String>();
            priorityList.addAll(newIncludeList);
            priorityList.addAll(filterPanel.getExcludedColumnSet());
            filterPanel.update(createTableSpec(priorityList), newIncludeList, filterPanel.getExcludedColumnSet());
        }
    }

    private class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            ((JButton) event.getSource()).setText("Refreshing...");
            ((JButton) event.getSource()).setEnabled(false);
            radioButton.getModel().setEnabled(false);
            filterPanel.setEnabled(false);

            List<String> priorities = null;
            try {
                priorities = refreshCaller.call();
            } catch (Exception e1) {
            }
            for (List<String> list : includeLists.values()) {
                list.clear();
            }
            try {
                loadFilter(priorities);
            } catch (NotConfigurableException e) {
            }

            radioButton.getModel().setEnabled(true);
            filterPanel.setEnabled(true);
            ((JButton) event.getSource()).setEnabled(true);
            ((JButton) event.getSource()).setText("Refresh");
        }
    }

}
