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

package org.impressivecode.depress.mr.intellijmetrics;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.google.common.base.Preconditions;

/**
 * 
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 * 
 */
public class IntellijMetricsEntriesParser {

    public List<IntellijMetricsEntry> parseEntries(final String path) throws ParserConfigurationException,
            SAXException, IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");

        Map<String, IntellijMetricsEntry> intellijMetricsEntriesMap;

        if (new File(path).isDirectory())
            intellijMetricsEntriesMap = parseEntriesFromDirectory(path);
        else
            intellijMetricsEntriesMap = parseEntriesFromFile(path);

        return new ArrayList<IntellijMetricsEntry>(intellijMetricsEntriesMap.values());

    }

    private Map<String, IntellijMetricsEntry> parseEntriesFromFile(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        Map<String, IntellijMetricsEntry> intellijMetricsEntriesMap = new LinkedHashMap<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getProblemNodes(doc);
        int size = nList.getLength();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            SimpleEntry entry = parse(item);
            String className = (String) entry.getKey();
            String severity = (String) entry.getValue();

            if (!intellijMetricsEntriesMap.containsKey(className)) {
                IntellijMetricsEntry intellijEntry = new IntellijMetricsEntry();
                intellijEntry.setClassName(className);
                intellijEntry.setValue(severity, 1);
                intellijMetricsEntriesMap.put(className, intellijEntry);
            } else {
                IntellijMetricsEntry existingEntry = intellijMetricsEntriesMap.get(className);
                intellijMetricsEntriesMap.put(className, updateIntellijMetricsEntry(existingEntry, severity));
            }
        }

        return intellijMetricsEntriesMap;
    }

    private Map<String, IntellijMetricsEntry> parseEntriesFromDirectory(final String dir)
            throws ParserConfigurationException, SAXException, IOException {
        Map<String, IntellijMetricsEntry> intellijMetricsEntriesMap = new LinkedHashMap<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        for (String file : listDirectory(dir)) {
            if (new File(file).getName().endsWith(".xml")) {
                Document doc = dBuilder.parse(file);
                NodeList nList = getProblemNodes(doc);
                int size = nList.getLength();
                for (int i = 0; i < size; i++) {
                    Node item = nList.item(i);
                    SimpleEntry entry = parse(item);
                    String className = (String) entry.getKey();
                    String severity = (String) entry.getValue();

                    if (!intellijMetricsEntriesMap.containsKey(className)) {
                        IntellijMetricsEntry intellijEntry = new IntellijMetricsEntry();
                        intellijEntry.setClassName(className);
                        intellijEntry.setValue(severity, 1);
                        intellijMetricsEntriesMap.put(className, intellijEntry);
                    } else {
                        IntellijMetricsEntry existingEntry = intellijMetricsEntriesMap.get(className);
                        intellijMetricsEntriesMap.put(className, updateIntellijMetricsEntry(existingEntry, severity));
                    }
                }
            }
        }

        return intellijMetricsEntriesMap;
    }

    private NodeList getProblemNodes(final Document doc) {
        return doc.getElementsByTagName("problem");
    }

    private SimpleEntry parse(final Node node) {
        NodeList childNodes = node.getChildNodes();
        String className = "";
        String severity = "";

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);

            if (item.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (isClassNode(item)) {
                className = getClassName(item);
                continue;
            }

            if (isProblemClassNode(item)) {
                severity = getSeverit(item);
                continue;
            }
        }

        return new SimpleEntry(className, severity);
    }

    private String getClassName(final Node node) {
        return ((Element) node).getAttribute("FQNAME").replaceAll("(.*?)(\\$[0-9]?[0-9]?$)", "$1");
    }

    private String getSeverit(final Node node) {
        return ((Element) node).getAttribute("severity");
    }

    private boolean isClassNode(final Node node) {
        return ((Element) node).getTagName().equals("entry_point");
    }

    private boolean isProblemClassNode(final Node node) {
        return ((Element) node).getTagName().equals("problem_class");
    }

    private IntellijMetricsEntry updateIntellijMetricsEntry(IntellijMetricsEntry entry, String severity) {
        int currentSeverityValue = entry.getValue(severity);
        entry.setValue(severity, currentSeverityValue + 1);

        return entry;
    }

    private List<String> listDirectory(final String dir) {
        List<String> files = new ArrayList<>();
        listDirectoryRecursively(new File(dir), files);

        return files;
    }

    private void listDirectoryRecursively(File dir, List<String> filesContainer) {
        for (final File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) {
                listDirectoryRecursively(fileEntry, filesContainer);
            } else {
                filesContainer.add(fileEntry.getPath());
            }
        }
    }
}