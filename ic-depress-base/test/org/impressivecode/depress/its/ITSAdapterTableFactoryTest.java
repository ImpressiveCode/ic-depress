package org.impressivecode.depress.its;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.common.DataTableSpecUtils.extractListStringCellValue;
import static org.impressivecode.depress.common.DataTableSpecUtils.extractStringCellValue;
import static org.impressivecode.depress.common.DataTableSpecUtils.isMissing;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.RESOLVED_DATE;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.SUMMARY;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.UPDATED_DATE;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.date.DateAndTimeCell;

import com.google.common.collect.Lists;

public class ITSAdapterTableFactoryTest {

    @Test
    public void shouldCreateITSTableSpec() {
        DataTableSpec spec = createDataColumnSpec();
        assertThat(spec.getNumColumns()).isEqualTo(13);
    }

    @Test
    public void shouldFillRowIdUsingBugId() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(row.getKey().getString()).isEqualTo("BUG-11");
    }

    @Test
    public void shouldTransformSummary() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getSummary()).thenReturn("Summary summary");
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, SUMMARY)).isEqualTo("Summary summary");
    }

    @Test
    public void shouldTransformSummaryWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getSummary()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, SUMMARY)).isTrue();
    }

    @Test
    public void shouldTransformPriority() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.PRIORITY)).isEqualTo("BLOCKER");
    }

    @Test
    public void shouldTransformStatus() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.STATUS)).isEqualTo("CLOSED");
    }

    @Test
    public void shouldTransformCreated() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractDateCellCalendarValue(row, ITSAdapterTableFactory.CREATION_DATE).getTimeInMillis())
        .isEqualTo(100);
    }

    @Test
    public void shouldTransformType() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.TYPE)).isEqualTo("BUG");
    }

    @Test
    public void shouldTransformVersion() {
        // given
        ITSDataType its = mockITSDataType();
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractListStringCellValue(row, ITSAdapterTableFactory.VERSION)).containsExactly("V1", "V2");
    }

    @Test
    public void shouldTransformFixVersion() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getFixVersion()).thenReturn(newArrayList("V3", "V4"));
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractListStringCellValue(row, ITSAdapterTableFactory.FIX_VERSION)).containsExactly("V3", "V4");
    }

    @Test
    public void shouldTransformFixVersionWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getFixVersion()).thenReturn(Collections.<String> emptyList());
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.FIX_VERSION)).isTrue();
    }

    @Test
    public void shouldTransformComments() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getComments()).thenReturn(newArrayList("c1", "c2"));
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractListStringCellValue(row, ITSAdapterTableFactory.COMMENTS)).containsExactly("c1", "c2");
    }

    @Test
    public void shouldTransformCommentsWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getComments()).thenReturn(Collections.<String> emptyList());
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.COMMENTS)).isTrue();
    }

    @Test
    public void shouldTransformDescription() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getDescription()).thenReturn("desc");
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.DESCRIPTION)).isEqualTo("desc");
    }

    @Test
    public void shouldTransformDescriptionWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getDescription()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.DESCRIPTION)).isTrue();
    }

    @Test
    public void shouldTransformLink() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getLink()).thenReturn("link");
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.LINK)).isEqualTo("link");
    }

    @Test
    public void shouldTransformLinkWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getLink()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.LINK)).isTrue();
    }

    @Test
    public void shouldTransformResolution() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getResolution()).thenReturn(ITSResolution.FIXED);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractStringCellValue(row, ITSAdapterTableFactory.RESOLUTION)).isEqualTo("FIXED");
    }

    @Test
    public void shouldTransformResolutionWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getResolution()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.RESOLUTION)).isTrue();
    }

    @Test
    public void shouldTransformUpdated() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getUpdated()).thenReturn(new Date(200));
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractDateCellCalendarValue(row, UPDATED_DATE).getTimeInMillis()).isEqualTo(200);
    }

    @Test
    public void shouldTransformUpdatedWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getUpdated()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, ITSAdapterTableFactory.UPDATED_DATE)).isTrue();
    }
    @Test
    public void shouldTransformResolved() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getResolved()).thenReturn(new Date(300));
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(extractDateCellCalendarValue(row, RESOLVED_DATE).getTimeInMillis()).isEqualTo(300);
    }

    @Test
    public void shouldTransformResolvedWhenEmpty() {
        // given
        ITSDataType its = mockITSDataType();
        when(its.getResolved()).thenReturn(null);
        // when
        DataRow row = ITSAdapterTableFactory.createTableRow(its);

        // then
        assertThat(isMissing(row, RESOLVED_DATE)).isTrue();
    }

    private Calendar extractDateCellCalendarValue(final DataRow row, final String colName) {
        return ((DateAndTimeCell) row.getCell(createDataColumnSpec().findColumnIndex(colName))).getUTCCalendarClone();
    }

    private ITSDataType mockITSDataType() {
        ITSDataType its = mock(ITSDataType.class, RETURNS_DEEP_STUBS);
        when(its.getIssueId()).thenReturn("BUG-11");
        when(its.getCreated()).thenReturn(new Date(100));
        when(its.getPriority()).thenReturn(ITSPriority.BLOCKER);
        when(its.getStatus()).thenReturn(ITSStatus.CLOSED);
        when(its.getType()).thenReturn(ITSType.BUG);
        when(its.getVersion()).thenReturn(Lists.newArrayList("V1", "V2"));
        return its;
    }

}
