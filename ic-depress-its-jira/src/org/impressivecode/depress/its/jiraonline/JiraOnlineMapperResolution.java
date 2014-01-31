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
package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;

/**
 * 
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public class JiraOnlineMapperResolution extends JiraOnlineMapperAbstractCustomField {

    public JiraOnlineMapperResolution(List<JiraOnlineFilterListItem> fieldList) {
        super(fieldList);
    }

    private static final String JIRA_RESOLUTION = "depress.its.jiraonline.resolutionList";

    @Override
    protected String getMapperModelString() {
        return JIRA_RESOLUTION;
    }

    @Override
    protected Collection<String> getImplementedMappings() {
        Collection<String> statuses = new ArrayList<String>();
        for (ITSResolution statusEnum : ITSResolution.values()) {
            statuses.add(statusEnum.toString());
        }
        return statuses;
    }

    @Override
    protected String getParserValue(String resolution) {
        return JiraOnlineAdapterParser.parseResolutionFromEnum(resolution).toString();
    }
}
