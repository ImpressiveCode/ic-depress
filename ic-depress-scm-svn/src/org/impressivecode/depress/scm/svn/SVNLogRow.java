package org.impressivecode.depress.scm.svn;

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

	private String className;
	private String marker;
	private String author;
	private String action;
	private String message;
	private String path;
	private String date;
	private String uid;

	public String getAction() {
		return action;
	}

	public String getAuthor() {
		return author;
	}

	public String getClassName() {
		return className;
	}

	public String getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "SVNLogRow [className=" + className + ", marker=" + marker
				+ ", author=" + author + ", action=" + action + ", message="
				+ message + ", path=" + path + ", date=" + date + ", uid="
				+ uid + "]";
	}

	public String getMarker() {
		return marker;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public String getUid() {
		return uid;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
