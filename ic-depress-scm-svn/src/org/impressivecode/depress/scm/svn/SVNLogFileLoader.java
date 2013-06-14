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

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.knime.core.node.CanceledExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVNLogFileLoader extends SVNLogLoader {

    /**
     * Actions: A - the item is added D - the item was deleted M - properties or
     * textual contents on the were changed R - the item was replaced by a
     * different one at the same location
     * 
     * @throws Exception
     * 
     * @throws FileNotFoundException
     */

    public void load(String inPath, String inIssueMarker, String inPackage, IReadProgressListener inProgress)
            throws CanceledExecutionException, Exception {

        if (new File(inPath).exists()) {
            loadXml(inPath, inIssueMarker, inPackage, inProgress);
            return;
        }

        throw new Exception(SVNLocale.eIvalidLocalPath(inPath));
    }

    // Methods
    protected void loadXml(String inPath, String inIssueMarker, String inPackage, IReadProgressListener inProgress)
            throws CanceledExecutionException, Exception {

        try {

            if (inPackage.endsWith(".*")) {
                inPackage = inPackage.substring(0, inPackage.length() - 2);
            }

            File fXmlFile = new File(inPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // recommended
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("logentry");

            tmpData.issueMarker = inIssueMarker;

            for (int logentry = 0; logentry < nList.getLength(); logentry++) {

                inProgress.checkLoading();

                Node nNode = nList.item(logentry);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    tmpData.uid = eElement.getAttribute("revision");

                    tmpData.author = getValue(eElement, "author");

                    tmpData.date = getValue(eElement, "date");

                    tmpData.message = getValue(eElement, "msg");

                    if (!isMarkerInMessage(tmpData.message, tmpData.issueMarker)) {
                        continue;
                    }

                    Node pathsNode = eElement.getElementsByTagName("paths").item(0);
                    Element ePathsElement = (Element) pathsNode;

                    NodeList nPathsList = ePathsElement.getElementsByTagName("path");

                    for (int path = 0; path < nPathsList.getLength(); path++) {

                        inProgress.checkLoading();

                        Node nPathNode = nPathsList.item(path);
                        Element ePathElement = (Element) nPathNode;

                        tmpData.path = ePathElement.getTextContent();

                        if (!isValidFile(tmpData.path)) {
                            continue;
                        }

                        if (isPackageValid(tmpData.path, inPackage) == false) {
                            continue;
                        }

                        tmpData.action = ePathElement.getAttribute("action");

                        inProgress.onReadProgress(percent(logentry, nList.getLength()), tmpData.createRow());
                    }
                }

            }
        } catch (CanceledExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private String getValue(Element inElement, String iName) {
        if (inElement.getElementsByTagName(iName) != null && inElement.getElementsByTagName(iName).getLength() > 0) {
            return inElement.getElementsByTagName(iName).item(0).getTextContent();
        }

        return null;
    }

    private final boolean isPackageValid(String path, String packageString) {
        path = path.replaceAll("/", ".");

        if (path.charAt(0) == '.') {
            path = path.substring(1);
        }

        if (packageString.equals("*")) {
            return true;
        }

        return path.contains(packageString);
    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
    }

}
