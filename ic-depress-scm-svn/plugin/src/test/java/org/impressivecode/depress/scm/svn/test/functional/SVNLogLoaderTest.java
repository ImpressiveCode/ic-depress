package org.impressivecode.depress.scm.svn.test.functional;

import org.impressivecode.depress.scm.svn.SVNLogFileLoader;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.impressivecode.depress.scm.svn.SVNLogRepoLoader;
import org.impressivecode.depress.scm.svn.SVNLogRow;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.CanceledExecutionException;

public class SVNLogLoaderTest {

	SVNLogFileLoader loader;
	private IReadProgressListener listener;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogRepoLoader();
		listener = new IReadProgressListener() {

			@Override
			public void onReadProgress(double inProgres, SVNLogRow inRow) {

			}

			@Override
			public void checkLoading() throws CanceledExecutionException {
			}
		};
	}

	@Test
	public void testLoadXML() {
		loader.load(TestSettings.xmlTestSvnLogPath, "", "", listener);
	}

	@Test
	public void testLoadRepo() {
		loader.load(TestSettings.repoTestSvnLogPath, "", "", listener);
	}

}
