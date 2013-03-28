package org.impressivecode.depress.scm.svn;

import org.knime.core.data.def.StringCell;

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


public class SVNLogRow {

	private StringCell className;
	private StringCell marker;
	private StringCell author;
	private StringCell action;
	private StringCell message;
	private StringCell path;
	private StringCell date;
	private StringCell uid;

	public StringCell getAction() {
		return action;
	}

	public StringCell getAuthor() {
		return author;
	}

	public StringCell getClassName() {
		return className;
	}

	public StringCell getDate() {
		return date;
	}

	public StringCell getMarker() {
		return marker;
	}

	public StringCell getMessage() {
		return message;
	}

	public StringCell getPath() {
		return path;
	}

	public StringCell getUid() {
		return uid;
	}

	public void setAction(StringCell action) {
		this.action = action;
	}

	public void setAuthor(StringCell author) {
		this.author = author;
	}

	public void setClassName(StringCell className) {
		this.className = className;
	}

	public void setDate(StringCell date) {
		this.date = date;
	}

	public void setMarker(StringCell marker) {
		this.marker = marker;
	}

	public void setMessage(StringCell message) {
		this.message = message;
	}

	public void setPath(StringCell path) {
		this.path = path;
	}

	public void setUid(StringCell uid) {
		this.uid = uid;
	}

	
}
