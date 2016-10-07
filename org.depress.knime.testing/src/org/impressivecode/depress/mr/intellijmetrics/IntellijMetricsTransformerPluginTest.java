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
package org.impressivecode.depress.mr.intellijmetrics;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.mr.intellijmetrics.IntellijMetricsTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import org.junit.Test;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.collect.Lists;

/**
 * 
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 * 
 */
public class IntellijMetricsTransformerPluginTest {

    @Test
    public void shouldTransformIntellijMetricsEntriesList() throws CanceledExecutionException {
        // given
        IntellijMetricsTransformer transformer = new IntellijMetricsTransformer(createDataColumnSpec());
        List<IntellijMetricsEntry> entries = Lists.newArrayList(createIntellijMetricsEntry("ClassA", 1, 2, 3, 4, 5, 6),
                createIntellijMetricsEntry("ClassB", 1, 2, 3, 4, 5, 6));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    @Test
    public void shouldTransformIntellijMetricsEntries() throws CanceledExecutionException {
        // given
        IntellijMetricsTransformer transformer = new IntellijMetricsTransformer(createDataColumnSpec());
        List<IntellijMetricsEntry> entries = Lists.newArrayList(createIntellijMetricsEntry("ClassA", 1, 2, 3, 4, 5, 6));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        System.out.println(value);
        assertThat(value.getKey().getString()).isEqualTo("ClassA");
        
        //new DataColumnSpecCreator(TYPO_SEVERITY, DoubleCell.TYPE).createSpec(),
        //new DataColumnSpecCreator(INFO_SEVERITY, DoubleCell.TYPE).createSpec(),
        //new DataColumnSpecCreator(SERVERPROBLEM_SEVERITY, DoubleCell.TYPE).createSpec(),
        //new DataColumnSpecCreator(WEAKWARNING_SEVERITY, DoubleCell.TYPE).createSpec(),
        //new DataColumnSpecCreator(WARNING_SEVERITY, DoubleCell.TYPE).createSpec(),
        //new DataColumnSpecCreator(ERROR_SEVERITY, DoubleCell.TYPE).createSpec()};        
        assertThat(((IntCell) value.getCell(0)).getIntValue()).isEqualTo(4);
        assertThat(((IntCell) value.getCell(1)).getIntValue()).isEqualTo(2);
        assertThat(((IntCell) value.getCell(2)).getIntValue()).isEqualTo(3);
        assertThat(((IntCell) value.getCell(3)).getIntValue()).isEqualTo(6);
        assertThat(((IntCell) value.getCell(4)).getIntValue()).isEqualTo(5);
        assertThat(((IntCell) value.getCell(5)).getIntValue()).isEqualTo(1);
    }

    private IntellijMetricsEntry createIntellijMetricsEntry(final String className, final int error, final int info,
            final int serverProblem, final int typo, final int warning, final int weakWarning) {
        IntellijMetricsEntry entry = new IntellijMetricsEntry();
        entry.setClassName(className);

        entry.setValue("ERROR", error);
        entry.setValue("INFO", info);
        entry.setValue("SERVER PROBLEM", serverProblem);
        entry.setValue("TYPO", typo);
        entry.setValue("WARNING", warning);
        entry.setValue("WEAK WARNING", weakWarning);

        return entry;
    }

}
