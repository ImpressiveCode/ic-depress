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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.mr.googleaudit.GoogleAuditXmlResult.Resource;
import org.knime.core.node.NodeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 */
public class GoogleAuditEntriesParser {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(GoogleAuditEntriesParser.class);
    private Document doc;

/*	public static List<Resource> unmarshalResults(final String path) throws JAXBException {
	        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
	        JAXBContext jaxbContext = JAXBContext.newInstance(GoogleAuditXmlResult.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

	        GoogleAuditXmlResult result = (GoogleAuditXmlResult) unmarshaller.unmarshal(new File(path));
	        return result.getResource();
	    }
*/	
    public List<GoogleAuditEntry> parseEntries(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        init(path);

        Map<String, GoogleAuditEntry> googleAuditEntriesMap = new LinkedHashMap<String, GoogleAuditEntry>();
        NodeList resourceList = getAllResources();
        for (int i = 0; i < resourceList.getLength(); i++) {
            Node resourceNode = resourceList.item(i);
            String resourceName = getResourceName(resourceNode);
            NodeList auditViolationNodes = getAuditViolationSubnodes(resourceNode);

            if (!googleAuditEntriesMap.containsKey(resourceName)) {
                GoogleAuditEntry entry = new GoogleAuditEntry();
                ((GoogleAuditEntry) entry).setResourceName(resourceName);
                entry = updateGoogleAuditEntry(auditViolationNodes, entry);
                googleAuditEntriesMap.put(resourceName, (GoogleAuditEntry) entry);
            }
        }
        return new ArrayList<GoogleAuditEntry>(googleAuditEntriesMap.values());
    }

    private String getResourceName(Node resource) {
        String absolutePath = ((Element) resource).getAttribute("path");
        String[] pathTable = absolutePath.split("/");
        String name = pathTable[pathTable.length-1];
        return name;
    }

    private GoogleAuditEntry updateGoogleAuditEntry(NodeList googleAuditNodes, GoogleAuditEntry entry) {
    	String auditViolationType = "";
    	Double high = 0.0;
    	Double medium = 0.0;
    	Double low = 0.0;

    	for (int i = 0; i < googleAuditNodes.getLength(); i++) {
            if (googleAuditNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element elem = (Element) googleAuditNodes.item(i);
            if (!elem.hasAttribute("severity")) {
                continue;
            }
            auditViolationType = elem.getAttribute("severity");
            try {
                if(elem.getAttribute("severity").equals("High"))
                	high++;
                if(elem.getAttribute("severity").equals("Medium"))
                	medium++;
                if(elem.getAttribute("severity").equals("Low"))
                	low++;
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        entry.setValue("high", high);
        entry.setValue("medium", medium);
        entry.setValue("low", low);
        return entry;
    }

    private NodeList getAllResources() {
        return doc.getElementsByTagName("resource");
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
    	boolean valid=false;
        NodeList mainNodes = doc.getChildNodes();
        for (int i = 0; i < mainNodes.getLength(); i++) {
            if (mainNodes.item(i).getNodeType() != Node.ELEMENT_NODE)
                continue;
            Element elem = (Element) mainNodes.item(i);
            if ("audit-violation-set".equals(elem.getTagName())) {
                valid = true;
            }
        }
        return valid;
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

    private NodeList getAuditViolationSubnodes(Node node) {
        Element elem = (Element) node;
        return elem.getElementsByTagName("audit-violation");
    }

}
