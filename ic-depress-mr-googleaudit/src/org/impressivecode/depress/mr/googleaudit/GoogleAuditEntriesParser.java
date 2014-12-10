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

import com.google.common.base.Preconditions;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 * @author Katarzyna Debowa, Wroclaw University of Technology
 * @author Pawel Krzos, Wroclaw University of Technology
 */
public class GoogleAuditEntriesParser {

	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(GoogleAuditEntriesParser.class);
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
				// TODO: remove unecessary type cast to GoogleAuditEntry
				((GoogleAuditEntry) entry).setResourceName(resourceName);
				entry = updateGoogleAuditEntry(auditViolationNodes, entry);
				// TODO: remove unecessary type cast to GoogleAuditEntry
				googleAuditEntriesMap.put(resourceName,
						(GoogleAuditEntry) entry);
			}
		}
		return new ArrayList<GoogleAuditEntry>(googleAuditEntriesMap.values());
	}

	private String getResourceName(Node resource) {
		// TODO: use members constants instead of strings
		String absolutePath = ((Element) resource).getAttribute("path");
		// TODO: use members constants instead of strings
		String[] pathTable = absolutePath.split("/");
		//TODO : 1 is magic number
		String name = pathTable[pathTable.length - 1];
		return name;
	}

	private GoogleAuditEntry updateGoogleAuditEntry(NodeList googleAuditNodes,
			GoogleAuditEntry entry) {
		// TODO : remove unused variable
		String auditViolationType = "";
		 //TODO: change Double to Integer
		Double high = 0.0;
		Double medium = 0.0;
		Double low = 0.0;

		for (int i = 0; i < googleAuditNodes.getLength(); i++) {
			if (googleAuditNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			// TODO: use members constants instead of strings "High", "Medium", "Low"
			// private static final String HIGH = "High";)

			Element elem = (Element) googleAuditNodes.item(i);
			if (!elem.hasAttribute("severity")) {
				continue;
			}
			// TODO : remove unused variable
			auditViolationType = elem.getAttribute("severity");

			try {

				// TODO: what if elem.getAttribute("...") is null
				// change to: HIGH.equals(elem.getAttribute("..."))
				
				// TODO: use members constants instead of strings ("severity")
				
				if ((elem.getAttribute("severity").equals("High")))
					high++;
				if (elem.getAttribute("severity").equals("Medium"))
					medium++;
				if (elem.getAttribute("severity").equals("Low"))
					low++;

				// TODO : What kind of Exception is catch?
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}

		// TODO: change method parameters: setValue(high, medium, low) and give appriopriate name
		entry.setValue("high", high);
		entry.setValue("medium", medium);
		entry.setValue("low", low);
		return entry;
	}

	private NodeList getAllResources() {
		return doc.getElementsByTagName("resource");
	}

	private void init(final String path) throws ParserConfigurationException,
			SAXException, IOException {
		Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(path);

		if (!validXmlStructure()) {
			throw new SAXException("Wrong or malformed file structure.");
		}
	}

	//TODO: validate all xml structure or change name of method
	private boolean validXmlStructure() {
		boolean valid = false;
		NodeList mainNodes = doc.getChildNodes();
		for (int i = 0; i < mainNodes.getLength(); i++) {
			if (mainNodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element elem = (Element) mainNodes.item(i);
			// TODO: use members constants instead of strings ("audit-violation-set")
			if ("audit-violation-set".equals(elem.getTagName())) {
				valid = true;
			}
		}
		return valid;
	}

	private NodeList getAuditViolationSubnodes(Node node) {
		Element elem = (Element) node;
		// TODO: use members constants instead of strings ("audit-violation")
		return elem.getElementsByTagName("audit-violation");
	}

}
