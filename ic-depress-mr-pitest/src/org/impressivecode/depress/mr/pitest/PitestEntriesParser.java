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

package org.impressivecode.depress.mr.pitest;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Poland
 * 
 **/

public class PitestEntriesParser {

    public List<PitestEntry> parseEntries(final String path) throws ParserConfigurationException, SAXException,
    IOException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(path);
        NodeList nList = getSourceFileNodes(doc);
        DecimalFormat df = new DecimalFormat("#.##");
        int size = nList.getLength();
        List<PitestEntry> pitestEntries = Lists.newLinkedList();
        for (int i = 0; i < size; i++) {
            Node item = nList.item(i);
            PitestEntry entry = parse(item);
            pitestEntries.add(entry);
        }
        
        for(int i = 0; i< pitestEntries.size(); i++) {
        	int mutations = 1;
        	int passed_mutations = 0;
        	PitestEntry elem = pitestEntries.get(i);
        	if(elem.getDetection() == true)
        		passed_mutations++;
        	for(int j = i+1; j< pitestEntries.size(); j++) {
            	PitestEntry elem1 = pitestEntries.get(j);
            	if(elem.getMutatedClass().equals(elem1.getMutatedClass())) {
            		if(elem1.getDetection() == true) {
        				passed_mutations++;
        			}
        			mutations ++;
        			pitestEntries.remove(elem1);
        			j--;
	
            	}
            	
            }

        	if(mutations != 0)
        		elem.setMutationScoreIndicator(Double.valueOf((df.format((double)passed_mutations/(double)mutations)).replaceAll(",",".")));
        	else
        		elem.setMutationScoreIndicator(0.00);
 
       }
       
        return pitestEntries;
    } 

    private NodeList getSourceFileNodes(final Document doc) {
        return doc.getElementsByTagName("mutation");
    }

    private PitestEntry parse(final Node item) {

        PitestEntry pitest = new PitestEntry();

        Element eItem = (Element) item;

        pitest.setDetection(Boolean.valueOf(eItem.getAttribute("detected")));
    	pitest.setMutatedClass(eItem.getElementsByTagName("mutatedClass").item(0).getTextContent());

        return pitest;
    }

}
