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
package org.impressivecode.depress.scm.svn;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.svn.SVNOfflineParser.SVNLog.Logentry;
import org.impressivecode.depress.scm.svn.SVNOfflineParser.SVNLog.Logentry.Paths.Path;

import com.google.common.collect.Lists;

/**
 * @author Marek Majchrzak, ImpressiveCode
 */
public class SVNOfflineParser {
    final SVNParserOptions parserOptions;

    public SVNOfflineParser(final SVNParserOptions parserOptions) {
        this.parserOptions = checkNotNull(parserOptions, "Options has to be set");
    }

    public List<SCMDataType> parseEntries(final String path) throws JAXBException, CloneNotSupportedException {
        checkArgument(!isNullOrEmpty(path), "Path has to be set.");

        List<SCMDataType> commitsList = parse(path, parserOptions);

        return commitsList;
    }

    private List<SCMDataType> parse(final String path, final SVNParserOptions parserOptions) throws JAXBException,
    CloneNotSupportedException {
        JAXBContext jaxbContext = JAXBContext.newInstance(SVNLog.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SVNLog log = (SVNLog) unmarshaller.unmarshal(new File(path));
        return convertToSCMType(log, parserOptions);
    }

    private List<SCMDataType> convertToSCMType(final SVNLog log, final SVNParserOptions parserOptions)
            throws CloneNotSupportedException {
        List<SCMDataType> scmEntries = Lists.newArrayListWithCapacity(1000);
        for (Logentry entry : log.getLogentry()) {
            SCMDataType base = scmBase(entry);
            for (Path path : entry.getPaths().getPath()) {
                if (include(path)) {
                    scmEntries.add(scm((SCMDataType) base.clone(), path));
                }
            }
        }
        return scmEntries;
    }

    private boolean include(final Path path) {
        String transformedPath = path.getValue().replaceAll("/", ".");
        boolean java = transformedPath.endsWith(".java");
        if (java) {
            if (parserOptions.hasPackagePrefix()) {
                return transformedPath.indexOf(parserOptions.getPackagePrefix()) != -1;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private SCMDataType scm(final SCMDataType scm, final Path path) {
        scm.setOperation(parseOperation(path));
        scm.setResourceName(parseJavaClass(path));
        scm.setPath(parsePath(path));
        return scm;
    }

    private String parsePath(final Path path) {
        return path.getValue();
    }

    private String parseJavaClass(final Path path) {
        String transformedPath = path.getValue().replaceAll("/", ".");
        String javaClass = transformedPath.replace(".java", "");
        if (parserOptions.hasPackagePrefix()) {
            javaClass = javaClass.substring(javaClass.indexOf(parserOptions.getPackagePrefix()));
        }
        return javaClass;
    }

    private SCMDataType scmBase(final Logentry entry) {
        SCMDataType scm = new SCMDataType();
        scm.setCommitID(parseCommitID(entry));
        scm.setAuthor(parseAuthor(entry));
        scm.setCommitDate(parseCommitDate(entry));
        scm.setMarkers(parseMarkers(entry));
        scm.setMessage(parseMessage(entry));
        return scm;
    }

    private SCMOperation parseOperation(final Path path) {
        switch (path.getAction()) {
        case "R":
            return SCMOperation.RENAMED;
        case "M":
            return SCMOperation.MODIFIED;
        case "A":
            return SCMOperation.ADDED;
        case "D":
            return SCMOperation.DELETED;
        default:
            return SCMOperation.OTHER;
        }
    }

    private String parseMessage(final Logentry entry) {
        return entry.getMsg();
    }

    private Set<String> parseMarkers(final Logentry entry) {
        if (this.parserOptions.hasMarkerPattern()) {
            Set<String> markers = newHashSet();
            Matcher matcher = this.parserOptions.getMarkerPattern().matcher(entry.getMsg());
            while (matcher.find()) {
                if (matcher.groupCount() >= 1) {
                    markers.add(matcher.group(1));
                } else {
                    markers.add(matcher.group());
                }
            }
            return markers;
        }
        return Collections.<String> emptySet();
    }

    private Date parseCommitDate(final Logentry entry) {
        return entry.getDate().toGregorianCalendar().getTime();
    }

    private String parseAuthor(final Logentry entry) {
        return entry.getAuthor();
    }

    private String parseCommitID(final Logentry entry) {
        return String.valueOf(entry.getRevision());
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "logentry" })
    @XmlRootElement(name = "log")
    static class SVNLog {

        @XmlElement(required = true)
        protected List<SVNLog.Logentry> logentry;

        public List<SVNLog.Logentry> getLogentry() {
            if (logentry == null) {
                logentry = new ArrayList<SVNLog.Logentry>();
            }
            return this.logentry;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "author", "date", "paths", "msg" })
        public static class Logentry {

            @XmlElement(required = true)
            protected String author;
            @XmlElement(required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar date;
            @XmlElement(required = true)
            protected SVNLog.Logentry.Paths paths;
            @XmlElement(required = true)
            protected String msg;
            @XmlAttribute(required = true)
            protected int revision;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(final String value) {
                this.author = value;
            }

            public XMLGregorianCalendar getDate() {
                return date;
            }

            public void setDate(final XMLGregorianCalendar value) {
                this.date = value;
            }

            public SVNLog.Logentry.Paths getPaths() {
                return paths;
            }

            public void setPaths(final SVNLog.Logentry.Paths value) {
                this.paths = value;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(final String value) {
                this.msg = value;
            }

            public int getRevision() {
                return revision;
            }

            public void setRevision(final int value) {
                this.revision = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "path" })
            public static class Paths {

                @XmlElement(required = true)
                protected List<SVNLog.Logentry.Paths.Path> path;

                public List<SVNLog.Logentry.Paths.Path> getPath() {
                    if (path == null) {
                        path = new ArrayList<SVNLog.Logentry.Paths.Path>();
                    }
                    return this.path;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "value" })
                public static class Path {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "text-mods")
                    protected Boolean textMods;
                    @XmlAttribute(name = "prop-mods")
                    protected Boolean propMods;
                    @XmlAttribute(required = true)
                    protected String kind;
                    @XmlAttribute(name = "copyfrom-rev")
                    protected Integer copyfromRev;
                    @XmlAttribute(name = "copyfrom-path")
                    protected String copyfromPath;
                    @XmlAttribute(required = true)
                    protected String action;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(final String value) {
                        this.value = value;
                    }

                    public Boolean isTextMods() {
                        return textMods;
                    }

                    public void setTextMods(final Boolean value) {
                        this.textMods = value;
                    }

                    public Boolean isPropMods() {
                        return propMods;
                    }

                    public void setPropMods(final Boolean value) {
                        this.propMods = value;
                    }

                    public String getKind() {
                        return kind;
                    }

                    public void setKind(final String value) {
                        this.kind = value;
                    }

                    public Integer getCopyfromRev() {
                        return copyfromRev;
                    }

                    public void setCopyfromRev(final Integer value) {
                        this.copyfromRev = value;
                    }

                    public String getCopyfromPath() {
                        return copyfromPath;
                    }

                    public void setCopyfromPath(final String value) {
                        this.copyfromPath = value;
                    }

                    public String getAction() {
                        return action;
                    }

                    public void setAction(final String value) {
                        this.action = value;
                    }
                }
            }
        }
    }
}
