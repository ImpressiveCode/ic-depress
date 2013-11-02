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

import org.impressivecode.depress.its.ITSDataHolder;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class Configuration {
	
	public static final String MSC_DT_SUMMARY = "SUMMARY";
	public static final String MSC_DT_DESCRIPTION = "DESCRIPTION";
	public static final String MSC_DT_COMMENTS = "COMMENTS";
	
	public static final String[] MSC_DATA_TYPE = { MSC_DT_SUMMARY, MSC_DT_DESCRIPTION, MSC_DT_COMMENTS };
	
    private final ITSDataHolder itsData;
    private final int authorWeight;
    private final int resolutionWeight;
    private final int comparsionsLimit;
    private final String mscComparsionObject;
    
    public Configuration(final ITSDataHolder itsData, final int authorWeight, final int resolutionWeight, final int comparsionLimit, final String mscComparsionObject) {
        this.itsData = itsData;
        this.authorWeight = authorWeight;
        this.resolutionWeight = resolutionWeight;
        this.comparsionsLimit = comparsionLimit;
        this.mscComparsionObject = mscComparsionObject;
    }

    ITSDataHolder getITSData() {
        return this.itsData;
    }

    public int getAuthorWeight() {
        return authorWeight;
    }

    public int getResolutionWeight() {
        return this.resolutionWeight;
    }
    
    public int getComparsionLimit() {
    	return this.comparsionsLimit;
    }
    
    public String getMcComparsionObject() {
    	return this.mscComparsionObject;
    }
    
}