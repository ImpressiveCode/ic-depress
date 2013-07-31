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
package org.impressivecode.depress.scm;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
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

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMAdapterTransformerTest {

    @Test
    public void shouldTransform() throws CanceledExecutionException {

        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        List<SCMDataType> entries = Lists.newArrayList(create("path1"), create("path2"));

        BufferedDataContainer container = mock(BufferedDataContainer.class);
        ExecutionContext exec = mock(ExecutionContext.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);

        // when
        transformer.transform(entries, exec);

        // then
        verify(container, times(2)).addRowToTable(Mockito.any(DataRow.class));
    }

    private SCMDataType create(final String path) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor("a");
        scm.setCommitDate(new Date());
        scm.setCommitID("ID");
        scm.setMessage("m");
        scm.setOperation(SCMOperation.ADDED);
        scm.setPath(path);
        scm.setResourceName("r");
        return scm;
    }

}
