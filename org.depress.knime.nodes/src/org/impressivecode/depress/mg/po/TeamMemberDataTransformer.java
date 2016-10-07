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
package org.impressivecode.depress.mg.po;

import java.util.Map;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
class TeamMemberDataTransformer {
    private final DataTableSpec developersDataSpec;

    public TeamMemberDataTransformer(final DataTableSpec developersDataSpec) {
        Preconditions.checkNotNull(developersDataSpec, "table specifikation can not be null.");
        this.developersDataSpec = developersDataSpec;
    }

    public Map<String, TeamMemberData> transformEngineerData(final BufferedDataTable devTable,
            final ExecutionContext exec) throws CanceledExecutionException {
        Map<String, TeamMemberData> devMap = Maps.newHashMap();
        CloseableRowIterator iterator = devTable.iterator();
        while (iterator.hasNext()) {
            progress(exec);
            TeamMemberData value = transform(iterator.next());
            assertIfEngineerIsUnique(devMap, value);
            devMap.put(value.getName(), value);
        }
        return devMap;
    }

    private void assertIfEngineerIsUnique(final Map<String, TeamMemberData> devMap, final TeamMemberData value) {
        if (devMap.containsKey(value.getName())) {
            throw new IllegalArgumentException("Engineers have to be unique. Engineer data:[" + value.getName() + "]");
        }
    }

    private TeamMemberData transform(final DataRow row) {
        TeamMemberData dev = new TeamMemberData();
        dev.setExLevel(extractExLevel(row));
        dev.setExEngineer(extractExEngineer(row));
        dev.setName(extractName(row));
        dev.setOrganizationPath(extractOrganizationPath(row));
        return dev;
    }

    private String extractOrganizationPath(final DataRow row) {
        return ((StringCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.ORGANIZATION_STRUCTURE))).getStringValue();
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
        int value = ((IntCell) row.getCell(developersDataSpec
                .findColumnIndex(PeopleOrganizationMetricTableFactory.EX_ENGINEER))).getIntValue();
        return value == 1 ? true : false;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
