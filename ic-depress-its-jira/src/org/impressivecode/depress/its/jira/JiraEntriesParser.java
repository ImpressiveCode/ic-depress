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
package org.impressivecode.depress.its.jira;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
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
public class JiraEntriesParser {
    private static final String JIRA_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public List<ITSDataType> parseEntries(final String path) throws ParserConfigurationException, SAXException,
    IOException, ParseException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getItemNodes(doc);
        int size = nList.getLength();
        List<ITSDataType> entries = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            ITSDataType entry = parse((Element) item);
            entries.add(entry);
        }
        return entries;
    }

    private NodeList getItemNodes(final Document doc) {
        return doc.getElementsByTagName("item");
    }

    private ITSDataType parse(final Element elem) throws ParseException {
        ITSDataType data = new ITSDataType();
        data.setIssueId(getKey(elem));
        data.setComments(getComments(elem));
        data.setCreated(getCreated(elem));
        data.setDescription(getDescription(elem));
        data.setFixVersion(getFixVersion(elem));
        data.setLink(getLink(elem));
        data.setPriority(getPriority(elem));
        data.setResolved(getResolved(elem));
        data.setStatus(getStatus(elem));
        data.setSummary(getSummary(elem));
        data.setType(getType(elem));
        data.setUpdated(getUpdated(elem));
        data.setVersion(getVersion(elem));
        data.setResolution(getResolution(elem));
        return data;
    }

    private ITSResolution getResolution(final Element elem) {
        String resolution = extractValue(elem, "resolution");
        switch (resolution) {
        case "Unresolved":
            return ITSResolution.UNRESOLVED;
        case "Fixed":
            return ITSResolution.FIXED;
        case "Wont't Fix":
            return ITSResolution.WONT_FIX;
        case "Duplicate":
            return ITSResolution.DUPLICATE;
        case "Invalid":
            return ITSResolution.INVALID;
        case "Incomplete":
            return ITSResolution.INVALID;
        case "Cannot Reproduce":
            return ITSResolution.WONT_FIX;
        case "Later":
            return ITSResolution.WONT_FIX;
        case "Not A Problem":
            return ITSResolution.WONT_FIX;
        case "Implemented":
            return ITSResolution.IMPLEMENTED;
        default:
            return ITSResolution.UNKNOWN;
        }
    }

    private List<String> getVersion(final Element elem) {
        return extractValues(elem, "version");
    }

    private Date getUpdated(final Element elem) throws ParseException {
        return extractDateValue(elem, "updated");
    }

    private ITSType getType(final Element elem) {
        String type = extractValue(elem, "type");
        switch (type) {
        case "Bug":
            return ITSType.BUG;
        case "Test":
            return ITSType.TEST;
        case "Improvement":
        case "New Feature":
        case "Task":
        case "Wish":
            return ITSType.ENHANCEMENT;
        default:
            return ITSType.UNKNOWN;
        }
    }

    private String getSummary(final Element elem) {
        return extractValue(elem, "summary");
    }

    private ITSStatus getStatus(final Element elem) {
        String status = extractValue(elem, "status");
        switch (status) {
        case "Open":
            return ITSStatus.OPEN;
        case "Reopen":
            return ITSStatus.REOPEN;
        case "In Progress":
            return ITSStatus.IN_PROGRESS;
        case "Resolved":
            return ITSStatus.RESOLVED;
        case "Closed":
            return ITSStatus.CLOSED;
        default:
            return ITSStatus.UNKNOWN;
        }
    }

    private Date getResolved(final Element elem) throws ParseException {
        return extractDateValue(elem, "resolved");
    }

    private ITSPriority getPriority(final Element elem) {
        String priority = extractValue(elem, "priority");
        switch (priority) {
        case "Trivial":
            return ITSPriority.TRIVIAL;
        case "Minor":
            return ITSPriority.MINOR;
        case "Major":
            return ITSPriority.MAJOR;
        case "Critical":
            return ITSPriority.CRITICAL;
        case "Blocker":
            return ITSPriority.BLOCKER;
        default:
            return ITSPriority.UNKNOWN;
        }
    }

    private String getLink(final Element elem) {
        return extractValue(elem, "link");
    }

    private List<String> getFixVersion(final Element elem) {
        return extractValues(elem, "fixVersion");
    }

    private String getDescription(final Element elem) {
        return extractValue(elem, "description");
    }

    private Date getCreated(final Element elem) throws ParseException {
        return extractDateValue(elem, "created");
    }

    private List<String> getComments(final Element elem) {
        return extractValues(elem, "comment");
    }

    private List<String> extractValues(final Element elem, final String tagName) {
        NodeList nodeList = elem.getElementsByTagName(tagName);
        int size = nodeList.getLength();
        List<String> values = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            String value = nodeList.item(i).getFirstChild().getNodeValue().trim();
            values.add(value);
        }
        return values;
    }

    private String getKey(final Element elem) {
        return extractValue(elem, "key");
    }

    private String extractValue(final Element elem, final String tagName) {
        NodeList nodeList = elem.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            return null;
        }
        Node firstChild = elem.getElementsByTagName(tagName).item(0).getFirstChild();
        return firstChild == null ? null : firstChild.getNodeValue().trim();
    }

    private Date parseDate(final String nodeValue) throws ParseException {
        // Mon, 16 Feb 2004 00:29:19 +0000
        //FIXME majchmar: fix time parsing, timezone
        SimpleDateFormat sdf = new SimpleDateFormat(JIRA_DATE_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+000"));
        sdf.setLenient(true);
        Date date = sdf.parse(nodeValue);
        return date;
    }

    private Date extractDateValue(final Element elem, final String tagName) throws ParseException {
        String value = extractValue(elem, tagName);
        return value == null ? null : parseDate(value);
    }
}
