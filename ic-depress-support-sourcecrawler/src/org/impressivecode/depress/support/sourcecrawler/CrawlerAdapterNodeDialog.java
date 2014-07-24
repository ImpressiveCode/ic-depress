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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerAdapterNodeDialog extends DefaultNodeSettingsPane {
	
	public static final String ADVANCED_TAB_NAME = "Advanced";
	public static final String CREATE_XML = "Create XML file";
	 
    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.sourcecrawler.historyid";

    private static final int booleanComponentWidth = 40;
    private static final int booleanComponentHeight = 30;
    
    protected CrawlerAdapterNodeDialog() {
        addDialogComponent(new DialogComponentFileChooser(CrawlerAdapterNodeModel.createFileSettings(), HISTORY_ID, JFileChooser.OPEN_DIALOG, false, FILE_EXTENSION));
        
        setHorizontalPlacement(true);
        
        createNewGroup("Acces:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelPublic(),
        		CrawlerOptionsParser.PUBLIC, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelPrivate(),
        		CrawlerOptionsParser.PRIVATE, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelProtected(),
        		CrawlerOptionsParser.PROTECTED, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelPackage(),
        		CrawlerOptionsParser.PACKAGE, booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        
        createNewGroup("Type:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelClass(),
        		CrawlerOptionsParser.CLASS, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelInterface(),
        		CrawlerOptionsParser.INTERFACE, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelAbstract(),
        		CrawlerOptionsParser.ABSTRACT, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelEnum(),
        		CrawlerOptionsParser.ENUM, booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        
        createNewGroup("Other:");
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelException(),
        		CrawlerOptionsParser.EXCEPTION, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelInner(),
        		CrawlerOptionsParser.INNER, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelTest(),
        		CrawlerOptionsParser.TEST, booleanComponentWidth, booleanComponentHeight));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createSettingsModelFinal(),
        		CrawlerOptionsParser.FINAL, booleanComponentWidth, booleanComponentHeight));
        closeCurrentGroup();
        
        setHorizontalPlacement(false);
        
        createNewTab(ADVANCED_TAB_NAME);
        
        addDialogComponent(new DialogComponentString(CrawlerAdapterNodeModel.createPackageSettings(), "Package: ", false, 40));
        addDialogComponent(createBooleanComponent(CrawlerAdapterNodeModel.createXMLSettings(),
        		CREATE_XML, booleanComponentWidth, booleanComponentHeight));
    }
    
    private DialogComponentBoolean createBooleanComponent(SettingsModelBoolean settings, String label, int width, int height){
    	DialogComponentBoolean component = new DialogComponentBoolean(settings, label);
    	component.getComponentPanel().setPreferredSize(new Dimension(width, height));
    	return component;
    }

}
