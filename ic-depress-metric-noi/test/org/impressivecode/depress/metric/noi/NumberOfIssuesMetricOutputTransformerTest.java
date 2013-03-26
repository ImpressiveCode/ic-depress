package org.impressivecode.depress.metric.noi;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class NumberOfIssuesMetricOutputTransformerTest {

    @Test
    public void shouldTransform() throws CanceledExecutionException {
        //given
        OutputTransformer<NoIMetricType> transformer = new NumberOfIssuesMetricOutputTransformer(NumberOfIssuesMetricTableFactory.createDataColumnSpec());
        List<NoIMetricType> entries = Lists.newArrayList(noi("ClassA", Lists.newArrayList("i1", "i2")),
                noi("ClassA", Lists.newArrayList("i1", "i1")));
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        //when
        transformer.transform(entries, exec);

        //then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    private NoIMetricType noi(final String className, final ArrayList<String> items) {
        NoIMetricType noi = new NoIMetricType();
        noi.setClassName(className);
        noi.setIssues(items);
        return noi;
    }

}
