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
package org.impressivecode.depress.support.sematicanalysis;

import static org.impressivecode.depress.common.Cells.integerOrMissingCell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.support.commonmarker.MarkerDataType;
import org.impressivecode.depress.support.commonmarker.MarkerInputTransformer;
import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;

import uk.ac.shef.wit.simmetrics.similaritymetrics.ChapmanLengthDeviation;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.OverlapCoefficient;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SemanticAnalysisCellFactory implements AppendedCellFactory {

    private final Configuration cfg;
    private final InputTransformer<SCMDataType> scmTransfomer;
    private MarkerInputTransformer markerTransformer;

    public SemanticAnalysisCellFactory(final Configuration configuration,
            final InputTransformer<SCMDataType> scmTransfomer, final MarkerInputTransformer markerTransformer) {
        this.cfg = configuration;
        this.scmTransfomer = Preconditions.checkNotNull(scmTransfomer, "ScmTransfomer has to be set");
        this.markerTransformer = Preconditions.checkNotNull(markerTransformer, "MarkerTransformer has to be set");
        ;
    }

    @Override
    public DataCell[] getAppendedCell(final DataRow row) {

        SCMDataType scm = scmTransfomer.transformRow(row);
        MarkerDataType marker = markerTransformer.transformRow(row);
        try{
        	Integer confidence = checkConfidence(scm, marker);
        	return new DataCell[] { integerOrMissingCell(confidence) };
        } catch(Exception e){
        	return null;
        }
        
    }

    private int checkConfidence(final SCMDataType scm, final MarkerDataType marker) throws Exception {
        Set<ITSDataType> issues = this.cfg.getITSData().issues(marker.getAllMarkers());
        if (issues.isEmpty()) {
            return 0;
        } else {
        	Set<ITSDataType> similarIssues = checkSimilarity(issues, scm);
        	
            return checkAuthor(scm, similarIssues) + checkResolution(similarIssues);
        }
    }
    
    private Set<ITSDataType> checkSimilarity(final Set<ITSDataType> issues, final SCMDataType scm) throws Exception
    {
    	String selectedAlgorithm = this.cfg.getSelectedAlgorithm();
    	String comparisionObject = this.cfg.getMcComparsionObject();
    	String message = scm.getMessage();
    	
    	Iterator<ITSDataType> issuesIterator = issues.iterator();
    	Set<ITSDataType> similarIssues = new HashSet<ITSDataType>();
    	
    	double threshold = this.cfg.getComparsionLimit();
    	double similarity = -1;
    	while(issuesIterator.hasNext())
    	{
    		ITSDataType issue;
    		issue = issuesIterator.next();
    		if(comparisionObject.equals(Configuration.MSC_DT_COMMENTS)){
    			int numberOfComments = issue.getComments().size();
    			if(numberOfComments > 0){
	    		   for(int i = 0; i<numberOfComments; i++){
    				  String comment = issue.getComments().get(i);
    				  if(comment == null || message ==null) continue;
    				  similarity = DoSimilarityTest(message, comment, selectedAlgorithm);
    				  if(similarity > threshold){
    					break;
	    			   }
	    			}
    			} 
    		} else if(comparisionObject.equals(Configuration.MSC_DT_DESCRIPTION)){
    			String description = issue.getDescription();
    			if(message != null && description != null){
    				similarity = DoSimilarityTest(message, description, selectedAlgorithm);
    			}
    		}
    		else if(comparisionObject.equals(Configuration.MSC_DT_SUMMARY)){
    			String summary = issue.getSummary();
    			if(message != null && summary != null){
    				similarity = DoSimilarityTest(message, summary, selectedAlgorithm);
    			}
    		}
    		similarity = similarity * 100;
    		if(similarity > threshold){
    			similarIssues.add(issue);
    		}
    	}
    	return similarIssues;
    }
    
    private double DoSimilarityTest(String string1, String string2, String selectedAlgorithm) throws Exception
    {
    	if(selectedAlgorithm.equals(Configuration.JARO_WINKLER_ALGHORITM)){
    		return DoJaroWinklerTest(string1, string2);
    	} else if(selectedAlgorithm.equals(Configuration.LEVENSTHEIN_ALGHORITM)){
    		return DoLevenstheinTest(string1, string2);
    	} else if(selectedAlgorithm.equals(Configuration.CHAPMAN_ALGHORITM)){
    		return DoChapmanTest(string1, string2);
    	} else if(selectedAlgorithm.equals(Configuration.OVERLAP_ALGHORITM)){
    		return DoOverlapTest(string1, string2);
    	} else{
    		throw new Exception("Unsupported Algorithm!");
    	}
    }
    
    private double DoLevenstheinTest(String string1, String string2) {
    	Levenshtein levenstheinTest = new Levenshtein();
		return levenstheinTest.getSimilarity(string1, string2);
	}

	private double DoJaroWinklerTest(String string1, String string2) {
		JaroWinkler jaroWinklerTest = new JaroWinkler();
		return jaroWinklerTest.getSimilarity(string1, string2);
	}
	
	private double DoChapmanTest(String string1, String string2) {
		ChapmanLengthDeviation chapmanTest = new ChapmanLengthDeviation();
		return chapmanTest.getSimilarity(string1, string2);
	}

	private double DoOverlapTest(String string1, String string2) {
		OverlapCoefficient overlapTest = new OverlapCoefficient();
		return overlapTest.getSimilarity(string1, string2);
	}
	private int checkResolution(final Set<ITSDataType> issues) {
        for (ITSDataType its : issues) {
            if (!ITSResolution.FIXED.equals(its.getResolution())) {
                return 0;
            }
        }
        return this.cfg.getResolutionWeight();
    }

    private int checkAuthor(final SCMDataType scm, final Set<ITSDataType> issues) {
        String author = scm.getAuthor();
        for (ITSDataType its : issues) {
            if (!its.getCommentAuthors().contains(author) && !its.getAssignees().contains(author)) {
                return 0;
            }
        }
        return this.cfg.getAuthorWeight();
    }
}