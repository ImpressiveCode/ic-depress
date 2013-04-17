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

package org.impressivecode.depress.scm.git;

/*
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 *
 */

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.def.StringCell;

public class GitTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec colSpec = GitTableFactory.createDataColumnSpec();
        assertNotNull(colSpec);
        assertEquals(8, colSpec.getNumColumns());
    }


    @Test
    public void shouldCreateTableRow() {
        HashSet<String> markers = new HashSet<String>();
        markers.add("1");

        Set<StringCell> markersCell = new HashSet<StringCell>();
        markersCell.add(new StringCell("1"));

        DataRow row = GitTableFactory.createTableRow("org.scm.git.test", markers, "Author",
                "Added", "#1 Test commit", "org/scm/git/test.java", "2013-03-28 23:05:00", "dfsdf2334", "342424232");
        assertEquals(row.getKey().getString(),"dfsdf2334");
        assertEquals(row.getCell(0), new StringCell("org.scm.git.test"));
        assertEquals(row.getCell(1), CollectionCellFactory.createSetCell(markersCell));
        assertEquals(row.getCell(2), new StringCell("Author"));
        assertEquals(row.getCell(3), new StringCell("Added"));
        assertEquals(row.getCell(4), new StringCell("#1 Test commit"));
        assertEquals(row.getCell(5), new StringCell("org/scm/git/test.java"));
        assertEquals(row.getCell(6), new StringCell("2013-03-28 23:05:00"));
        assertEquals(row.getCell(7), new StringCell("342424232"));
        assertEquals(row.getKey().getString(), "dfsdf2334");
    }

}
