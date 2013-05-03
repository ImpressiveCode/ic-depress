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
import java.util.regex.*;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntriesParser {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(EclipseMetricsEntriesParser.class);
    private Document doc;
    
    public List<EclipseMetricsEntryMethodLevel> parseEntriesMethodLevel(final String path) throws ParserConfigurationException, SAXException,
    IOException {
        return new ArrayList<EclipseMetricsEntryMethodLevel>();
    }

    public List<EclipseMetricsEntry> parseEntriesClassLevel(final String path) throws ParserConfigurationException, SAXException,
            IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(path);

        if (!validXmlStructure()) {
            throw new SAXException("Wrong or malformed file structure.");
        }

        Map<String, EclipseMetricsEntry> eclipsemetricsEntriesMap = new LinkedHashMap<String, EclipseMetricsEntry>();
        NodeList classList = getAllTypeNodes();
        for (int i = 0; i < classList.getLength(); i++) {
            Node typeNode = classList.item(i);
            String className = getClassName(typeNode);
            NodeList metricNodes = getMetricSubnodes(typeNode);

            if (!eclipsemetricsEntriesMap.containsKey(className)) {
                EclipseMetricsEntry entry = new EclipseMetricsEntry();
                entry.setClassName(className);
                entry = updateEclipseMetricsEntry(metricNodes, entry);
                eclipsemetricsEntriesMap.put(className, entry);
            }
        }
        return new ArrayList<EclipseMetricsEntry>(eclipsemetricsEntriesMap.values());
    }

    private boolean validXmlStructure() {
        NodeList mainNodes = doc.getChildNodes();
        for (int i = 0; i < mainNodes.getLength(); i++) {
            Element elem = (Element) mainNodes.item(i);
            if ("Metrics".equals(elem.getTagName())) {
                return true;
            }
        }
        return false;
    }

    private NodeList getAllTypeNodes() {
        return doc.getElementsByTagName("Type");
    }

    private String getClassName(Node typeNode) {
        String subjectString = ((Element) typeNode).getAttribute("handle");
        String className = "";

        Pattern regexPackageName = Pattern.compile("(?<=<).+(?=\\{)");
        Matcher regexMatcher = regexPackageName.matcher(subjectString);
        while (regexMatcher.find()) {
            className += regexMatcher.group();
        }

        String name1 = "";
        Pattern regexName1 = Pattern.compile("(?<=\\.java\\[).+");
        regexMatcher = regexName1.matcher(subjectString);
        while (regexMatcher.find()) {
            name1 = regexMatcher.group();
        }
        name1 = name1.replace("[", "$");
        className += "." + name1;

        return className;
    }

    private NodeList getMetricSubnodes(Node typeNode) {
        Element elem = (Element) typeNode;
        return elem.getElementsByTagName("Metric");
    }

    private EclipseMetricsEntry updateEclipseMetricsEntry(NodeList metricNodes, EclipseMetricsEntry entry) {
        for (int i = 0; i < metricNodes.getLength(); i++) {
            if (metricNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element elem = (Element) metricNodes.item(i);
            if (!elem.hasAttribute("value")) {
                continue;
            }
            String metricId = elem.getAttribute("id");
            try {
                Double metricValue = Double.parseDouble(elem.getAttribute("value").replace(",", "."));
                entry.setValue(metricId, metricValue);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return entry;
    }
}
