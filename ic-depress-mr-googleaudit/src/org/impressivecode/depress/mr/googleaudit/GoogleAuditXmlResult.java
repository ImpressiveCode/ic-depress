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

	public List<Resource> getResource() {
		if (resource == null) {
			resource = new ArrayList<Resource>();
		}
		return this.resource;
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

		public static class AuditViolation {

			@XmlAttribute(name = "severity", required = true)
			@XmlSchemaType(name = "anySimpleType")
			protected String severity;

			public String getSeverity() {
				return severity;
			}

			public void setSeverity(String severity) {
				this.severity = severity;
			}
		}
	}
}