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
package org.impressivecode.depress.its;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.ISSUE_ID_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.RESOURCE_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.impressivecode.depress.common.Cells;
import org.impressivecode.depress.common.InputTransformer;
import org.junit.Test;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.mockito.Mockito;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ITSInputTransformerTest {

    @Test(expected = InvalidSettingsException.class)
    public void shouldValidateSpec() throws InvalidSettingsException {
        // given
        DataTableSpec spec = new DataTableSpec(new DataColumnSpecCreator(RESOURCE_NAME, StringCell.TYPE).createSpec());
        DataTableSpec minSpec = new DataTableSpec(new DataColumnSpecCreator(RESOURCE_NAME, IntCell.TYPE).createSpec());
        // when
        new ITSInputTransformer().setInputSpec(spec).setMinimalSpec(minSpec).validate();
    }

    @Test
    public void shoulTransform() throws InvalidSettingsException {
        // given
        DataTableSpec spec = new DataTableSpec(ISSUE_ID_COLSPEC);
        InputTransformer<ITSDataType> transformer = new ITSInputTransformer().setMinimalSpec(spec).setInputSpec(spec);
        DataTable inTable = mock(DataTable.class);
        RowIterator iterator = mock(RowIterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        DataRow row = mock(DataRow.class);
        when(row.getCell(Mockito.eq(0))).thenReturn(Cells.stringCell("ID"));
        when(iterator.next()).thenReturn(row);
        when(inTable.iterator()).thenReturn(iterator );
        // when
        List<ITSDataType> data = transformer.transform(inTable );
        // then
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getIssueId()).isEqualTo("ID");
    }

}
