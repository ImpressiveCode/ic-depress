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
package org.impressivecode.depress.its.common;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
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
    private String reporter;
    private Integer timeEstimate;
    private Integer timeSpent;
    /**
     * It is possible to have more assignees.
     */
    private Set<String> assignees = Collections.emptySet();
    private Set<String> commentAuthors = Collections.emptySet();
	private String parentId;
	private Set<String> labels;
	private Set<String> attachments = Collections.emptySet();
	private Integer votes;
	private Integer watches;
	private List<Date> commentsDates;

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

    public String getReporter() {
        return reporter;
    }

    public void setReporter(final String reporter) {
        this.reporter = reporter;
    }

    public Set<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(final Set<String> assignees) {
        this.assignees = assignees;
    }

    public Set<String> getCommentAuthors() {
        return commentAuthors;
    }

    public void setCommentAuthors(final Set<String> commentAuthors) {
        this.commentAuthors = commentAuthors;
    }

    public Integer getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(final Integer timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(final Integer timeSpent) {
        this.timeSpent = timeSpent;
    }
    

	public void setParentId(String parent) {
		this.parentId = parent;
	}
	
	public String getParentId(){
		return parentId;
	}
	

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}
	
	public Set<String> getLabels() {
		return this.labels;
	}

	public Set<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<String> attachments) {
		this.attachments = attachments;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public Integer getWatches() {
		return watches;
	}

	public void setWatches(Integer watchers) {
		this.watches = watchers;
	}

	public List<Date> getCommentsDates() {
		return commentsDates;
	}

	public void setCommentsDates(List<Date> commentsDates) {
		this.commentsDates = commentsDates;
	}

	@Override
	public String toString() {
		return "ITSDataType [issueId=" + issueId + ", created=" + created + ", updated=" + updated + ", resolved="
				+ resolved + ", status=" + status + ", type=" + type + ", version=" + version + ", fixVersion="
				+ fixVersion + ", priority=" + priority + ", summary=" + summary + ", link=" + link + ", description="
				+ description + ", comments=" + comments + ", resolution=" + resolution + ", reporter=" + reporter
				+ ", timeEstimate=" + timeEstimate + ", timeSpent=" + timeSpent + ", assignees=" + assignees
				+ ", commentAuthors=" + commentAuthors + ", parentId=" + parentId + ", labels=" + labels
				+ ", attachments=" + attachments + ", votes=" + votes + ", watches=" + watches + ", commentsDates="
				+ commentsDates + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignees == null) ? 0 : assignees.hashCode());
		result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
		result = prime * result + ((commentAuthors == null) ? 0 : commentAuthors.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fixVersion == null) ? 0 : fixVersion.hashCode());
		result = prime * result + ((issueId == null) ? 0 : issueId.hashCode());
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((reporter == null) ? 0 : reporter.hashCode());
		result = prime * result + ((resolution == null) ? 0 : resolution.hashCode());
		result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((timeEstimate == null) ? 0 : timeEstimate.hashCode());
		result = prime * result + ((timeSpent == null) ? 0 : timeSpent.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((votes == null) ? 0 : votes.hashCode());
		result = prime * result + ((watches == null) ? 0 : watches.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ITSDataType other = (ITSDataType) obj;
		if (assignees == null) {
			if (other.assignees != null)
				return false;
		} else if (!assignees.equals(other.assignees))
			return false;
		if (attachments == null) {
			if (other.attachments != null)
				return false;
		} else if (!attachments.equals(other.attachments))
			return false;
		if (commentAuthors == null) {
			if (other.commentAuthors != null)
				return false;
		} else if (!commentAuthors.equals(other.commentAuthors))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fixVersion == null) {
			if (other.fixVersion != null)
				return false;
		} else if (!fixVersion.equals(other.fixVersion))
			return false;
		if (issueId == null) {
			if (other.issueId != null)
				return false;
		} else if (!issueId.equals(other.issueId))
			return false;
		if (labels == null) {
			if (other.labels != null)
				return false;
		} else if (!labels.equals(other.labels))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (priority != other.priority)
			return false;
		if (reporter == null) {
			if (other.reporter != null)
				return false;
		} else if (!reporter.equals(other.reporter))
			return false;
		if (resolution != other.resolution)
			return false;
		if (resolved == null) {
			if (other.resolved != null)
				return false;
		} else if (!resolved.equals(other.resolved))
			return false;
		if (status != other.status)
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		if (timeEstimate == null) {
			if (other.timeEstimate != null)
				return false;
		} else if (!timeEstimate.equals(other.timeEstimate))
			return false;
		if (timeSpent == null) {
			if (other.timeSpent != null)
				return false;
		} else if (!timeSpent.equals(other.timeSpent))
			return false;
		if (type != other.type)
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (votes == null) {
			if (other.votes != null)
				return false;
		} else if (!votes.equals(other.votes))
			return false;
		if (watches == null) {
			if (other.watches != null)
				return false;
		} else if (!watches.equals(other.watches))
			return false;
		return true;
	}

}
