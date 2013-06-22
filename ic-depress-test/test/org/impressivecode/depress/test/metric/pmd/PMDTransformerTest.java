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
package org.impressivecode.depress.test.metric.pmd;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.metric.pmd.PMDAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.impressivecode.depress.metric.pmd.PMDAdapterTransformer;
import org.impressivecode.depress.metric.pmd.PMDEntry;
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
 * @author £ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDTransformerTest {

    @Test
    public void shouldTransformCheckStyleEntriesList() throws CanceledExecutionException {
        // given
        PMDAdapterTransformer transformer = new PMDAdapterTransformer(createDataColumnSpec());
        List<PMDEntry> entries = Lists.newArrayList(
                createPMDEntry("file1.name", "10", "4", "6", "6", "aRule", "aRuleSet", "(customPackage)", "customClass", "nonexistent.url", "8", "this is an example message1"),
                createPMDEntry("file2.name", "12", "6", "8", "8", "aRule2", "aRuleSet2", "(customPackage2)", "customClass2", "nonexistent2.url", "10", "this is an example message2"));
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
        List<PMDEntry> entries = Lists.newArrayList(createPMDEntry("Yang.java",
                "2", "2","0", "0", "MethodArgumentCouldBeFinal", "Optimization Rules", "(default)", "Yang", "http://pmd.sourceforge.net/rules/optimizations.html#MethodArgumentCouldBeFinal", "3", "Parameter 'str' is not assigned and could be declared final"));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        ArgumentCaptor<DataRow> captor = ArgumentCaptor.forClass(DataRow.class);
        verify(container).addRowToTable(captor.capture());
        DataRow value = captor.getValue();
        assertThat(((StringCell) value.getCell(0)).getStringValue()).isEqualTo("test_file.name");
        assertThat(((StringCell) value.getCell(1)).getStringValue()).isEqualTo("10");
        assertThat(((StringCell) value.getCell(2)).getStringValue()).isEqualTo("4");
        assertThat(((StringCell) value.getCell(3)).getStringValue()).isEqualTo("7");
        assertThat(((StringCell) value.getCell(4)).getStringValue()).isEqualTo("7");
        assertThat(((StringCell) value.getCell(5)).getStringValue()).isEqualTo("sample_rule");
        assertThat(((StringCell) value.getCell(6)).getStringValue()).isEqualTo("sample_ruleSet");
        assertThat(((StringCell) value.getCell(7)).getStringValue()).isEqualTo("(custom)");
        assertThat(((StringCell) value.getCell(8)).getStringValue()).isEqualTo("aClass");
        assertThat(((StringCell) value.getCell(9)).getStringValue()).isEqualTo("a.url.test");
        assertThat(((StringCell) value.getCell(10)).getStringValue()).isEqualTo("9");
        assertThat(((StringCell) value.getCell(11)).getStringValue()).isEqualTo("this is an example message");
        
    }

    private PMDEntry createPMDEntry(final String fileName, final String d,
            final String e, final String f, final String g, final String h, final String i, 
            final String j, final String k, final String l, final String m, final String n) {
        PMDEntry entry = new PMDEntry();
        
        entry.setFileName(fileName);
        entry.setBeginLine(d);
        entry.setEndLine(e);
        entry.setBeginColumn(f);
        entry.setEndColumn(g);
        entry.setRule(h);
        entry.setRuleSet(i);
        entry.setPackageName(j);
        entry.setClassName(k);
        entry.setInfoUrl(l);
        entry.setPriority(m);
        entry.setMessageText(n);
        
        return entry;
    }

}
