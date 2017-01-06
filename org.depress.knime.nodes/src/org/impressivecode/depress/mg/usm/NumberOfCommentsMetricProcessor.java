package org.impressivecode.depress.mg.usm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.impressivecode.depress.its.common.ITSDataType;
import org.knime.core.data.DataRow;
import org.knime.core.data.collection.ListCell;
/*
 * Stworzyc interface do komunikacji UserStoryMetricProcessor z metoda computeMetric
 * dostaje to samo i zwraca Object
 * tu implementowac go
 */

/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */

public class NumberOfCommentsMetricProcessor implements UserStoryMetricProcessor{

	@Override
	public Object computeMetric(final ITSDataType row) {
        return Arrays.asList(row.getComments().size(), numberCommentsWithLinks(row.getComments()), numberCommentsWithCodeListing(row.getComments()));
	}
	
	private Integer numberCommentsWithLinks(List<String> comments) {
		Integer numberOfLinks = 0;
		for(String comment : comments) {
			if(hasLink(comment)) {
				numberOfLinks++;
			}
		}
		return numberOfLinks;
	}
	
	private boolean hasLink(String c) {
		String patternHTTPString = "(.*)http://(.*)";
		String patternHTTPSString = "(.*)https://(.*)";
		Pattern patternHttp = Pattern.compile(patternHTTPString);
		Pattern patternHttps = Pattern.compile(patternHTTPSString);
		Matcher matcherHttp = patternHttp.matcher(c);
		Matcher matcherHttps = patternHttps.matcher(c);
		return matcherHttp.matches() || matcherHttps.matches();
	}
	
	private Integer numberCommentsWithCodeListing(List<String> comments) {
		return new Long(comments.stream().filter(this::haveCode).count()).intValue();
	}
	
	// TODO: better recognize if code
	private boolean haveCode(String c) {
		return adnotation(c) || haveCodeBlock(c);//c.matches("(.*)function(.*)");
	}
	
	private boolean adnotation(String c) {
		String patternString = "(.*)\\s@\\w(.*)";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(c);
		return matcher.matches();
	}
	
	private boolean haveCodeBlock(String c) {
		String patternString = "(.*)\\{(.*)return(.*)\\}(.*)";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(c);
		return matcher.matches();
	}
	
}
