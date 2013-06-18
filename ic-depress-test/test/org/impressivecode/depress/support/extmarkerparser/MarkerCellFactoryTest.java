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
package org.impressivecode.depress.support.extmarkerparser;

import static com.google.common.collect.Sets.newHashSet;
import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.CFG_IDBUILDER;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.CFG_KEYWORDS;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.CFG_REGEXP_ID;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.CFG_REGEXP_KEYWORDS;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.CFG_REGEXP_ONLYIDS;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.IDBUILDER_DEFAULT;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.KEYWORDS_DEFAULT;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.REGEXP_ID_DEFAULT;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.REGEXP_KEYWORDS_DEFAULT;
import static org.impressivecode.depress.support.extmarkerparser.ExtendedMarkerParserNodeModel.REGEXP_ONLYIDS_DEFAULT;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.impressivecode.depress.common.Cells;
import org.impressivecode.depress.support.extmarkerparser.Configuration;
import org.impressivecode.depress.support.extmarkerparser.ExtMarkerCellFactory;
import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.mockito.Mockito;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerCellFactoryTest {

    @Test
    public void shouldComputeConfidence2() {
        // given
        Configuration configuration = mockConfiguraton();
        ExtMarkerCellFactory mcf = new ExtMarkerCellFactory(configuration, 0);

        // when
        DataCell[] cells = mcf.getAppendedCell(mockRow("fixed bug 1234 1235"));

        // then
        assertThat(cells).hasSize(2);
        assertThat(extractIds(cells[0])).containsOnly("1234", "1235");
        assertThat(((IntCell) cells[1]).getIntValue()).isEqualTo(2);
    }

    @Test
    public void shouldComputeConfidence2WhenGivenKeyword() {
        // given
        Configuration configuration = mockConfiguraton();
        ExtMarkerCellFactory mcf = new ExtMarkerCellFactory(configuration, 0);

        // when
        DataCell[] cells = mcf.getAppendedCell(mockRow("exception 1234 1235"));

        // then
        assertThat(cells).hasSize(2);
        assertThat(extractIds(cells[0])).containsOnly("1234", "1235");
        assertThat(((IntCell) cells[1]).getIntValue()).isEqualTo(2);
    }

    @Test
    public void shouldComputeConfidence0WhenGivenKeyword() {
        // given
        Configuration configuration = mockConfiguraton();
        ExtMarkerCellFactory mcf = new ExtMarkerCellFactory(configuration, 0);

        // when
        DataCell[] cells = mcf.getAppendedCell(mockRow("tralalla 1234 1235"));

        // then
        assertThat(cells).hasSize(2);
        assertThat(extractIds(cells[0])).containsOnly("1234", "1235");
        assertThat(((IntCell) cells[1]).getIntValue()).isEqualTo(0);
    }

    @Test
    public void shouldComputeConfidenceMissing() {
        // given
        Configuration configuration = mockConfiguraton();
        ExtMarkerCellFactory mcf = new ExtMarkerCellFactory(configuration, 0);

        // when
        DataCell[] cells = mcf.getAppendedCell(mockRow("tralalla"));

        // then
        assertThat(cells).hasSize(2);
        assertThat(extractIds(cells[0])).isEmpty();
        assertThat(cells[1].isMissing()).isTrue();
    }

    @Test
    public void shouldComputeConfidence1WhenOnlyNumbers() {
        // given
        Configuration configuration = mockConfiguraton();
        ExtMarkerCellFactory mcf = new ExtMarkerCellFactory(configuration, 0);

        // when
        DataCell[] cells = mcf.getAppendedCell(mockRow("1234,1235"));

        // then
        assertThat(cells).hasSize(2);
        assertThat(extractIds(cells[0])).containsOnly("1234", "1235");
        assertThat(((IntCell) cells[1]).getIntValue()).isEqualTo(1);
    }

    private Set<String> extractIds(final DataCell dataCell) {
        SetCell set = ((SetCell) dataCell);
        return newHashSet(Iterables.transform(set, new Function<DataCell, String>() {
            @Override
            public String apply(final DataCell cell) {
                return ((StringCell) cell).getStringValue();
            }
        }));
    }

    private DataRow mockRow(final String message) {
        DataRow mock = Mockito.mock(DataRow.class);
        when(mock.getCell(0)).thenReturn(Cells.stringCell(message));
        return mock;
    }

    private Configuration mockConfiguraton() {
        return new Configuration(regExpID, regExpKeywords, keywords, regExpOnlyIds, builder);
    }

    private final SettingsModelString regExpID = new SettingsModelString(CFG_REGEXP_ID, REGEXP_ID_DEFAULT);
    private final SettingsModelString regExpKeywords = new SettingsModelString(CFG_REGEXP_KEYWORDS,
            REGEXP_KEYWORDS_DEFAULT);
    private final SettingsModelString keywords = new SettingsModelString(CFG_KEYWORDS, KEYWORDS_DEFAULT);
    private final SettingsModelString builder = new SettingsModelString(CFG_IDBUILDER, IDBUILDER_DEFAULT);
    private final SettingsModelString regExpOnlyIds = new SettingsModelString(CFG_REGEXP_ONLYIDS,
            REGEXP_ONLYIDS_DEFAULT);

}
