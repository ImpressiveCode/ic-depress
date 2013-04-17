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

import java.security.InvalidParameterException;

/*
 * Represents parsed commit information about a file
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 *
 */

public class GitCommitFile {
    final String path;
    GitCommitFileOperation operation;
    GitCommit commit;
    int churn;
    boolean isTextFile = false;

    public GitCommitFile(String path) {
        this.path = path;
    }

    public GitCommitFile(String path, char operationCode) {
        this.path = path;
        setOperation(operationCode);
    }

    public GitCommitFile(String path, char operationCode, GitCommit commit) {
        this.path = path;
        setOperation(operationCode);
        setCommit(commit);
    }

    public String getPath() {
        return path;
    }

    public GitCommit getCommit() {
        return commit;
    }

    public void setCommit(GitCommit commit) {
        this.commit = commit;
    }

    public GitCommitFileOperation getOperation() {
        return operation;
    }

    public void setOperation(GitCommitFileOperation operation) {
        this.operation = operation;
    }

    public void setOperation(char operationCode) {
        switch (operationCode) {
        case 'M':
            this.operation = GitCommitFileOperation.Modified;
            break;
        case 'A':
            this.operation = GitCommitFileOperation.Added;
            break;
        case 'C':
            this.operation = GitCommitFileOperation.Copied;
            break;
        case 'D':
            this.operation = GitCommitFileOperation.Deleted;
            break;
        case 'R':
            this.operation = GitCommitFileOperation.Renamed;
            break;
        case 'T':
            this.operation = GitCommitFileOperation.TypeChanged;
            break;
        default:
            throw new InvalidParameterException(String.format(
                    "Invalid operation code '{1}'",
                    String.valueOf(operationCode)));
        }
    }

    public void setChurn(int churn) {
        this.churn = churn;
        this.isTextFile = true;
    }

    public boolean isTextFile() {
        return this.isTextFile;
    }

    public int getChurn() {
        if (!isTextFile) {
            throw new UnsupportedOperationException("Can only retrieve churn from a text file");
        }
        return this.churn;
    }
}
