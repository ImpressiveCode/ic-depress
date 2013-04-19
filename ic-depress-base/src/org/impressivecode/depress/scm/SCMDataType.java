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

import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMDataType {

    private String resourceName;
    private Set<String> markers = Collections.emptySet();
    private String author;
    private String operation;
    private String message;
    private String path;
    private Date commitDate;
    private String commitID;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
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

    public void setMarkers(final Set<String> markers) {
        this.markers = markers;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Set<String> getMarkers() {
        return markers;
    }

}
