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

import java.awt.Dimension;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerAdapterNodeDialog extends DefaultNodeSettingsPane {
	
	private static final String ADVANCED_TAB_NAME = "Advanced";
	 
    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.sourcecrawler.historyid";

    private static final int booleanComponentWidth = 40;
    private static final int booleanComponentHeight = 30;
    
    protected CrawlerAdapterNodeDialog() {
    	createOptionsTab();
    	createNewTab(ADVANCED_TAB_NAME);
        createAdvancedTab();
    }
    
    private DialogComponentBoolean createBooleanComponent(final String label, final String configLabel, int width, int height){
    	DialogComponentBoolean component = new DialogComponentBoolean(CrawlerAdapterNodeModel.createSettingsModel(configLabel, true), label);
    	component.getComponentPanel().setPreferredSize(new Dimension(width, height));
    	return component;
    }
    
    private void createOptionsTab(){
        addDialogComponent(new DialogComponentFileChooser(CrawlerAdapterNodeModel.createFileSettings(), HISTORY_ID, JFileChooser.OPEN_DIALOG, false, FILE_EXTENSION));
        
        setHorizontalPlacement(true);
        createNewGroup("Acces:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.PUBLIC, CrawlerAdapterNodeModel.PUBLIC_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.PRIVATE, CrawlerAdapterNodeModel.PRIVATE_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.PROTECTED, CrawlerAdapterNodeModel.PROTECTED_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.PACKAGE, CrawlerAdapterNodeModel.PACKAGE_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        
        createNewGroup("Type:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.CLASS, CrawlerAdapterNodeModel.CLASS_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.INTERFACE, CrawlerAdapterNodeModel.INTERFACE_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.ABSTRACT, CrawlerAdapterNodeModel.ABSTRACT_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.ENUM, CrawlerAdapterNodeModel.ENUM_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        
        createNewGroup("Other:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.EXCEPTION, CrawlerAdapterNodeModel.EXCEPTION_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.INNER, CrawlerAdapterNodeModel.INNER_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.TEST, CrawlerAdapterNodeModel.TEST_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.FINAL, CrawlerAdapterNodeModel.FINAL_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        setHorizontalPlacement(false);
    }
    
    void createAdvancedTab(){
        addDialogComponent(new DialogComponentString(CrawlerAdapterNodeModel.createPackageSettings(), "Package: ", false, 40));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.CREATE_XML, CrawlerAdapterNodeModel.CREATE_XML_CONFIG, 
        		booleanComponentWidth, booleanComponentHeight));
    }

}
