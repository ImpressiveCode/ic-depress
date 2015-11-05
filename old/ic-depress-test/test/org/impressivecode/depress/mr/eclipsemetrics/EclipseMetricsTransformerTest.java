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
package org.impressivecode.depress.mr.eclipsemetrics;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTableFactory.createDataColumnSpecMethodLevel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryClassLevel;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryMethodLevel;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTransformer;
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
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsTransformerTest {

    @Test
    public void shouldTransformEclipseMetricsEntriesList() throws CanceledExecutionException {
        // given
        EclipseMetricsTransformer transformer = new EclipseMetricsTransformer(createDataColumnSpec());
        List<EclipseMetricsEntryClassLevel> entries = Lists.newArrayList(
                createEclipseMetricsEntryClassLevel("ClassA", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0),
                createEclipseMetricsEntryClassLevel("ClassB", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transformClassLevel(entries, exec);

        // then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    @Test
    public void shouldTransformEclipseMetricsEntryClassLevel() throws CanceledExecutionException {
        // given
        EclipseMetricsTransformer transformer = new EclipseMetricsTransformer(createDataColumnSpec());
        List<EclipseMetricsEntryClassLevel> entries = Lists.newArrayList(createEclipseMetricsEntryClassLevel("ClassA",
                1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transformClassLevel(entries, exec);

        // then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(value.getKey().getString()).isEqualTo("ClassA");
        assertThat(((DoubleCell) value.getCell(0)).getDoubleValue()).isEqualTo(1.0);
        assertThat(((DoubleCell) value.getCell(1)).getDoubleValue()).isEqualTo(2.0);
        assertThat(((DoubleCell) value.getCell(2)).getDoubleValue()).isEqualTo(3.0);
        assertThat(((DoubleCell) value.getCell(3)).getDoubleValue()).isEqualTo(4.0);
        assertThat(((DoubleCell) value.getCell(4)).getDoubleValue()).isEqualTo(5.0);
        assertThat(((DoubleCell) value.getCell(5)).getDoubleValue()).isEqualTo(6.0);
        assertThat(((DoubleCell) value.getCell(6)).getDoubleValue()).isEqualTo(7.0);
        assertThat(((DoubleCell) value.getCell(7)).getDoubleValue()).isEqualTo(8.0);
        assertThat(((DoubleCell) value.getCell(8)).getDoubleValue()).isEqualTo(9.0);
        assertThat(((DoubleCell) value.getCell(9)).getDoubleValue()).isEqualTo(10.0);
    }

    private EclipseMetricsEntryClassLevel createEclipseMetricsEntryClassLevel(final String className, final double d,
            final double e, final double f, final double g, final double h, final double i, final double j,
            final double k, final double l, final double m) {
        EclipseMetricsEntryClassLevel entry = new EclipseMetricsEntryClassLevel();
        entry.setClassName(className);

        entry.setNumberOfOverriddenMethods(d);
        entry.setNumberOfAttributes(e);
        entry.setNumberOfChildren(f);
        entry.setNumberOfMethods(g);
        entry.setDepthOfInheritanceTree(h);
        entry.setLackOfCohesionOfMethods(i);
        entry.setNumberOfStaticMethods(j);
        entry.setSpecializationIndex(k);
        entry.setWeightedMethodsPerClass(l);
        entry.setNumberOfStaticAttributes(m);

        return entry;
    }

    @Test
    public void shouldTransformEclipseMetricsEntryMethodLevel() throws CanceledExecutionException {
        // given
        EclipseMetricsTransformer transformer = new EclipseMetricsTransformer(createDataColumnSpecMethodLevel());
        List<EclipseMetricsEntryMethodLevel> entries = Lists.newArrayList(createEclipseMetricsEntryMethodLevel(
                "methodA", 1.0, 2.0, 3.0, 4.0));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transformMethodLevel(entries, exec);

        // then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(value.getKey().getString()).isEqualTo("ClassA");
        assertThat(((DoubleCell) value.getCell(0)).getDoubleValue()).isEqualTo(1.0);
        assertThat(((DoubleCell) value.getCell(1)).getDoubleValue()).isEqualTo(2.0);
        assertThat(((DoubleCell) value.getCell(2)).getDoubleValue()).isEqualTo(3.0);
        assertThat(((DoubleCell) value.getCell(3)).getDoubleValue()).isEqualTo(4.0);
        assertThat(((DoubleCell) value.getCell(4)).getDoubleValue()).isEqualTo(5.0);
        assertThat(((DoubleCell) value.getCell(5)).getDoubleValue()).isEqualTo(6.0);
        assertThat(((DoubleCell) value.getCell(6)).getDoubleValue()).isEqualTo(7.0);
        assertThat(((DoubleCell) value.getCell(7)).getDoubleValue()).isEqualTo(8.0);
        assertThat(((DoubleCell) value.getCell(8)).getDoubleValue()).isEqualTo(9.0);
        assertThat(((DoubleCell) value.getCell(9)).getDoubleValue()).isEqualTo(10.0);
    }

    private EclipseMetricsEntryMethodLevel createEclipseMetricsEntryMethodLevel(final String methodName,
            final double d, final double e, final double f, final double g) {
        EclipseMetricsEntryMethodLevel entry = new EclipseMetricsEntryMethodLevel();
        entry.setMethodName(methodName);

        entry.setMethodLinesOfCode(d);
        entry.setNestedBlockDepth(e);
        entry.setMcCabeCyclomaticComplexity(f);
        entry.setNumberOfParameters(g);

        return entry;
    }
}
