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
package org.impressivecode.depress.mr.jacoco;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoEntriesParser {
    public List<JaCoCoEntry> parseEntries(final String path) throws ParserConfigurationException, SAXException,
    IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getSourceFileNodes(doc);
        int size = nList.getLength();
        List<JaCoCoEntry> jacocoEntries = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            JaCoCoEntry entry = parse(item);
            jacocoEntries.add(entry);
        }
        return jacocoEntries;
    }

    private NodeList getSourceFileNodes(final Document doc) {
        return doc.getElementsByTagName("sourcefile");
    }

    private JaCoCoEntry parse(final Node node) {
        NodeList cNodes = node.getChildNodes();
        JaCoCoEntry jacoco = new JaCoCoEntry();
        jacoco.setClassName(getClassName(node));

        for (int i = 0; i < cNodes.getLength(); i++) {
            Node item = cNodes.item(i);

            if (!isCounter(item)) {
                continue;
            }

            if (isLineCoverageCounter(item)) {
                jacoco.setLineCoverageCounter(getCoverageCounterValue(item));
                continue;
            }

            if (isInstructionCoverageCounter(item)) {
                jacoco.setInstructionCoverageCounter(getCoverageCounterValue(item));
                continue;
            }

            if (isBranchCoverageCounter(item)) {
                jacoco.setBranchCoverageCounter(getCoverageCounterValue(item));
                continue;
            }

            if (isComplexityCoverageCounter(item)) {
                jacoco.setComplexityCoverageCounter(getCoverageCounterValue(item));
                continue;
            }

            if (isMethodCoverageCounter(item)) {
                jacoco.setMethodCoverageCounter(getCoverageCounterValue(item));
                continue;
            }

            if (isClassCoverageCounter(item)) {
                jacoco.setClassCoverageCounter(getCoverageCounterValue(item));
                continue;
            }
        }
        return jacoco;
    }

    private boolean isCounter(final Node item) {
        return item.getNodeName().equals("counter");
    }

    private double getCoverageCounterValue(final Node item) {
        Element elem = (Element) item;
        Double covered = Double.valueOf(elem.getAttribute("covered"));
        Double missed = Double.valueOf(elem.getAttribute("missed"));
        return covered / (missed + covered);
    }

    private boolean isLineCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("LINE");
    }

    private boolean isInstructionCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("INSTRUCTION");
    }

    private boolean isBranchCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("BRANCH");
    }

    private boolean isComplexityCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("COMPLEXITY");
    }

    private boolean isMethodCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("METHOD");
    }

    private boolean isClassCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("CLASS");
    }

    private String getClassName(final Node sourceFile) {
        Node packageNode = sourceFile.getParentNode();
        String packageName = ((Element) packageNode).getAttribute("name").replace('/', '.');
        String fileName = ((Element) sourceFile).getAttribute("name").replace(".java", "");
        return packageName + "." + fileName;
    }
}
