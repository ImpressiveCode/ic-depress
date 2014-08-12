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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.impressivecode.depress.its.ITSPriority;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeDialog extends NodeDialogPane {

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.its.jira.historyid";
    private static final SettingsModelString radioButtonSettings = JiraAdapterNodeModel.createRadioSettings();
    
    private DialogComponentFileChooser chooser;
    private MultiFilterComponent multiFilterComponent;

    protected JiraAdapterNodeDialog() {
    	createSettingsTab();
    	createPriorityTab();
    }

	private void createSettingsTab() {
		chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
    	addTab("Settings", chooser.getComponentPanel());
	}
	
    private void createPriorityTab() {
    	multiFilterComponent = new MultiFilterComponent(radioButtonSettings, null, ITSPriority.labels(), new refreshCaller());
        addTab("Priority", multiFilterComponent.getPanel());
	}
    
	private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtansion) {
        return new DialogComponentFileChooser(createFileChooserSettings(), historyId, fileExtansion);
    }

	@Override
    public final void loadSettingsFrom(final NodeSettingsRO settings,
            final PortObjectSpec[] specs) throws NotConfigurableException {
    	chooser.loadSettingsFrom(settings, specs);
    	multiFilterComponent.loadSettingsFrom(settings, specs, null);
    }
    
    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings)
            throws InvalidSettingsException {
    	chooser.saveSettingsTo(settings);
    	multiFilterComponent.saveSettingsTo(settings);
    }
    
    private class refreshCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
            }
            Document doc = null;
            try {
                doc = builder.parse(new File(((SettingsModelString)(chooser.getModel())).getStringValue()));
            } catch (SAXException | IOException e) {
            }
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = null;
            try {
                expr = xpath.compile("/rss/channel/item/priority[not(preceding::priority/. = .)]");
            } catch (XPathExpressionException e1) {
            }
            List<String> priorityList = new LinkedList<String>();
            try {
                NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                for (int i = 0; i < list.getLength(); i++) {
                    priorityList.add(list.item(i).getTextContent());
                }
            } catch (XPathExpressionException e) {
            }
            for (int i = 0; i < ITSPriority.labels().length; i++) {
                priorityList.remove(ITSPriority.labels()[i]);
            }
            return priorityList;
        }
        
    }

}
