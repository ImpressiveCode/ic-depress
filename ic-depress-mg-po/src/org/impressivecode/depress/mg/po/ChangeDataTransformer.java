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

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
class ChangeDataTransformer {
    private final DataTableSpec changeHistory;

    public ChangeDataTransformer(final DataTableSpec changeHistory) {
        Preconditions.checkNotNull(changeHistory, "table specifikation can not be null.");
        this.changeHistory = changeHistory;
    }

    public Map<String, ChangeData> transformChangeData(final BufferedDataTable changeHistory,
            final ExecutionContext exec) throws CanceledExecutionException {
        Map<String, ChangeData> changeData = Maps.newHashMapWithExpectedSize(1000);
        CloseableRowIterator iterator = changeHistory.iterator();
        while (iterator.hasNext()) {
            progress(exec);
            ChangeData curr = transform(iterator.next());
            String key = curr.getClassName();
            changeData.put(key, mergeChangeData(changeData.get(key), curr));
        }
        iterator.close();
        return changeData;
    }

    private ChangeData mergeChangeData(final ChangeData old, final ChangeData curr) {
        if (old == null) {
            return curr;
        } else {
            old.getInvolvedEngineers().addAll(curr.getInvolvedEngineers());
            return old;
        }
    }

    public ChangeData transform(final DataRow row) {
        ChangeData data = new ChangeData();
        data.setClassName(extractClassName(row));
        data.setInvolvedEngineers(Lists.newArrayList(extractAuthor(row)));
        return data;
    }

    private String extractClassName(final DataRow row) {
        return ((StringCell) row.getCell(changeHistory.findColumnIndex(SCMAdapterTableFactory.RESOURCE_NAME)))
                .getStringValue();
    }

    private String extractAuthor(final DataRow row) {
        return ((StringCell) row.getCell(changeHistory.findColumnIndex(SCMAdapterTableFactory.AUTHOR_COLNAME)))
                .getStringValue();
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
