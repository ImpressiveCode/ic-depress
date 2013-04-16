package org.impressivecode.depress.scm.git;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class GitTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec colSpec = GitTableFactory.createDataColumnSpec();
        assertNotNull(colSpec);
        assertEquals(9, colSpec.getNumColumns());
    }


    @Test
    public void shouldCreateTableRow() {
        HashSet<String> markers = new HashSet<String>();
        markers.add("1");

        Set<StringCell> markersCell = new HashSet<StringCell>();
        markersCell.add(new StringCell("1"));

        DataRow row = GitTableFactory.createTableRow("org.scm.git.test", markers, "Author",
                "Added", "#1 Test commit", "org/scm/git/test.java", "2013-03-28 23:05:00", "dfsdf2334", "342424232", -435);
        assertEquals(row.getKey().getString(),"dfsdf2334");
        assertEquals(row.getCell(0), new StringCell("org.scm.git.test"));
        assertEquals(row.getCell(1), CollectionCellFactory.createSetCell(markersCell));
        assertEquals(row.getCell(2), new StringCell("Author"));
        assertEquals(row.getCell(3), new StringCell("Added"));
        assertEquals(row.getCell(4), new StringCell("#1 Test commit"));
        assertEquals(row.getCell(5), new StringCell("org/scm/git/test.java"));
        assertEquals(row.getCell(6), new StringCell("2013-03-28 23:05:00"));
        assertEquals(row.getCell(7), new StringCell("342424232"));
        assertEquals(row.getCell(8), new IntCell(-435));
        assertEquals(row.getKey().getString(), "dfsdf2334");
    }

}
