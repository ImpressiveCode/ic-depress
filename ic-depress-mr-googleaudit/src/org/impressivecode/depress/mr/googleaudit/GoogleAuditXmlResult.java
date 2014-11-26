package org.impressivecode.depress.mr.googleaudit;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "resource" })
@XmlRootElement(name = "audit-violation-set")
public class GoogleAuditXmlResult {

	@XmlElement(name = "resource", required = true)
	protected List<Resource> resource;

	@XmlAttribute(name = "title", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected String title;

	@XmlAttribute(name = "resource-count-total", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected int resourceCountTotal;

	@XmlAttribute(name = "severity-count-high", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected int severityCountHigh;

	@XmlAttribute(name = "severity-count-medium", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected int severityCountMedium;

	@XmlAttribute(name = "severity-count-low", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected int severityCountLow;

	public List<Resource> getResource() {
		if (resource == null) {
			resource = new ArrayList<Resource>();
		}
		return this.resource;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getResourceCountTotal() {
		return resourceCountTotal;
	}

	public void setResourceCountTotal(int resourceCountTotal) {
		this.resourceCountTotal = resourceCountTotal;
	}

	public int getSeverityCountHigh() {
		return severityCountHigh;
	}

	public void setSeverityCountHigh(int severityCountHigh) {
		this.severityCountHigh = severityCountHigh;
	}

	public int getSeverityCountMedium() {
		return severityCountMedium;
	}

	public void setSeverityCountMedium(int severityCountMedium) {
		this.severityCountMedium = severityCountMedium;
	}

	public int getSeverityCountLow() {
		return severityCountLow;
	}

	public void setSeverityCountLow(int severityCountLow) {
		this.severityCountLow = severityCountLow;
	}

	public void setResource(List<Resource> resource) {
		this.resource = resource;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "auditViolation" })
	@XmlRootElement(name = "resource")
	public static class Resource {

		@XmlElement(name = "audit-violation", required = true)
		protected List<AuditViolation> auditViolation;

		@XmlAttribute(name = "path", required = true)
		@XmlSchemaType(name = "anySimpleType")
		protected String path;

		@XmlAttribute(name = "count", required = true)
		@XmlSchemaType(name = "anySimpleType")
		protected int count;

		public List<AuditViolation> getAuditViolation() {
			if (auditViolation == null) {
				auditViolation = new ArrayList<AuditViolation>();
			}
			return this.auditViolation;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public static class AuditViolation {

			@XmlAttribute(name = "description", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String description;

			@XmlAttribute(name = "recommendation", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String recommendation;

			@XmlAttribute(name = "severity", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String severity;

			@XmlAttribute(name = "absolutePath", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String absolutePath;

			@XmlAttribute(name = "file", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String file;

			@XmlAttribute(name = "line", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected int line;

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getRecommendation() {
				return recommendation;
			}

			public void setRecommendation(String recommendation) {
				this.recommendation = recommendation;
			}

			public String getSeverity() {
				return severity;
			}

			public void setSeverity(String severity) {
				this.severity = severity;
			}

			public String getAbsolutePath() {
				return absolutePath;
			}

			public void setAbsolutePath(String absolutePath) {
				this.absolutePath = absolutePath;
			}

			public String getFile() {
				return file;
			}

			public void setFile(String file) {
				this.file = file;
			}

			public int getLine() {
				return line;
			}

			public void setLine(int line) {
				this.line = line;
			}

		}
	}
}