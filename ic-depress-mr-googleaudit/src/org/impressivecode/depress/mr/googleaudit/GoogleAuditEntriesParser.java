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
package org.impressivecode.depress.mr.googleaudit;

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
public class GoogleAuditEntriesParser {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(GoogleAuditEntriesParser.class);
    private Document doc;

    public List<GoogleAuditEntryMethodLevel> parseEntriesMethodLevel(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        init(path);

        Map<String, GoogleAuditEntryMethodLevel> eclipsemetricsEntriesMap = new LinkedHashMap<String, GoogleAuditEntryMethodLevel>();
        NodeList methodList = getAllMethodNodes();
        for (int i = 0; i < methodList.getLength(); i++) {
            Node methodNode = methodList.item(i);
            String methodName = getMethodName(methodNode);
            NodeList metricNodes = getMetricSubnodes(methodNode);

            if (!eclipsemetricsEntriesMap.containsKey(methodName)) {
                GoogleAuditEntry entry = new GoogleAuditEntryMethodLevel();
                ((GoogleAuditEntryMethodLevel) entry).setMethodName(methodName);
                entry = updateEclipseMetricsEntry(metricNodes, entry);
                eclipsemetricsEntriesMap.put(methodName, (GoogleAuditEntryMethodLevel) entry);
            }
        }
        return new ArrayList<GoogleAuditEntryMethodLevel>(eclipsemetricsEntriesMap.values());
    }

    private String getMethodName(Node methodNode) {
        String subjectString = ((Element) methodNode).getAttribute("handle");

        String packageName = extractPackageName(subjectString);

        String name1 = "";
        Pattern regexName1 = Pattern.compile("(?<=\\.java\\[).+");
        Matcher regexMatcher = regexName1.matcher(subjectString);
        while (regexMatcher.find()) {
            name1 = regexMatcher.group();
        }
        String methodName = packageName + "." + name1;

        return methodName;
    }

    public List<GoogleAuditEntryClassLevel> parseEntriesClassLevel(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        init(path);

        Map<String, GoogleAuditEntryClassLevel> eclipsemetricsEntriesMap = new LinkedHashMap<String, GoogleAuditEntryClassLevel>();
        NodeList classList = getAllTypeNodes();
        for (int i = 0; i < classList.getLength(); i++) {
            Node typeNode = classList.item(i);
            String className = getClassName(typeNode);
            NodeList metricNodes = getMetricSubnodes(typeNode);

            if (!eclipsemetricsEntriesMap.containsKey(className)) {
                GoogleAuditEntry entry = new GoogleAuditEntryClassLevel();
                ((GoogleAuditEntryClassLevel) entry).setClassName(className);
                entry = updateEclipseMetricsEntry(metricNodes, entry);
                eclipsemetricsEntriesMap.put(className, (GoogleAuditEntryClassLevel) entry);
            }
        }
        return new ArrayList<GoogleAuditEntryClassLevel>(eclipsemetricsEntriesMap.values());
    }

    private GoogleAuditEntry updateEclipseMetricsEntry(NodeList metricNodes, GoogleAuditEntry entry) {
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

    private NodeList getAllMethodNodes() {
        return doc.getElementsByTagName("Method");
    }

    private void init(final String path) throws ParserConfigurationException, SAXException, IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(path);

        if (!validXmlStructure()) {
            throw new SAXException("Wrong or malformed file structure.");
        }
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

        String packageName = extractPackageName(subjectString);

        String name1 = "";
        Pattern regexName1 = Pattern.compile("(?<=\\.java\\[).+");
        Matcher regexMatcher = regexName1.matcher(subjectString);
        while (regexMatcher.find()) {
            name1 = regexMatcher.group();
        }
        name1 = name1.replace("[", "$");
        String className = packageName + "." + name1;

        return className;
    }

    private String extractPackageName(String subjectString) {
        Pattern regexPackageName = Pattern.compile("(?<=<).+(?=\\{)");
        Matcher regexMatcher = regexPackageName.matcher(subjectString);
        String packageName = "";
        while (regexMatcher.find()) {
            packageName = regexMatcher.group();
        }
        return packageName;
    }

    private NodeList getMetricSubnodes(Node node) {
        Element elem = (Element) node;
        return elem.getElementsByTagName("Metric");
    }
}
