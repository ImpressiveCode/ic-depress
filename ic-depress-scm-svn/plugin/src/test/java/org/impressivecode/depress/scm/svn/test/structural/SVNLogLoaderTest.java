package org.impressivecode.depress.scm.svn.test.structural;

import org.impressivecode.depress.scm.svn.SVNLogFileLoader;
import org.impressivecode.depress.scm.svn.SVNLogFileLoader.IReadProgressListener;
import org.impressivecode.depress.scm.svn.SVNLogRow;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.CanceledExecutionException;

public class SVNLogLoaderTest {

	SVNLogFileLoader loader;
	IReadProgressListener logic;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogFileLoader();
		logic = new IReadProgressListener() {

			@Override
			public void onReadProgress(int inProgres, SVNLogRow inRow)
					throws CanceledExecutionException {

			}
		};
	}

	@Test
	public void testLoadXmlIssue() {
		loader.load(TestSettings.xmlTestSvnLogPath, null, "", logic);
	}

	@Test
	public void testLoadXmlPackage() {
		loader.load(TestSettings.xmlTestSvnLogPath, "", null, logic);
	}

	@Test
	public void testLoadXmlPath() {
		loader.load(null, "", "", logic);
	}

}
