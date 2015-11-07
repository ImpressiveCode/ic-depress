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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a parsed git commit
 * 
 * @author Tomasz Kuzemko
 * @author S³awomir Kap³onski
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class GitCommit {
	
    private String id;
    private Date date;
    private String author;
    private StringBuilder messageBuilder;
    private List<GitCommitFile> files;

    public GitCommit() {
        this.messageBuilder = new StringBuilder();
        this.files = new ArrayList<GitCommitFile>();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return messageBuilder.toString();
    }

    public void addToMessage(final String text) {
        messageBuilder.append(text);
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public List<GitCommitFile> getFiles() {
        return files;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
