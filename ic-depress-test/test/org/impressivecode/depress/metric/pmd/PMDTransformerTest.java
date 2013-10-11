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
package org.impressivecode.depress.metric.pmd;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.metric.pmd.PMDAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

/**
 * 
 * @author Tomasz Banach
 * @author Łukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDTransformerTest {

    @Test
    public void shouldTransformCheckStyleEntriesList() throws CanceledExecutionException {
        // given
        PMDAdapterTransformer transformer = new PMDAdapterTransformer(createDataColumnSpec());
        List<PMDEntry> entries = Lists.newArrayList(
                createPMDEntry("10", "4", "6", "6", "aRule", "aRuleSet", "customClass",
                        "nonexistent.url", "8", "this is an example message1"),
                        createPMDEntry("12", "6", "8", "8", "aRule2", "aRuleSet2", "customClass2",
                                "nonexistent2.url", "10", "this is an example message2"));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    @Test
    public void shouldTransformCheckStyleEntry() throws CanceledExecutionException {
        // given
        PMDAdapterTransformer transformer = new PMDAdapterTransformer(createDataColumnSpec());
        List<PMDEntry> entries = Lists.newArrayList(createPMDEntry("2", "2", "0", "0",
                "MethodArgumentCouldBeFinal", "Optimization Rules", "Yang",
                "http://pmd.sourceforge.net/rules/optimizations.html#MethodArgumentCouldBeFinal", "3",
                "Parameter 'str' is not assigned and could be declared final"));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(((StringCell) value.getCell(0)).getStringValue()).isEqualTo("Yang");
        assertThat(((StringCell) value.getCell(1)).getStringValue()).isEqualTo("3");
        assertThat(((StringCell) value.getCell(2)).getStringValue()).isEqualTo("2");
        assertThat(((StringCell) value.getCell(3)).getStringValue()).isEqualTo("2");
        assertThat(((StringCell) value.getCell(4)).getStringValue()).isEqualTo("0");
        assertThat(((StringCell) value.getCell(5)).getStringValue()).isEqualTo("0");
        assertThat(((StringCell) value.getCell(6)).getStringValue()).isEqualTo("MethodArgumentCouldBeFinal");
        assertThat(((StringCell) value.getCell(7)).getStringValue()).isEqualTo("Optimization Rules");
        assertThat(((StringCell) value.getCell(8)).getStringValue()).isEqualTo("http://pmd.sourceforge.net/rules/optimizations.html#MethodArgumentCouldBeFinal");
        assertThat(((StringCell) value.getCell(9)).getStringValue()).isEqualTo("Parameter 'str' is not assigned and could be declared final");

    }

    private PMDEntry createPMDEntry(final String beginLine, final String endLine,
            final String beginColumn, final String endColumn, final String rule, final String ruleSet,
            final String className, final String infoUrl, final String priority, final String messageText) {
        PMDEntry entry = new PMDEntry();
        entry.setBeginLine(beginLine);
        entry.setEndLine(endLine);
        entry.setBeginColumn(beginColumn);
        entry.setEndColumn(endColumn);
        entry.setRule(rule);
        entry.setRuleSet(ruleSet);
        entry.setClassName(className);
        entry.setInfoUrl(infoUrl);
        entry.setPriority(priority);
        entry.setMessageText(messageText);

        return entry;
    }

}
