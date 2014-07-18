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
package org.impressivecode.depress.support.sourcecrawler;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerAdapterNodeDialog extends DefaultNodeSettingsPane {

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.sourcecrawler.historyid";

    protected CrawlerAdapterNodeDialog() {
    	CrawlerAdapterNodeModel nodeModel = new CrawlerAdapterNodeModel();
    	
        addDialogComponent(new DialogComponentFileChooser(nodeModel.createFileSettings(), HISTORY_ID, JFileChooser.OPEN_DIALOG, false, FILE_EXTENSION));
        
    }

}
