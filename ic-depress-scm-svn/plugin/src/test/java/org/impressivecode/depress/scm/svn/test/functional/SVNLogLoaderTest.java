package org.impressivecode.depress.scm.svn.test.functional;

import org.impressivecode.depress.scm.svn.SVNLogLoader;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;

public class SVNLogLoaderTest {

	SVNLogLoader loader;
	IReadProgressListener logic;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogLoader();
	}

	@Test
	public void testLoadXML() {
		loader.loadXml(TestSettings.xmlTestSvnLogPath, "", "", logic);
	}

}
