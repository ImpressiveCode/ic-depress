package org.impressivecode.depress.scm.git;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.data.DataTableSpec;
import org.impressivecode.depress.scm.git.GitTableFactory;

public class GitTableFactoryTest {

    @Test
    public void shouldCreateDataColumnSpec() {
        DataTableSpec colSpec = GitTableFactory.createDataColumnSpec();
        assertNotNull(colSpec);
        assertEquals(8, colSpec.getNumColumns());
    }

}
