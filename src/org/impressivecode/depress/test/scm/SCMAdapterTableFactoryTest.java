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
package org.impressivecode.depress.test.scm;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.common.TableCellReader.reader;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.ACTION_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.AUTHOR_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.DATE_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MESSAGE_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.PATH_COLNAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.RESOURCE_NAME;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMAdapterTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec colSpec = SCMAdapterTableFactory.createDataColumnSpec();
        assertEquals(8, colSpec.getNumColumns());
    }


    @Test
    public void shouldFillRowIdUsingCustomId() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(row.getKey().getString()).isEqualTo("rowId");
    }

    @Test
    public void shouldTransformResource() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).string(RESOURCE_NAME)).isEqualTo("resource");
    }

    @Test
    public void shouldTransformPath() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).string(PATH_COLNAME)).isEqualTo("path");
    }

    @Test
    public void shouldTransformAuthor() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).string(AUTHOR_COLNAME)).isEqualTo("author");
    }

    @Test
    public void shouldTransformCommitDate() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).date(DATE_COLNAME).getTimeInMillis()).isEqualTo(100);
    }

    @Test
    public void shouldTransformMessage() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).string(MESSAGE_COLNAME)).isEqualTo("message");
    }

    @Test
    public void shouldTransformOperation() {
        // given
        SCMDataType scm = mockSCMDataType();
        // when
        DataRow row = SCMAdapterTableFactory.createTableRow("rowId",scm);
        // then
        assertThat(reader(createDataColumnSpec(), row).string(ACTION_COLNAME)).isEqualTo("MODIFIED");
    }

    private SCMDataType mockSCMDataType() {
        SCMDataType scm = mock(SCMDataType.class, RETURNS_DEEP_STUBS);
        when(scm.getResourceName()).thenReturn("resource");
        when(scm.getPath()).thenReturn("path");
        when(scm.getAuthor()).thenReturn("author");
        when(scm.getCommitDate()).thenReturn(new Date(100));
        when(scm.getCommitID()).thenReturn("commitID");
        when(scm.getMessage()).thenReturn("message");
        when(scm.getOperation()).thenReturn(SCMOperation.MODIFIED);
        return scm;
    }

}
