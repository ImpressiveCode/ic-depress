package org.impressivecode.depress.scm.git;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;


import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.DataTableSpec;
import org.impressivecode.depress.scm.git.GitTableFactory;

public class GitTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec colSpec = GitTableFactory.createDataColumnSpec();
        assertNotNull(colSpec);
        assertEquals(8, colSpec.getNumColumns());
    }
    
    
    @Test
    public void shouldCreateTableRow() {
        DataRow row = GitTableFactory.createTableRow("test.java", "marker", "Author", 
                "Added", "Test commit", "org/scm/git/test.java", "2013-03-28 23:05:00", "dfsdf2334");
        assertEquals(row.getKey().getString(),"dfsdf2334");
        assertEquals((StringCell)row.getCell(0), new StringCell("test.java"));
        assertEquals((StringCell)row.getCell(1), new StringCell("marker"));
        assertEquals((StringCell)row.getCell(2), new StringCell("Author"));
        assertEquals((StringCell)row.getCell(3), new StringCell("Added"));
        assertEquals((StringCell)row.getCell(4), new StringCell("Test commit"));
        assertEquals((StringCell)row.getCell(5), new StringCell("org/scm/git/test.java"));
        assertEquals((StringCell)row.getCell(6), new StringCell("2013-03-28 23:05:00"));
        assertEquals((StringCell)row.getCell(7), new StringCell("dfsdf2334"));
    }

}
