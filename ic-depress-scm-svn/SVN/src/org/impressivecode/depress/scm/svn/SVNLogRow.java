package org.impressivecode.depress.scm.svn;

import org.knime.core.data.def.StringCell;

public class SVNLogRow {

	private StringCell className;
	private StringCell marker;
	private StringCell author;
	private StringCell action;
	private StringCell message;
	private StringCell path;
	private StringCell date;
	private StringCell uid;

	public StringCell getClassName() {
		return className;
	}

	public void setClassName(StringCell className) {
		this.className = className;
	}

	public StringCell getMarker() {
		return marker;
	}

	public void setMarker(StringCell marker) {
		this.marker = marker;
	}

	public StringCell getAuthor() {
		return author;
	}

	public void setAuthor(StringCell author) {
		this.author = author;
	}

	public StringCell getAction() {
		return action;
	}

	public void setAction(StringCell action) {
		this.action = action;
	}

	public StringCell getMessage() {
		return message;
	}

	public void setMessage(StringCell message) {
		this.message = message;
	}

	public StringCell getPath() {
		return path;
	}

	public void setPath(StringCell path) {
		this.path = path;
	}

	public StringCell getDate() {
		return date;
	}

	public void setDate(StringCell date) {
		this.date = date;
	}

	public StringCell getUid() {
		return uid;
	}

	public void setUid(StringCell uid) {
		this.uid = uid;
	}
}
