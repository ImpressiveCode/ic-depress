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
package org.impressivecode.depress.scm;

import java.util.Date;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMDataType implements Cloneable{

    private String resourceName;
    private String author;
    private SCMOperation operation;
    private String message;
    private String path;
    private Date commitDate;
    private String commitID;
    private String extension;
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public SCMOperation getOperation() {
        return operation;
    }

    public void setOperation(final SCMOperation operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(final Date commitDate) {
        this.commitDate = commitDate;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(final String commitID) {
        this.commitID = commitID;
    }

    public void setResourceName(final String resource) {
        this.resourceName = resource;
    }

    public String getResourceName() {
        return resourceName;
    }
    
	public String getExtension() {
		return extension;
	}
	
    public void setExtension(final String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return String
                .format("SCMDataType [resourceName=%s, extension=%s, author=%s, operation=%s, message=%s, path=%s, commitDate=%s, commitID=%s]",
                        resourceName, extension, author, operation, message, path, commitDate, commitID);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
