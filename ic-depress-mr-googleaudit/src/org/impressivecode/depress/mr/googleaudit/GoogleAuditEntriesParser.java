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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 * @author Katarzyna Debowa, Wroclaw University of Technology
 * @author Pawel Krzos, Wroclaw University of Technology
 */
public class GoogleAuditEntriesParser {

	private static final String PATH = "path";
	private static final String DELIMITER = "/";
	private static final String HIGH = "High";
	private static final String MEDIUM = "Medium";
	private static final String LOW = "Low";
	private static final String SEVERITY = "severity";
	private static final String RESOURCE = "resource";
	private static final String AUDIT_VIOLATION_SET = "audit-violation-set";
	private static final String AUDIT_VIOLATION = "audit-violation";
	private Document doc;

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
				entry.setResourceName(resourceName);
				entry = updateGoogleAuditEntry(auditViolationNodes, entry);
				googleAuditEntriesMap.put(resourceName, entry);
			}
		}
		return new ArrayList<GoogleAuditEntry>(googleAuditEntriesMap.values());
	}

	private String getResourceName(Node resource) {
		String absolutePath = ((Element) resource).getAttribute(PATH);
		String[] pathTable = absolutePath.split(DELIMITER);
		String name = pathTable[pathTable.length - 1];
		return name;
	}

	private GoogleAuditEntry updateGoogleAuditEntry(NodeList googleAuditNodes,
			GoogleAuditEntry entry) {

		Double high = 0.0;
		Double medium = 0.0;
		Double low = 0.0;

		for (int i = 0; i < googleAuditNodes.getLength(); i++) {
			if (googleAuditNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element elem = (Element) googleAuditNodes.item(i);
			if (!elem.hasAttribute(SEVERITY)) {
				continue;
			}

			if (HIGH.equals(elem.getAttribute(SEVERITY))) {
				high++;
			} else if (MEDIUM.equals(elem.getAttribute(SEVERITY))) {
				medium++;
			} else if (LOW.equals(elem.getAttribute(SEVERITY))) {
				low++;
			}
		}

		entry.setSeverityValues(high, medium, low);
		return entry;
	}

	private NodeList getAllResources() {
		return doc.getElementsByTagName(RESOURCE);
	}

	private void init(final String path) throws ParserConfigurationException,
			SAXException, IOException {
		Preconditions
				.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(path);

		if (!validXmlRootNode()) {
			throw new SAXException("Wrong or malformed file structure.");
		}
	}

	private boolean validXmlRootNode() {
		boolean valid = false;
		NodeList mainNodes = doc.getChildNodes();
		for (int i = 0; i < mainNodes.getLength(); i++) {
			if (mainNodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element elem = (Element) mainNodes.item(i);
			if (AUDIT_VIOLATION_SET.equals(elem.getTagName())) {
				valid = true;
			}
		}
		return valid;
	}

	private NodeList getAuditViolationSubnodes(Node node) {
		Element elem = (Element) node;
		return elem.getElementsByTagName(AUDIT_VIOLATION);
	}
}