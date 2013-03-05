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

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
class ChangeHistoryTransformer {
    private final DataTableSpec changeHistory;
    private final Map<String, TeamMemberData> members;

    public ChangeHistoryTransformer(final DataTableSpec changeHistory, final Map<String, TeamMemberData> members) {
        Preconditions.checkNotNull(changeHistory, "table specifikation can not be null.");
        this.changeHistory = changeHistory;
        this.members = members;
    }

    public POData transform(final DataRow row) {
        POData data = new POData();
        data.setClassName(extractClassName(row));
        data.getInvolvedDevelopers().add(extractAuthor(row));
        return data;
    }

    private String extractClassName(final DataRow row) {
        return ((StringCell) row.getCell(changeHistory.findColumnIndex(SCMAdapterTableFactory.CLASS_COLNAME)))
                .getStringValue();
    }

    private TeamMemberData extractAuthor(final DataRow row) {
        String name = ((StringCell) row.getCell(changeHistory.findColumnIndex(SCMAdapterTableFactory.AUTHOR_COLNAME)))
                .getStringValue();
        return members.get(name);
    }
}
