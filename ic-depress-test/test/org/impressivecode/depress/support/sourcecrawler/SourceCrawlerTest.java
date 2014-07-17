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

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

public class SourceCrawlerTest {
	
	private final static String testedFilePath = SourceCrawlerTest.class.getResource("test.xml").getPath();
    
	SourceCrawlerOutput output;
	
	@Before
    public void setUp() throws JAXBException {
    	CrawlerEntriesParser parser = new CrawlerEntriesParser();
    	output = parser.parseFromXML(testedFilePath);	
    }
    
    @Test
    public void shouldParseNumberOfSources(){
    	int number = 0;
    	for(SourceFile f : output.getSourceFiles()){
	    	number += f.getClasses().size();
    	}
    	assertEquals(number, 293);
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


}
