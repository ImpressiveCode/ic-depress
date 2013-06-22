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
package org.impressivecode.depress.metric.checkstyle;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.metric.checkstyle.CheckStyleAdapterTableFactory.createDataColumnSpec;
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
 * @author �ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class CheckStyleTransformerTest {

    @Test
    public void shouldTransformCheckStyleEntriesList() throws CanceledExecutionException {
        // given
        CheckStyleAdapterTransformer transformer = new CheckStyleAdapterTransformer(createDataColumnSpec());
        List<CheckStyleEntry> entries = Lists.newArrayList(
                createCheckStyleEntry("file1.name", "10", "4", "this is an example message1", "error_severity1", "\\source\\file.ext1"),
                createCheckStyleEntry("file2.name", "21", "14", "this is an example message2", "error_severity2", "\\source\\file.ext2"));
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
        CheckStyleAdapterTransformer transformer = new CheckStyleAdapterTransformer(createDataColumnSpec());
        List<CheckStyleEntry> entries = Lists.newArrayList(createCheckStyleEntry("test_file.name",
                "10", "4", "this is an example message", "error_severity", "\\source\\file.ext"));
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
        assertThat(((StringCell) value.getCell(3)).getStringValue()).isEqualTo("error_severity");
        assertThat(((StringCell) value.getCell(4)).getStringValue()).isEqualTo("this is an example message");
        assertThat(((StringCell) value.getCell(5)).getStringValue()).isEqualTo("\\source\\file.ext");
    }

    private CheckStyleEntry createCheckStyleEntry(final String fileName, final String d,
            final String e, final String f, final String g, final String h) {
        CheckStyleEntry entry = new CheckStyleEntry();
        entry.setFileName(fileName);

        entry.setLineNumber(d);
        entry.setColumnNumber(e);
        entry.setMessageText(f);
        entry.setSeverityType(g);
        entry.setSourcePlace(h);

        return entry;
    }

}
