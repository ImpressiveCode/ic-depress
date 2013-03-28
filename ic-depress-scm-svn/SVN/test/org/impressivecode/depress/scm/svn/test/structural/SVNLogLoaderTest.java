package org.impressivecode.depress.scm.svn.test.structural;

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
	public void testLoadXmlPath() {
		loader.loadXmlL(null, "", "", logic);
	}

	@Test
	public void testLoadXmlIssue() {
		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, null, "", logic);
	}

	@Test
	public void testLoadXmlPackage() {
		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, "", null, logic);
	}

}
