/*
ImpressiveCode Depress Framework
Copyright (C) 2014  ImpressiveCode contributors

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

import static org.junit.Assert.*;

import java.util.Hashtable;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SourceCrawlerXMLTest {
	
	private final static String testedFilePath = SourceCrawlerXMLTest.class.getResource("test.xml").getPath();
    
	SourceCrawlerOutput output;
	
	@Before
    public void setUp() throws JAXBException {
    	CrawlerEntriesParser parser = new CrawlerEntriesParser();
    	output = parser.parseFromXML(testedFilePath);	
    }
	
    @Test(expected=JAXBException.class)
    public void badPath() throws JAXBException{
    	CrawlerEntriesParser parser = new CrawlerEntriesParser();
    	output = parser.parseFromXML("doesnotexist");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void noPath() throws JAXBException{
    	CrawlerEntriesParser parser = new CrawlerEntriesParser();
    	output = parser.parseFromXML("");
    }
    
    @Test
    public void shouldParseNumberOfSources(){
    	int number = 0;
    	for(SourceFile f : output.getSourceFiles()){
	    	number += f.getClasses().size();
    	}
    	assertEquals(number, 296);
    }
    
    @Test
    public void shouldParseAFile(){
    	SourceFile sourceFile = output.getSourceFiles().get(13);
    	assertEquals(sourceFile.getSourcePackage(), "org.impressivecode.depress.its");
    }
    
    @Test
    public void shouldParseAClass(){
    	boolean found = false;
    	for(SourceFile f : output.getSourceFiles()){
    		for(Clazz c : f.getClasses()){
	    		if(c.getName().equals("JaCoCoEntry")) found = true;
    		}
    	}
    	assertTrue(found);
    }
    
    @Test
    public void shouldParseInner(){
    	boolean inner = false;
    	for(SourceFile f : output.getSourceFiles()){
    		for(Clazz c : f.getClasses()){
	    		if(c.getName().equals("MarkerCellFactory")){
	    			inner = c.isInner();
	    		}
    		}
    	}
    	assertTrue(inner);
    }
    
    @Test
    public void shouldParseTest(){
    	boolean test = false;
    	for(SourceFile f : output.getSourceFiles()){
    		for(Clazz c : f.getClasses()){
	    		if(c.getName().equals("BugzillaEntriesParserTest")){
	    			test = c.isTest();
	    		}
    		}
    	}
    	assertTrue(test);
    }
    
    @Test
    public void shouldParseEnum(){
    	String type = "NotEnum";
    	for(SourceFile f : output.getSourceFiles()){
    		for(Clazz c : f.getClasses()){
	    		if(c.getName().equals("SCMOperation")){
	    			type = c.getType();
	    		}
    		}
    	}
    	assertEquals(type, "Enum");
    }
    	
    @Test
    public void shouldParseFiltered(){
    	CrawlerOptionsParser optionsParser = createOptionsParser(false, false, true, true, 
    			true, false, true, false, false, true, true, true, "org.impressivecode.depress.scm");
    	optionsParser.checkRequirements(output);
    	int number = 0;
    	for(SourceFile f : output.getSourceFiles()){
	    	number += f.getClasses().size();
    	}
    	assertEquals(number, 40);
    }
    


	@Test
    public void shouldParseFiltered2(){
    	CrawlerOptionsParser optionsParser = createOptionsParser(true, true, false, false, 
    			false, true, false, true, true, false, false, false, "org.");
    	optionsParser.checkRequirements(output);
    	int number = 0;
    	for(SourceFile f : output.getSourceFiles()){
	    	number += f.getClasses().size();
    	}
    	assertEquals(number, 3);
    }
	
    private CrawlerOptionsParser createOptionsParser(final boolean publi, final boolean privat,
    		 final boolean protecte, final boolean packag, final boolean clas, final boolean interfac,
    		 final boolean abstrac, final boolean enu, final boolean exception, final boolean inner,
    		 final boolean test, final boolean fina, final String packageName) {
    	Hashtable<String, SettingsModelBoolean> booleanSettings = new Hashtable<String, SettingsModelBoolean>();
        booleanSettings.put(CrawlerAdapterNodeModel.PUBLIC, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.PUBLIC_CONFIG, publi));
        booleanSettings.put(CrawlerAdapterNodeModel.PRIVATE, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.PRIVATE_CONFIG, privat));
        booleanSettings.put(CrawlerAdapterNodeModel.PROTECTED, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.PROTECTED_CONFIG, protecte));
        booleanSettings.put(CrawlerAdapterNodeModel.PACKAGE, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.PACKAGE_CONFIG, packag));
        booleanSettings.put(CrawlerAdapterNodeModel.CLASS, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.CLASS_CONFIG, clas));
        booleanSettings.put(CrawlerAdapterNodeModel.INTERFACE, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.INTERFACE_CONFIG, interfac));
        booleanSettings.put(CrawlerAdapterNodeModel.ABSTRACT, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.ABSTRACT_CONFIG, abstrac));
        booleanSettings.put(CrawlerAdapterNodeModel.ENUM, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.ENUM_CONFIG, enu));
        booleanSettings.put(CrawlerAdapterNodeModel.EXCEPTION, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.EXCEPTION_CONFIG, exception));
        booleanSettings.put(CrawlerAdapterNodeModel.INNER, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.INNER_CONFIG, inner));
        booleanSettings.put(CrawlerAdapterNodeModel.TEST, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.TEST_CONFIG, test));
        booleanSettings.put(CrawlerAdapterNodeModel.FINAL, CrawlerAdapterNodeModel.createSettingsModel(CrawlerAdapterNodeModel.FINAL_CONFIG, fina));
        
        SettingsModelString packageSettings = CrawlerAdapterNodeModel.createPackageSettings();
        packageSettings.setStringValue(packageName);
        
    	CrawlerOptionsParser optionsParser = new CrawlerOptionsParser(booleanSettings, packageSettings);
    	
    	return optionsParser;
	}

}
