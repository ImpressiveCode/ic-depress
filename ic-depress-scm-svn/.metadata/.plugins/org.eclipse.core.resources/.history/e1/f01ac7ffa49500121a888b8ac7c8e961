package org.impressivecode.depress.scm.svn.test.functional;

import org.impressivecode.depress.scm.svn.SVNLogLoader;
import org.impressivecode.depress.scm.svn.SVNNodeLogic;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;

public class SVNLogLoaderTest {

	SVNLogLoader loader;
	SVNNodeLogic logic;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogLoader();
		logic = new SVNNodeLogic();
	}

	@Test
	public void testloadXML() {
		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, "", "", logic);
	}
}
