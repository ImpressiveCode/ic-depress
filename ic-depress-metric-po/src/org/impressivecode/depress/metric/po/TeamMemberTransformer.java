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

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
class TeamMemberTransformer {
    private final DataTableSpec developersDataSpec;

    public TeamMemberTransformer(final DataTableSpec developersDataSpec) {
        Preconditions.checkNotNull(developersDataSpec, "table specifikation can not be null.");
        this.developersDataSpec = developersDataSpec;
    }

    public TeamMemberData transform(final DataRow row) {
        TeamMemberData dev = new TeamMemberData();
        dev.setExLevel(extractExLevel(row));
        dev.setExEngineer(extractExEngineer(row));
        dev.setExternal(extractExternal(row));
        dev.setName(extractName(row));
        return dev;
    }

    private String extractName(final DataRow row) {
        return ((StringCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.DEVELOPER))).getStringValue();
    }

    private int extractExLevel(final DataRow row) {
        return ((IntCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.EXPIERIENCE))).getIntValue();
    }

    private boolean extractExEngineer(final DataRow row) {
        return ((BooleanCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.EX_ENGINEER))).getBooleanValue();
    }

    private boolean extractExternal(final DataRow row) {
        return ((BooleanCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.EXTERNAL_ENGINEER))).getBooleanValue();
    }
}
