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
package org.impressivecode.depress.metric.checkstyle;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.common.TransformationUtils;
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
public class CheckStyleEntriesParser {
    public List<CheckStyleEntry> parseEntries(final String path) throws ParserConfigurationException, SAXException,
    IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getSourceFileNodes(doc);

        int size = nList.getLength();
        List<CheckStyleEntry> checkstyleEntries = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            if(!isJavaFile(item)){
                continue;
            }
            NodeList cNodes = item.getChildNodes();
            processEntries(checkstyleEntries, cNodes);
        }
        return checkstyleEntries;
    }

    private void processEntries(final List<CheckStyleEntry> checkstyleEntries, final NodeList cNodes) {
        for (int j = 0; j < cNodes.getLength(); j++) {
            Node item2 = cNodes.item(j);
            if (!isError(item2)) {
                continue;
            }
            CheckStyleEntry entry = parse(item2);
            checkstyleEntries.add(entry);
        }
    }

    private boolean isJavaFile(final Node item) {
        String fileName = ((Element)item).getAttribute("name");
        return TransformationUtils.isJavaFile(fileName);
    }

    private NodeList getSourceFileNodes(final Document doc) {
        return doc.getElementsByTagName("file");
    }

    private CheckStyleEntry parse(final Node node) {
        CheckStyleEntry checkstyle = new CheckStyleEntry();
        checkstyle.setFileName(getFileName(node));

        checkstyle.setLineNumber(getLineNumberValue(node));
        checkstyle.setColumnNumber(getColumnNumberValue(node));
        checkstyle.setSeverityType(getSeverityTypeValue(node));
        checkstyle.setMessageText(getMessageTextValue(node));
        checkstyle.setSourcePlace(getSourcePlaceValue(node));

        return checkstyle;
    }

    private boolean isError(final Node item) {
        return item.getNodeName().equals("error");
    }

    private String getLineNumberValue(final Node item) {
        Element elem = (Element) item;
        String line = String.valueOf(elem.getAttribute("line"));
        return line;
    }

    private String getColumnNumberValue(final Node item) {
        Element elem = (Element) item;
        String column = String.valueOf(elem.getAttribute("column"));
        return column;
    }

    private String getSeverityTypeValue(final Node item) {
        Element elem = (Element) item;
        String severity = String.valueOf(elem.getAttribute("severity"));
        return severity;
    }

    private String getMessageTextValue(final Node item) {
        Element elem = (Element) item;
        String message = String.valueOf(elem.getAttribute("message"));
        return message;
    }

    private String getSourcePlaceValue(final Node item) {
        Element elem = (Element) item;
        String source = String.valueOf(elem.getAttribute("source"));
        return source;
    }

    private String getFileName(final Node sourceFile) {
        Node fileNode = sourceFile.getParentNode();
        String fileName = ((Element) fileNode).getAttribute("name");
        return TransformationUtils.filePath2JavaClass(fileName);
    }
}
