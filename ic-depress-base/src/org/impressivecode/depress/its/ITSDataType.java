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
package org.impressivecode.depress.its;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ITSDataType {
    private String issueId;
    private Date created;
    private Date updated;
    private Date resolved;
    private ITSStatus status;
    private ITSType type;
    private List<String> version = Collections.emptyList();
    private List<String> fixVersion = Collections.emptyList();
    private ITSPriority priority;
    private String summary;
    private String link;
    private String description;
    private List<String> comments = Collections.emptyList();
    private ITSResolution resolution;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(final String bugId) {
        this.issueId = bugId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

    public Date getResolved() {
        return resolved;
    }

    public void setResolved(final Date resolved) {
        this.resolved = resolved;
    }

    public ITSStatus getStatus() {
        return status;
    }

    public void setStatus(final ITSStatus status) {
        this.status = status;
    }

    public ITSType getType() {
        return type;
    }

    public void setType(final ITSType type) {
        this.type = type;
    }

    public List<String> getVersion() {
        return version;
    }

    public void setVersion(final List<String> version) {
        this.version = version;
    }

    public List<String> getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(final List<String> fixVersion) {
        this.fixVersion = fixVersion;
    }

    public ITSPriority getPriority() {
        return priority;
    }

    public void setPriority(final ITSPriority priority) {
        this.priority = priority;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(final List<String> comments) {
        this.comments = comments;
    }

    public void setResolution(final ITSResolution resolution) {
        this.resolution = resolution;
    }

    public ITSResolution getResolution() {
        return resolution;
    }

    @Override
    public String toString() {
        return String
                .format("ITSDataType [bugId=%s, created=%s, updated=%s, resolved=%s, status=%s, type=%s, version=%s, fixVersion=%s, priority=%s, summary=%s, link=%s, description=%s, comments=%s, resolution=%s]",
                        issueId, created, updated, resolved, status, type, Iterables.toString(version),
                        Iterables.toString(fixVersion), priority, summary, link, description, comments, resolution);
    }

}
