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

import org.knime.core.node.CanceledExecutionException;

public abstract class SVNLogLoader {

	public interface IReadProgressListener {

		void onReadProgress(double inProgres, SVNLogRow inRow);

		void checkLoading() throws CanceledExecutionException;

	}

	protected Tmp tmData = new Tmp();

	public class Tmp {

		public String author;
		public String message;
		public String date;
		public String uid;
		public String issueMarker;
		public String action;
		public String path;

		public SVNLogRow createRow() {
			SVNLogRow r = new SVNLogRow();

			r.setMarker(cleanString(issueMarker));
			r.setAction(action);
			r.setPath(path);
			r.setClassName(getClassNameFromPath(path));
			r.setUid(uid);
			r.setAuthor(author);
			r.setMessage(cleanString(message));
			r.setDate(date);

			return r;
		}

		protected final String cleanString(String inText) {

			if (inText != null && inText.length() > 0) {
				return inText.replace('\n', ' ').replace('\r', ' ');
			}

			return "";
		}

		protected final String getClassNameFromPath(String path) {
			int lastDot = path.lastIndexOf(".");

			if (lastDot == -1) {
				lastDot = path.length() - 1;
			}

			int lastSlash = path.lastIndexOf("/") + 1;

			String fileName = path.substring(lastSlash, path.length());

			if (fileName.contains(".java") || fileName.contains(".JAVA")) {

				String smallPath = path.substring(lastSlash, lastDot);

				return (smallPath.substring(0, 1).toUpperCase() + smallPath
						.substring(1, smallPath.length()));
			}

			return "";
		}
	}

	public abstract void load(String inPath, String inIssueMarker,
			String inPackage, IReadProgressListener inProgress);

	protected final double percent(double cur, double all) {
		return (cur / all);
	}

	protected final boolean isValidFile(String path) {

		String[] dirs = path.split("/");

		if (dirs.length > 0) {
			String last = dirs[dirs.length - 1];

			return last.toLowerCase().contains(".java");
		} else {
			return dirs[0].toLowerCase().contains(".java");
		}
	}

	protected final boolean isMarkerInMessage(String message, String marker) {
		return message.contains(marker);
	}
}
