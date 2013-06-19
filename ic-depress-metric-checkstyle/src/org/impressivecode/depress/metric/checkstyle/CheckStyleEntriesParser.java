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
            NodeList cNodes = item.getChildNodes();
            for (int j = 0; j < cNodes.getLength(); j++) {
                Node item2 = cNodes.item(j);
                if (!isError(item2)) {
                    continue;
                }
	            CheckStyleEntry entry = parse(item2);
	            checkstyleEntries.add(entry);
            }
        }
        return checkstyleEntries;
    }

    private NodeList getSourceFileNodes(final Document doc) {
        return doc.getElementsByTagName("file");
    }

    private CheckStyleEntry parse(final Node node) {
        //NodeList cNodes = node.getChildNodes();
        //List<CheckStyleEntry> checkstyleEntries = Lists.newLinkedList();
        CheckStyleEntry checkstyle = new CheckStyleEntry();
        checkstyle.setFileName(getFileName(node));

        //for (int i = 0; i < cNodes.getLength(); i++) {
          //  Node item = cNodes.item(i);

            /*if (!isError(node)) {
                continue;
            }*/
            
            /*Element elem = (Element) node;
            Double line = Double.valueOf(elem.getAttribute("line"));
            checkstyle.setLineNumber(line);*/
            
            //if (isLineNumber(node)) {
                checkstyle.setLineNumber(getLineNumberValue(node));
                //continue;
            //}
            
            //if (isColumnNumber(node)) {
            	checkstyle.setColumnNumber(getColumnNumberValue(node));
            	//continue;
            //}
                
            //if (isSourcePlace(node)) {
                checkstyle.setSeverityType(getSeverityTypeValue(node));
                //continue;
            //}

            //if (isSeverityType(node)) {
                checkstyle.setMessageText(getMessageTextValue(node));
                //continue;
            //}

            //if (isMessageText(node)) {
                checkstyle.setSourcePlace(getSourcePlaceValue(node));
                //continue;
            //}

            /*if (isComplexityCoverageCounter(item)) {
                checkstyle.setComplexityCoverageCounter(getCoverageCounterValue(item));
                continue;
            }*/

            

            /*if (isClassCoverageCounter(item)) {
                checkstyle.setClassCoverageCounter(getCoverageCounterValue(item));
                continue;
            }*/
        return checkstyle;
    }

    private boolean isError(final Node item) {
        return item.getNodeName().equals("error");
    }

    private String getLineNumberValue(final Node item) {
        Element elem = (Element) item;
        String line = String.valueOf(elem.getAttribute("line"));
        //Double missed = Double.valueOf(elem.getAttribute("missed"));
        return line;
    }
    
    private String getColumnNumberValue(final Node item) {
        Element elem = (Element) item;
        String column = String.valueOf(elem.getAttribute("column"));
        //Double missed = Double.valueOf(elem.getAttribute("missed"));
        return column;
    }
    
    private String getSeverityTypeValue(final Node item) {
        Element elem = (Element) item;
        String severity = String.valueOf(elem.getAttribute("severity"));
        //Double missed = Double.valueOf(elem.getAttribute("missed"));
        return severity;
    }
    
    private String getMessageTextValue(final Node item) {
        Element elem = (Element) item;
        String message = String.valueOf(elem.getAttribute("message"));
        //Double missed = Double.valueOf(elem.getAttribute("missed"));
        return message;
    }
    
    private String getSourcePlaceValue(final Node item) {
        Element elem = (Element) item;
        String source = String.valueOf(elem.getAttribute("source"));
        //Double missed = Double.valueOf(elem.getAttribute("missed"));
        return source;
    }

    /*private boolean isLineNumber(final Node node) {
        return ((Element) node).getAttribute("type").equals("line");
    }*/

    /*private boolean isSeverityType(final Node node) {
        return ((Element) node).getAttribute("type").equals("severity");
    }*/

    /*private boolean isMessageText(final Node node) {
        return ((Element) node).getAttribute("type").equals("message");
    }*/

    /*private boolean isSourcePlace(final Node node) {
        return ((Element) node).getAttribute("type").equals("source");
    }*/

    /*private boolean isMethodCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("METHOD");
    }*/

    /*private boolean isClassCoverageCounter(final Node node) {
        return ((Element) node).getAttribute("type").equals("CLASS");
    }*/

    private String getFileName(final Node sourceFile) {
        Node packageNode = sourceFile.getParentNode();
        String packageName = ((Element) packageNode).getAttribute("name");//.replace('/', '.');
        //String fileName = ((Element) sourceFile).getAttribute("name").replace(".java", "");
        return packageName;// + "." + fileName;
    }
}
