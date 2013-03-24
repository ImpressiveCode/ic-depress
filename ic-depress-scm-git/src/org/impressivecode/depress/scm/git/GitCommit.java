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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

/*
 * Represents a parsed git commit
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 *
 */

public class GitCommit {
	public final String id;
	public static final Pattern idRegex = Pattern.compile("^[a-f0-9]{40}$");
	static final DateFormat commitDateFormat = DateFormat.getDateInstance();
	Date date;
	String author;
	StringBuilder messageBuilder;
	public List<GitCommitFile> files;

	public GitCommit(String id) {
		if (!idRegex.matcher(id).matches()) {
			throw new InvalidParameterException("Commit id is invalid");
		}
		this.id = id;
		this.messageBuilder = new StringBuilder();
		this.files = new ArrayList<GitCommitFile>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(String date) throws ParseException {
		this.date = commitDateFormat.parse(date);
	}

	public String getMessage() {
		return messageBuilder.toString();
	}

	public void addToMessage(String text) {
		messageBuilder.append(text);
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}
}
