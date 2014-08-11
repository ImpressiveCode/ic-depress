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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.ColumnFilterPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeDialog extends NodeDialogPane {

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.its.jira.historyid";
    private static final String[] actions = {"Trivial", "Minor", "Major", "Critical", "Blocker"};
    private static final SettingsModelString radioButtonSettings = JiraAdapterNodeModel.createRadioSettings();
    
    private DialogComponentFileChooser chooser;
    private DialogComponentButtonGroup radioButton;
    private ColumnFilterPanel filterPanel;
    
    private DataTableSpec priorityTable;
    private final Map<String, List<String>> actionsIncludeList = new LinkedHashMap<String, List<String>>();

    protected JiraAdapterNodeDialog() {
    	createSettingsTab();
    	createPriorityTab();
    }

	private void createSettingsTab() {
		chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
    	addTab("Settings", chooser.getComponentPanel());
	}
	
    private void createPriorityTab() {
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JButton refreshButton = new JButton("Refresh");
    	refreshButton.addActionListener(new RefreshListener());
    	radioButton = new DialogComponentButtonGroup(radioButtonSettings, null, false, actions, actions);
    	radioButton.getModel().addChangeListener(new RadioButtonChangeListener());
    	north.add(refreshButton);
    	north.add(radioButton.getComponentPanel());
        panel.add(north, BorderLayout.NORTH);
        filterPanel = new ColumnFilterPanel(false);
        filterPanel.addChangeListener(new FilteringChangeListener());
        panel.add(filterPanel, BorderLayout.CENTER);
        addTab("Priority", panel);
	}
    
	private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtansion) {
        return new DialogComponentFileChooser(createFileChooserSettings(), historyId, fileExtansion);
    }

	@Override
    public final void loadSettingsFrom(final NodeSettingsRO settings,
            final PortObjectSpec[] specs) throws NotConfigurableException {
    	chooser.loadSettingsFrom(settings, specs);
    	radioButton.loadSettingsFrom(settings, specs);
    	loadMultiFilterSettings();
    }

    @SuppressWarnings("unchecked")
	private void loadMultiFilterSettings() {
    	List<String> priorityList = null;
    	if(actionsIncludeList.isEmpty()) {
			priorityList = parsePriorities();
            priorityTable = createTableSpec(priorityList);
            filterPanel.update(priorityTable, false, Collections.EMPTY_SET);
    	} else {
    		priorityList = new LinkedList<String>();
    		priorityList.addAll(filterPanel.getIncludedColumnSet());
    		priorityList.addAll(filterPanel.getExcludedColumnSet());
            priorityTable = createTableSpec(priorityList);
            filterPanel.update(priorityTable, filterPanel.getIncludedColumnSet(), filterPanel.getExcludedColumnSet());
    	}
	}
    private List<String> parsePriorities() {
    	for(String action : actions) {
    		actionsIncludeList.put(action, new LinkedList<String>());
    	}
    	List<String> priorityList = new LinkedList<String>();
        priorityList.add("one");
        priorityList.add("two");
        priorityList.add("three");
        priorityList.add("four");
        priorityList.add("five");
        return priorityList;
    }
    
	private List<String> parsePrioritiesFromFile() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {}
        Document doc = null;
		try {
			System.out.println("Looking at: " + ((SettingsModelString)(chooser.getModel())).getStringValue());
			doc = builder.parse(new File(((SettingsModelString)(chooser.getModel())).getStringValue()));
		} catch (SAXException | IOException e) {
		}
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
		try {
			expr = xpath.compile("/rss/channel/item/priority[not(preceding::priority/. = .)]");
		} catch (XPathExpressionException e1) {}
		List<String> priorityList = new LinkedList<String>();
        try {
			NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
			    Node node = list.item(i);
			    priorityList.add(node.getTextContent());
			    System.out.println(node.getTextContent());
			}
		} catch (XPathExpressionException e) {} 
        return priorityList;
    }
    
    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings)
            throws InvalidSettingsException {
    	chooser.saveSettingsTo(settings);
    	radioButton.saveSettingsTo(settings);
    	saveMultiFilterSettings(settings);
    }
    
    private void saveMultiFilterSettings(final NodeSettingsWO settings) {
    	//refactor
    	Set<String> set = filterPanel.getIncludedColumnSet();
    	settings.addStringArray(JiraAdapterNodeModel.UNKNOWN_CONFIG_NAME, set.toArray(new String[set.size()]));
    	List<String> list;
    	list = actionsIncludeList.get("Blocker");
    	settings.addStringArray(JiraAdapterNodeModel.BLOCKER_CONFIG_NAME, list.toArray(new String[list.size()]));
    	list = actionsIncludeList.get("Critical");
    	settings.addStringArray(JiraAdapterNodeModel.CRITICAL_CONFIG_NAME, list.toArray(new String[list.size()]));
    	list = actionsIncludeList.get("Major");
    	settings.addStringArray(JiraAdapterNodeModel.MAJOR_CONFIG_NAME, list.toArray(new String[list.size()]));
    	list = actionsIncludeList.get("Minor");
    	settings.addStringArray(JiraAdapterNodeModel.MINOR_CONFIG_NAME, list.toArray(new String[list.size()]));
    	list = actionsIncludeList.get("Trivial");
    	settings.addStringArray(JiraAdapterNodeModel.TRIVIAL_CONFIG_NAME, list.toArray(new String[list.size()]));
	}

	private DataTableSpec createTableSpec(List<String> list) {
		DataColumnSpec[] columns = new DataColumnSpec[list.size()];
		int index = 0;
        for (String s : list) {
        	columns[index++] = new DataColumnSpecCreator(s, StringCell.TYPE).createSpec();
        }
        return new DataTableSpec(columns);
    }
    
    private class FilteringChangeListener implements ChangeListener {
    	@Override
 		public void stateChanged(ChangeEvent event) {
 			List<String> memorizedIncludeList = actionsIncludeList.get(radioButtonSettings.getStringValue());
 			memorizedIncludeList.clear();
 			memorizedIncludeList.addAll(filterPanel.getIncludedColumnSet());
 		}
 	}
    
    private class RadioButtonChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent event) {
			SettingsModelString radioSettings = (SettingsModelString)(event.getSource());
			List<String> newIncludeList = actionsIncludeList.get(radioSettings.getStringValue());
			List<String> priorityList = new LinkedList<String>();
			priorityList.addAll(newIncludeList);
			priorityList.addAll(filterPanel.getExcludedColumnSet());
			priorityTable = createTableSpec(priorityList);
 			filterPanel.update(priorityTable, newIncludeList, filterPanel.getExcludedColumnSet());
 		}
 	}
    
    private class RefreshListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			((JButton)event.getSource()).setText("Refreshing...");
			((JButton)event.getSource()).setEnabled(false);
			radioButton.getModel().setEnabled(false);
			filterPanel.setEnabled(false);
			
			parsePrioritiesFromFile();
	        
	        radioButton.getModel().setEnabled(true);
	        filterPanel.setEnabled(true);
	        ((JButton)event.getSource()).setEnabled(true);
	        ((JButton)event.getSource()).setText("Refresh");
		}
    }
}
