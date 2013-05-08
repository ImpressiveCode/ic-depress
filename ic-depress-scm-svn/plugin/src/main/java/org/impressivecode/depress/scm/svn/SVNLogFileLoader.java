package org.impressivecode.depress.scm.svn;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.knime.core.data.def.StringCell;
import org.knime.core.node.CanceledExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

public class SVNLogFileLoader {

	public interface IReadProgressListener {

		void onReadProgress(int inProgres, SVNLogRow inRow)
				throws CanceledExecutionException;

	}

	/**
	 * Actions: A - the item is added D - the item was deleted M - properties or
	 * textual contents on the were changed R - the item was replaced by a
	 * different one at the same location
	 */

	public void load(String inPath, String inIssueMarker, String inPackage,
			IReadProgressListener inProgress) {
		loadXml(inPath, inIssueMarker, inPackage, inProgress);
	}

	// Methods
	protected void loadXml(String inPath, String inIssueMarker,
			String inPackage, IReadProgressListener inProgress) {

		try {

			if (inPackage.endsWith(".*")) {
				inPackage = inPackage.substring(0, inPackage.length() - 2);
			}

			String authorString = new String();
			String messageString = new String();
			String dateString = new String();
			String uidString = new String();

			File fXmlFile = new File(inPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// recommended
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("logentry");


			for (int logentry = 0; logentry < nList.getLength(); logentry++) {
				Node nNode = nList.item(logentry);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					uidString = eElement.getAttribute("revision");

					authorString = eElement.getElementsByTagName("author")
							.item(0).getTextContent();

					dateString = eElement.getElementsByTagName("date").item(0)
							.getTextContent();

					messageString = eElement.getElementsByTagName("msg")
							.item(0).getTextContent();

					// if message has not appropriate markers then will not
					// include that row
					if (isMarkerInMessage(messageString, inIssueMarker) == false) {
						continue;
					}
					// //////////////////////////////////////////////////////////////////////

					Node pathsNode = eElement.getElementsByTagName("paths")
							.item(0);
					Element ePathsElement = (Element) pathsNode;

					NodeList nPathsList = ePathsElement
							.getElementsByTagName("path");

					for (int path = 0; path < nPathsList.getLength(); path++) {
						Node nPathNode = nPathsList.item(path);
						Element ePathElement = (Element) nPathNode;

						String pathString = ePathElement.getTextContent();

						// if package is not valid then will not include this
						// row
						if (isPackageValid(pathString, inPackage) == false) {
							continue;
						}

						if (ePathElement.getAttribute("kind").equals("dir")) // directory
																				// is
																				// not
																				// important
																				// so
																				// skip
																				// it
						{
							continue;
						}

						// Uzupełnienie SVNLogRow

						SVNLogRow r = new SVNLogRow();
						r.setMarker(new StringCell(inIssueMarker));

						r.setAction(new StringCell(ePathElement
								.getAttribute("action")));

						r.setPath(new StringCell(pathString));

						r.setClassName(new StringCell(
								getClassNameFromPath(pathString)));
						r.setUid(new StringCell(uidString));
						r.setAuthor(new StringCell(authorString));
						r.setMessage(new StringCell(messageString));
						r.setDate(new StringCell(dateString));

						inProgress.onReadProgress(logentry / nList.getLength(),
								r);			
					}
				}
			}
		} catch (Exception e) {
			Logger.instance().error(" loadXml ", e);
			try {
				inProgress.onReadProgress(100, null);
			} catch (CanceledExecutionException e1) {
				Logger.instance()
						.error(" loadXml onReadProgress complete ", e1);
			}
		}
	}

	public final String getClassNameFromPath(String path) {
		int lastDot = path.lastIndexOf(".");

		if (lastDot == -1) {
			lastDot = path.length() - 1;
		}

		int lastSlash = path.lastIndexOf("/") + 1;

		String smallPath = path.substring(lastSlash, lastDot);

		return (smallPath.substring(0, 1).toUpperCase() + smallPath.substring(
				1, smallPath.length()));
	}

	public final boolean isPackageValid(String path, String packageString) {
		path = path.replaceAll("/", ".");

		if (path.charAt(0) == '.') {
			path = path.substring(1);
		}

		if (packageString.equals("*")) {
			return true;
		}

		return path.contains(packageString);
	}

	public final boolean isMarkerInMessage(String message, String marker) {
		return message.contains(marker);
	}

	public void loadXml3(String inPath, String inIsueMarker, String inPackage,
			IReadProgressListener inProgress) {

		Logger.instance().warn("Start loading..");

		Logger.instance().warn("Path : " + inPath);
		Logger.instance().warn("IsueMarker : " + inIsueMarker);
		Logger.instance().warn("Package : " + inPackage);

		try {
			for (int i = 0; i < 100; ++i) {

				SVNLogRow r = new SVNLogRow();

				r.setClassName(new StringCell(" ClassName  " + i));
				r.setAuthor(new StringCell(" Author  " + i));
				r.setAction(new StringCell(" Action  " + i));
				r.setDate(new StringCell(" Date  " + i));
				r.setMarker(new StringCell(" Maker  " + i));
				r.setMessage(new StringCell(" Message  " + i));
				r.setPath(new StringCell(" Path  " + i));
				r.setUid(new StringCell(" Uid  " + i));

				inProgress.onReadProgress(i, r);

			}
		} catch (CanceledExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.instance().warn("End loading..");
	}

}
