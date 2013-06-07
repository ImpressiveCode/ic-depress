package org.impressivecode.depress.metric.datasource;

public class ClassDataEntry {
	String className;
	String methodName;
	Boolean isPublic;
	Boolean isProtected;
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

	public void setLocation(String location) {
		this.location = location;
	}

	Boolean isPrivate;
	Boolean isStatic;
	Boolean isFinal;
	Boolean isAbstract;
	String location;
	
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

	public String getLocation() {
		return location;
	}
	
	
}
