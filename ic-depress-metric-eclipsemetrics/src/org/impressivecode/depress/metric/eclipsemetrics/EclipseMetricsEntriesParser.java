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

        NodeList metricsList = getAllMetricsNodes(doc);
        Map<String, EclipseMetricsEntry> eclipsemetricsEntriesMap = new LinkedHashMap<String, EclipseMetricsEntry>();

        for (int i = 0; i < metricsList.getLength(); i++) {
            Node metricNode = metricsList.item(i);

            if (!isNodePerType(metricNode)) {
                continue;
            }
            updateEclipseMetricsEntriesMap(eclipsemetricsEntriesMap, metricNode);
        }
        return new ArrayList<EclipseMetricsEntry>(eclipsemetricsEntriesMap.values());
    }

    private void updateEclipseMetricsEntriesMap(Map<String, EclipseMetricsEntry> eclipsemetricsEntriesMap,
            Node metricNode) {
        String metricId = parseMetricId(metricNode);
        Map<String, Double> metricClassesAndValuesMap = parseMetricNode(metricNode);
        for (Map.Entry<String, Double> data : metricClassesAndValuesMap.entrySet()) {
            String className = data.getKey();
            Double metricValue = data.getValue();
            if (eclipsemetricsEntriesMap.containsKey(className)) {
                EclipseMetricsEntry entry = eclipsemetricsEntriesMap.get(className);
                entry.setValue(metricId, metricValue);
            } else {
                EclipseMetricsEntry entry = new EclipseMetricsEntry();
                entry.setClassName(className);
                entry.setValue(metricId, metricValue);
                eclipsemetricsEntriesMap.put(className, entry);
            }
        }
    }

    private boolean isNodePerType(final Node item) {
        return ((Element) item).getAttribute("per").equals("type");
    }

    private NodeList getAllMetricsNodes(final Document doc) {
        return doc.getElementsByTagName("Values");
    }

    private String parseMetricId(Node item) {
        return ((Element) item.getParentNode()).getAttribute("id");
    }

    private Map<String, Double> parseMetricNode(Node metricNode) {
        Map<String, Double> values = new LinkedHashMap<String, Double>();
        NodeList childNodeList = metricNode.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node childItem = childNodeList.item(i);
            if (childItem.getNodeName().equals("Value")) {
                Element elem = (Element) childItem;
                String name = elem.getAttribute("name");
                String source = elem.getAttribute("source").replace(".java", "");
                String packageName = elem.getAttribute("package");
                String className = "(default package)".equals(packageName) ? name : packageName + "." + source;
                if (!name.equals(source)) {
                    className += "$" + name;
                }
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
