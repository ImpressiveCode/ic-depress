package org.impressivecode.depress.its.jiraonline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraFieldModel {
	
	
	/**
	 *     private String issueId;
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
    private Set<String> assignees = Collections.emptySet();
    private Set<String> commentAuthors = Collections.emptySet();
	 */
	
	private String created;
	private String updated;
	private Status status;

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	
	
	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}



	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Status {
		private String description;
		private String name;
		private int id;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
