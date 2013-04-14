package org.impressivecode.depress.scm.svn.test.functional;

import org.impressivecode.depress.scm.svn.SVNLogLoader;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.impressivecode.depress.scm.svn.SVNLogRow;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.CanceledExecutionException;

public class SVNLogLoaderTest {

	SVNLogLoader loader;
	IReadProgressListener logic;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogLoader();
	}

	@Test
	public void testLoadXML() {
		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, "", "", logic);
	}

}
