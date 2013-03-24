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

/*
 * Represents parsed commit information about a file
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 *
 */

public class GitCommitFile {
	public final String path;
	GitCommitFileOperation operation;
	GitCommit commit;


	public GitCommitFile(String path) {
		this.path = path;
	}

	public GitCommit getParent() {
		return commit;
	}

	public void setParent(GitCommit parent) {
		this.commit = parent;
	}

	public GitCommitFileOperation getOperation() {
		return operation;
	}

	public void setOperation(GitCommitFileOperation operation) {
		this.operation = operation;
	}
}
