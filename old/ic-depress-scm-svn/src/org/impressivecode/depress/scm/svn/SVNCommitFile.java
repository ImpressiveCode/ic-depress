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
package org.impressivecode.depress.scm.svn;

import org.impressivecode.depress.scm.SCMOperation;

public class SVNCommitFile {
	private String path;
	private String resourceName;
	private SCMOperation operation;
	private String extension;

	public String getPath() {
		return path;
	}
	
	public String getExtension() {
		return extension;
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
		case 'D':
			this.operation = SCMOperation.DELETED;
			break;
		default:
			this.operation = SCMOperation.OTHER;
			break;
		}
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public void setExtension(final String extension) {
		this.extension = extension;
	}
	
	public void setResourceName(final String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return this.resourceName;
	}
}
