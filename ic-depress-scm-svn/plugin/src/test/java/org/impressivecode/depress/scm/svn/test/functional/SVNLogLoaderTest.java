package org.impressivecode.depress.scm.svn.test.functional;

import org.impressivecode.depress.scm.svn.SVNLogLoader;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.impressivecode.depress.scm.svn.SVNLogRow;
import org.impressivecode.depress.scm.svn.SVNNodeLogic;
import org.impressivecode.depress.scm.svn.test.TestSettings;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.CanceledExecutionException;

public class SVNLogLoaderTest {

	SVNLogLoader loader;
	SVNNodeLogic logic;

	@Before
	public void setUp() throws Exception {
		loader = new SVNLogLoader();
		logic = new SVNNodeLogic();
	}

	@Test
	public void testLoadXML() {
		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, "", "", logic);
	}

//	@Test
//	public void testLoadXMLException() {
//		loader.loadXmlL(TestSettings.xmlTestSvnLogPath, "", "",
//				new IReadProgressListener() {
//					@Override
//					public void onReadProgress(int inProgres, SVNLogRow inRow)
//							throws CanceledExecutionException {
//						
//					}
//				});
//	}

}
