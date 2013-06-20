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

package org.impressivecode.depress.data.source;
/**
 * 
 * @author Marcin Strzeszyna
 * @author Wieslaw Rybicki
 * 
 */
public class DataSourceAdapterClassDataEntry {
private String className;
	private String methodName;
	private Boolean isPublic;
	private Boolean isProtected;
	private Boolean isPrivate;
	private Boolean isStatic;
	private Boolean isFinal;
	private Boolean isAbstract;
	private Boolean isInterface;
	private String location;
	
	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setIsStatic(Boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setIsFinal(Boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void setIsAbstract(Boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public void setIsInterface(Boolean isInterface) {
		this.isInterface = isInterface;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getClassName()
	{
		return this.className;
	}

	public String getMethodName() {
		return methodName;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public Boolean getIsProtected() {
		return isProtected;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public Boolean getIsStatic() {
		return isStatic;
	}

	public Boolean getIsFinal() {
		return isFinal;
	}

	public Boolean getIsAbstract() {
		return isAbstract;
	}

	public Boolean getIsInterface() {
		return isInterface;
	}

	public String getLocation() {
		return location;
	}
	
	
}

