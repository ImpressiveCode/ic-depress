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
package org.impressivecode.depress.its.bugzilla;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class BugzillaEntriesParser {
    private static final String BUGZILLA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";
    private final HashMap<String, String[]> prioritySettings;
    private final HashMap<String, String[]> resolutionSettings;
    private final HashMap<String, String[]> statusSettings;

    public BugzillaEntriesParser(final HashMap<String, String[]> prioritySettings,
            final HashMap<String, String[]> resolutionSettings, HashMap<String, String[]> statusSettings) {
        this.prioritySettings = prioritySettings;
        this.resolutionSettings = resolutionSettings;
        this.statusSettings = statusSettings;
    }

    public List<ITSDataType> parseEntries(final String path) throws ParserConfigurationException, SAXException,
            IOException, ParseException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);

        NodeList nList = getItemNodes(doc);
        int size = nList.getLength();
        List<ITSDataType> entries = Lists.newLinkedList();
        String urlBase = getUrlBase(doc);
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            ITSDataType entry = parse((Element) item);
            updateUrl(entry, urlBase);
            entries.add(entry);
        }

        return entries;
    }

    private void updateUrl(final ITSDataType entry, final String urlBase) {
        entry.setLink(urlBase + entry.getIssueId());
    }

    private String getUrlBase(final Document doc) {
        return doc.getDocumentElement().getAttribute("urlbase") + "show_bug.cgi?id=";
    }

    private NodeList getItemNodes(final Document doc) {
        return doc.getElementsByTagName("bug");
    }

    private ITSDataType parse(final Element elem) throws ParseException {
        ITSDataType data = new ITSDataType();
        data.setIssueId(getKey(elem));
        data.setComments(getComments(elem));
        data.setCreated(getCreated(elem));
        data.setDescription(getDescription(elem));
        data.setFixVersion(getFixVersion(elem));
        data.setPriority(getPriority(elem));
        data.setResolved(getResolved(elem)); // no proper history in offline
                                             // version
        data.setUpdated(getResolved(elem));// no proper history in offline
                                           // version
        data.setStatus(getStatus(elem));
        data.setSummary(getSummary(elem));
        data.setType(getType(elem));
        data.setUpdated(getUpdated(elem));
        data.setVersion(getVersion(elem));
        data.setResolution(getResolution(elem));
        data.setReporter(getReporter(elem));
        data.setAssignees(getAssinees(elem));
        data.setCommentAuthors(getCommentAuthors(elem));
        data.setTimeEstimate(getTimeEstimate(elem));
        data.setTimeSpent(getTimeSpent(elem));
        return data;
    }

    private Integer getTimeSpent(final Element elem) {
        String stringTime = extractValue(elem, "actual_time");
        if (null == stringTime) {
            return null;
        }
        return (int) (Double.valueOf(stringTime) * 60);
    }

    private Integer getTimeEstimate(final Element elem) {
        String stringTime = extractValue(elem, "estimated_time");
        if (null == stringTime) {
            return null;
        }
        return (int) (Double.valueOf(stringTime) * 60);
    }

    private Set<String> getCommentAuthors(final Element elem) {
        NodeList nodeList = elem.getElementsByTagName("long_desc");
        Builder<String> authors = ImmutableSet.builder();
        if (nodeList.getLength() <= 1) {
            return authors.build();
        }
        for (int i = 1; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            authors.add(extractValue((Element) item, "who"));
        }
        return authors.build();
    }

    private Set<String> getAssinees(final Element elem) {
        String value = extractValue(elem, "assigned_to");
        return value == null ? Collections.<String> emptySet() : ImmutableSet.of(value);
    }

    private String getReporter(final Element elem) {
        return extractValue(elem, "reporter");
    }

    private ITSResolution getResolution(final Element elem) {
        String resolution = extractValue(elem, "resolution");
        if (resolution == null) {
            return ITSResolution.UNKNOWN;
        }
        for (String key : resolutionSettings.keySet()) {
            for (String value : resolutionSettings.get(key)) {
                if (resolution.equalsIgnoreCase(value))
                    return ITSResolution.get(key);
            }
        }
        return ITSResolution.UNKNOWN;
    }

    private List<String> getVersion(final Element elem) {
        return extractValues(elem, "version");
    }

    private Date getUpdated(final Element elem) throws ParseException {
        return null;
    }

    private ITSType getType(final Element elem) {
        return ITSType.BUG;
    }

    private String getSummary(final Element elem) {
        return extractValue(elem, "short_desc");
    }

    private ITSStatus getStatus(final Element elem) {
        String status = extractValue(elem, "bug_status");
        if (status == null) {
            return ITSStatus.UNKNOWN;
        }
        for (String key : statusSettings.keySet()) {
            for (String value : statusSettings.get(key)) {
                if (status.equalsIgnoreCase(value)) {
                    return ITSStatus.get(key);
                }
            }
        }
        return ITSStatus.UNKNOWN;
    }

    private Date getResolved(final Element elem) throws ParseException {
        return extractDateValue(elem, "delta_ts");
    }

    private ITSPriority getPriority(final Element elem) {
        String priority = extractValue(elem, "bug_severity");
        if (priority == null) {
            return ITSPriority.UNKNOWN;
        }
        for (String key : prioritySettings.keySet()) {
            for (String value : prioritySettings.get(key)) {
                if (priority.equalsIgnoreCase(value)) {
                    return ITSPriority.get(key);
                }
            }
        }
        return ITSPriority.UNKNOWN;
    }

    private List<String> getFixVersion(final Element elem) {
        return extractValues(elem, "target_milestone");
    }

    private String getDescription(final Element elem) {
        return extractValue(elem, "thetext");
    }

    private Date getCreated(final Element elem) throws ParseException {
        return extractDateValue(elem, "creation_ts");
    }

    private List<String> getComments(final Element elem) {
        List<String> values = extractValues(elem, "thetext");
        if (values.size() > 1) {
            return values.subList(1, values.size());
        } else {
            return Collections.emptyList();
        }
    }

    private String getKey(final Element elem) {
        return extractValue(elem, "bug_id");
    }

    private List<String> extractValues(final Element elem, final String tagName) {
        NodeList nodeList = elem.getElementsByTagName(tagName);
        int size = nodeList.getLength();
        List<String> values = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node firstChild = nodeList.item(i).getFirstChild();
            if (firstChild != null) {
                String value = firstChild.getNodeValue().trim();
                values.add(value);
            }
        }
        return values;
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
        // FIXME majchmar: fix time parsing, timezone
        SimpleDateFormat sdf = new SimpleDateFormat(BUGZILLA_DATE_FORMAT, Locale.US);
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
