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
package org.impressivecode.depress.mg.ipa;

import static org.fest.assertions.Assertions.assertThat;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.mg.ipa.IssuesMetricTableFactory;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.IntCell;

import com.google.common.collect.Lists;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class IssuesMetricTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec spec = IssuesMetricTableFactory.createDataColumnSpec();
        assertThat(spec.getNumColumns()).isEqualTo(3);
    }

    @Test
    public void shouldCreateTableRow() {
        //given
        IssuesMetricType noi = new IssuesMetricType();
        noi.setResourceName("ClassA");
        noi.setIssues(Lists.newArrayList(its("i1"), its("i2"), its("i2")));
        //when
        DataRow row = IssuesMetricTableFactory.createTableRow(noi);
        //then
        assertThat(row.getKey().getString()).isEqualTo("ClassA");
        assertThat(((ListCell)row.getCell(0)).get(0).toString()).isEqualTo("i1");
        assertThat(((ListCell)row.getCell(0)).get(1).toString()).isEqualTo("i2");
        assertThat(((ListCell)row.getCell(0)).get(2).toString()).isEqualTo("i2");
        assertThat(((IntCell)row.getCell(1)).getIntValue()).isEqualTo(3);
        assertThat(((IntCell)row.getCell(2)).getIntValue()).isEqualTo(2);
    }

    private ITSDataType its(final String id) {
        ITSDataType its = new ITSDataType();
        its.setIssueId(id);
        return its;
    }
}
