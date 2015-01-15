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
package org.impressivecode.depress.scm.endevor.models;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
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