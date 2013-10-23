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
package org.impressivecode.depress.metric.pmd;

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
 * @author Tomasz Banach
 * @author �ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDEntriesParser {
    public List<PMDEntry> parseEntries(final String path) throws
    IOException, ParserConfigurationException, SAXException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getSourceFileNodes(doc);
        int size = nList.getLength();
        List<PMDEntry> pmdEntries = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            NodeList cNodes = item.getChildNodes();
            for (int j = 0; j < cNodes.getLength(); j++) {
                Node item2 = cNodes.item(j);
                if (!isError(item2)) {
                    continue;
                }
                PMDEntry entry = parse(item2);
                pmdEntries.add(entry);
            }
        }
        return pmdEntries;
    }

    private NodeList getSourceFileNodes(final Document doc) {
        return doc.getElementsByTagName("file");
    }

    private PMDEntry parse(final Node node) {
        PMDEntry pmd = new PMDEntry();
        pmd.setClassName(getClassName(node));
        pmd.setBeginLine(getBeginLineValue(node));
        pmd.setEndLine(getEndLineValue(node));
        pmd.setBeginColumn(getBeginColumnValue(node));
        pmd.setEndColumn(getEndColumnValue(node));
        pmd.setRule(getRuleValue(node));
        pmd.setMessageText(getMessageTextValue(node));
        pmd.setRuleSet(getRuleSetValue(node));
        pmd.setInfoUrl(getInfoUrlValue(node));
        pmd.setPriority(getPriorityValue(node));

        return pmd;
    }

    private boolean isError(final Node item) {
        return item.getNodeName().equals("violation");
    }

    private String getBeginLineValue(final Node item) {
        Element elem = (Element) item;
        String line = String.valueOf(elem.getAttribute("beginline"));
        return line;
    }

    private String getEndLineValue(final Node item) {
        Element elem = (Element) item;
        String endLine = String.valueOf(elem.getAttribute("endline"));
        return endLine;
    }

    private String getBeginColumnValue(final Node item) {
        Element elem = (Element) item;
        String bgnColumn = String.valueOf(elem.getAttribute("begincolumn"));
        return bgnColumn;
    }

    private String getEndColumnValue(final Node item) {
        Element elem = (Element) item;
        String endColumn = String.valueOf(elem.getAttribute("endcolumn"));
        return endColumn;
    }

    private String getRuleValue(final Node item) {
        Element elem = (Element) item;
        String rule = String.valueOf(elem.getAttribute("rule"));
        return rule;
    }

    private String getMessageTextValue(final Node item) {
        Node node = item;
        //String message = String.valueOf(elem.getAttribute("message"));
        String message = String.valueOf(node.getFirstChild().getTextContent());
        return message;
    }

    private String getRuleSetValue(final Node item) {
        Element elem = (Element) item;
        String ruleSet = String.valueOf(elem.getAttribute("ruleset"));
        return ruleSet;
    }

    private String getPackageNameValue(final Node item) {
        Element elem = (Element) item;
        String packageName = String.valueOf(elem.getAttribute("package"));
        return packageName;
    }

    private String getClassName(final Node item) {
        Element elem = (Element) item;
        String className = String.valueOf(elem.getAttribute("class"));
        return getPackageNameValue(item) + "." + className;
    }

    private String getInfoUrlValue(final Node item) {
        Element elem = (Element) item;
        String infoUrl = String.valueOf(elem.getAttribute("externalInfoUrl"));
        return infoUrl;
    }

    private String getPriorityValue(final Node item) {
        Element elem = (Element) item;
        String priority = String.valueOf(elem.getAttribute("priority"));
        return priority;
    }
}
