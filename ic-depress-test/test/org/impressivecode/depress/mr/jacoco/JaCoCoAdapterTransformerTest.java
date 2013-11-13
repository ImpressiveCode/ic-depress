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
package org.impressivecode.depress.mr.jacoco;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.mr.jacoco.JaCoCoAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.impressivecode.depress.mr.jacoco.JaCoCoAdapterTransformer;
import org.impressivecode.depress.mr.jacoco.JaCoCoEntry;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoAdapterTransformerTest {

    @Test
    public void shouldTransformJacocoEntriesList() throws CanceledExecutionException {
        //given
        JaCoCoAdapterTransformer transformer = new JaCoCoAdapterTransformer(createDataColumnSpec());
        List<JaCoCoEntry> entries = Lists.newArrayList(createJaCoCoEntry("ClassA", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                createJaCoCoEntry("ClassB", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        //when
        transformer.transform(entries, exec);

        //then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    @Test
    public void shouldTransformJacocoEntry() throws CanceledExecutionException {
        //given
        JaCoCoAdapterTransformer transformer = new JaCoCoAdapterTransformer(createDataColumnSpec());
        List<JaCoCoEntry> entries = Lists.newArrayList(createJaCoCoEntry("ClassA", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        //when
        transformer.transform(entries, exec);

        //then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(value.getKey().getString()).isEqualTo("ClassA");
        //        new DataColumnSpecCreator(LINE_COVERAGE, DoubleCell.TYPE).createSpec(),
        //        new DataColumnSpecCreator(INSTRUCTIONS_COVERAGE, DoubleCell.TYPE).createSpec(),
        //        new DataColumnSpecCreator(BRANCHE_COVERAGE, DoubleCell.TYPE).createSpec(),
        //        new DataColumnSpecCreator(COMPLEXITY_COVERAGE, DoubleCell.TYPE).createSpec(),
        //        new DataColumnSpecCreator(METHOD_COVERAGE, DoubleCell.TYPE).createSpec(), 
        //        new DataColumnSpecCreator(CLASS_COVERAGE, DoubleCell.TYPE).createSpec()};
        assertThat(((DoubleCell)value.getCell(2)).getDoubleValue()).isEqualTo(1.0);
        assertThat(((DoubleCell)value.getCell(5)).getDoubleValue()).isEqualTo(2.0);
        assertThat(((DoubleCell)value.getCell(3)).getDoubleValue()).isEqualTo(3.0);
        assertThat(((DoubleCell)value.getCell(1)).getDoubleValue()).isEqualTo(4.0);
        assertThat(((DoubleCell)value.getCell(0)).getDoubleValue()).isEqualTo(5.0);
        assertThat(((DoubleCell)value.getCell(4)).getDoubleValue()).isEqualTo(6.0);
    }

    private JaCoCoEntry createJaCoCoEntry(final String className, final double d, final double e, final double f, final double g, final double h,
            final double i) {
        JaCoCoEntry entry = new JaCoCoEntry();
        entry.setClassName(className);

        entry.setBranchCoverageCounter(d);
        entry.setClassCoverageCounter(e);
        entry.setComplexityCoverageCounter(f);
        entry.setInstructionCoverageCounter(g);
        entry.setLineCoverageCounter(h);
        entry.setMethodCoverageCounter(i);
        return entry;
    }
}
