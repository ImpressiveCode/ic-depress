package org.impressivecode.depress.scm.endevor.models;

public class EndevorElementPathModel {
	
	private String system;
	private String subsystem;
	private String environment;
	private String stageId;
	private String type;
	private String element;
	private StringBuilder scmPath;
	
	public EndevorElementPathModel() {
		
	}
	
	@Override
	public String toString() {
		this.scmPath = new StringBuilder();
		
		scmPath.append(system);
		scmPath.append("/");
		scmPath.append(subsystem);
		scmPath.append("/");
		scmPath.append(environment);
		scmPath.append("/");
		scmPath.append(stageId);
		scmPath.append("/");
		scmPath.append(type);
		scmPath.append("/");
		scmPath.append(element);
		
		return scmPath.toString();
	}
	
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
}