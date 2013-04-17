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
package org.impressivecode.depress.metric.eclipsemetrics;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.knime.core.node.NodeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntriesParser {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(EclipseMetricsEntriesParser.class);
    
    public List<EclipseMetricsEntry> parseEntries(final String path) throws ParserConfigurationException, SAXException,
    IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        
        NodeList metricsList = getValuesNodes(doc);
        Map<String,EclipseMetricsEntry> eclipsemetricsEntries = new LinkedHashMap<String,EclipseMetricsEntry>();
        
        for (int i = 0; i < metricsList.getLength(); i++) {
            Node item = metricsList.item(i);

            if (!isPerType(item)) {
                continue;
            }
            
            String metricId = ((Element) item.getParentNode()).getAttribute("id");
            Map<String,Double> classesAndValues = parseMetricNode(item);
            for (Map.Entry<String,Double> data : classesAndValues.entrySet()) {
                String className = data.getKey();
                Double metricValue = data.getValue();
                if (eclipsemetricsEntries.containsKey(className)) {
                    EclipseMetricsEntry entry = eclipsemetricsEntries.get(className);
                    entry.setValue(metricId, metricValue);
                } else {
                    EclipseMetricsEntry entry = new EclipseMetricsEntry();
                    entry.setClassName(className);
                    entry.setValue(metricId, metricValue);
                    eclipsemetricsEntries.put(className, entry);
                }
            }
        }
        return new ArrayList<EclipseMetricsEntry>(eclipsemetricsEntries.values());
    }
    
    private boolean isPerType(final Node item) {
        return ((Element) item).getAttribute("per").equals("type");
    }
    
    private NodeList getValuesNodes(final Document doc) {
        return doc.getElementsByTagName("Values");
    }
    
    private Map<String,Double> parseMetricNode(Node node) {
        Map<String,Double> values = new LinkedHashMap<String,Double>();
        NodeList nodeList = node.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            Node childItem = nodeList.item(i);
            if (childItem.getNodeName().equals("Value")) {
                Element elem = (Element) childItem;
                String name = elem.getAttribute("name");
                String packageName = elem.getAttribute("package");
                //TODO decide how represent internal static classes
                String className = "(default package)".equals(packageName) ? name : packageName+"."+name;
                try {
                    Double value = Double.parseDouble(elem.getAttribute("value").replace(",", "."));
                    values.put(className, value);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return values;
    }
}
