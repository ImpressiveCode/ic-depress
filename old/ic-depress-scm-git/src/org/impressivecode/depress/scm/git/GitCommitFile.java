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

package org.impressivecode.depress.scm.git;

import org.impressivecode.depress.scm.SCMOperation;

/**
 * Represents parsed commit information about a file
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * 
 */

public class GitCommitFile {
    private String path;
    private String javaClass;
    private SCMOperation operation;
    private String extension;

    public String getPath() {
        return path;
    }

    public SCMOperation getOperation() {
        return operation;
    }

    public void setOperation(final SCMOperation operation) {
        this.operation = operation;
    }

    public void setRawOperation(final char operationCode) {
        switch (operationCode) {
        case 'M':
            this.operation = SCMOperation.MODIFIED;
            break;
        case 'A':
            this.operation = SCMOperation.ADDED;
            break;
        case 'C':
            this.operation = SCMOperation.COPIED;
            break;
        case 'D':
            this.operation = SCMOperation.DELETED;
            break;
        case 'R':
            this.operation = SCMOperation.RENAMED;
            break;
        default:
            this.operation = SCMOperation.OTHER;
            break;
        }
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public void setJavaClass(final String javaClass) {
        this.javaClass = javaClass;
    }

    public String getJavaClass(){
        return this.javaClass;
    }

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
