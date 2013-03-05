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
package org.impressivecode.depress.metric.po;

import java.util.Map;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MetricProcessor {

    static void updateOrganization(final POData value, final Map<String, TeamMemberData> devMap) {
        for (String dev : value.getInvolvedDevelopers()) {
            doWithAuthor(value, devMap.get(dev));
        }
    }

    static POData processHistory(final POData existing, final POData newData) {
        if (existing != null) {
            existing.getInvolvedDevelopers().addAll(newData.getInvolvedDevelopers());
            existing.setEF(1 + existing.getEF());
            return existing;
        } else {
            return newData;
        }
    }

    private static void doWithAuthor(final POData value, final TeamMemberData teamMemberData) {

        doWithExpirience(value, teamMemberData);
        doWithExEngineers(value, teamMemberData);
        doWithDepthOfMasterOwnership(value, teamMemberData);

    }

    private static void doWithDepthOfMasterOwnership(final POData value, final TeamMemberData teamMemberData) {
        // TODO Auto-generated method stub

    }

    private static void doWithExEngineers(final POData value, final TeamMemberData teamMemberData) {
        if (teamMemberData.isExEngineer()) {
            value.setNOEE(value.getNOEE() + 1);
        }
    }

    private static void doWithExpirience(final POData value, final TeamMemberData teamMemberData) {
        switch (teamMemberData.getExLevel()) {
        case 1:
            value.setNOE1(value.getNOE1() + 1);
            break;
        case 2:
            value.setNOE1(value.getNOE2() + 1);
            break;
        case 3:
            value.setNOE1(value.getNOE3() + 1);
            break;
        case 4:
            value.setNOE1(value.getNOE4() + 1);
            break;
        default:
            throw new IllegalStateException("Unkown Expirience Level [" + teamMemberData.getExLevel() + "]");
        }
    }
}
