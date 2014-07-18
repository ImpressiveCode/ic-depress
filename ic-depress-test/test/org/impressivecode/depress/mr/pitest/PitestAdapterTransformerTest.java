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
package org.impressivecode.depress.mr.pitest;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.mr.pitest.PitestAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.impressivecode.depress.mr.pitest.PitestAdapterTransformer;
import org.impressivecode.depress.mr.pitest.PitestEntry;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.knime.core.node.NodeLogger;
import com.google.common.collect.Lists;
/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Polska
 * 
 */
public class PitestAdapterTransformerTest {

    @Test
    public void shouldTransformPitestEntriesList() throws CanceledExecutionException {
        //given
    	PitestAdapterTransformer transformer = new PitestAdapterTransformer(PitestAdapterTableFactory.createDataColumnSpec());
        List<PitestEntry> entries = Lists.newArrayList(createPitestEntry("KILLED", true, "java.java", "Foo", "Bar", "TODO", 2, "mutator", 0, null),
                createPitestEntry("SURVIVED", false, "Class.java", "Foo1", "Bar1", "TODO", 1, "mutator1", 0, "test"));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        //when
        transformer.transform(entries, exec);

        //then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    @Test
    public void shouldTransformPitestEntry() throws CanceledExecutionException {
        //given
    	PitestAdapterTransformer transformer = new PitestAdapterTransformer(createDataColumnSpec());
        List<PitestEntry> entries = Lists.newArrayList(createPitestEntry("KILLED", true, "java.java", "Foo", "Bar", "TODO", 2, "mutator", 0, null));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        //when
        transformer.transform(entries, exec);

        //then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(value.getKey().getString()).isEqualTo("KILLED");
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

    private PitestEntry createPitestEntry(final String mutationStatus, final boolean detection, final String sourceFile, final String mutatedClass, 
    		final String mutatedMethod, final String methodDescription, final int lineNumber, final String mutator, final int index, final String killingTest) {
    	PitestEntry entry = new PitestEntry();
        
    	entry.setMutationStatus(mutationStatus);
    	entry.setDetection(detection);
    	entry.setSourceFile(sourceFile);
    	entry.setMutatedClass(mutatedClass);
    	entry.setMutatedMethod(mutatedMethod);
    	entry.setMethodDescription(methodDescription);
    	entry.setLineNumber(lineNumber);
    	entry.setMutator(mutator);
    	entry.setIndex(index);
    	entry.setKillingTest(killingTest);
        return entry;
    }
}
