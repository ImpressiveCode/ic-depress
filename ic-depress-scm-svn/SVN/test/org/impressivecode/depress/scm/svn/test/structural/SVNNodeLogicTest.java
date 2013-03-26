package org.impressivecode.depress.scm.svn.test.structural;

import org.impressivecode.depress.scm.svn.SVNNodeLogic;
import org.junit.Before;
import org.junit.Test;

public class SVNNodeLogicTest {

	SVNNodeLogic logic;

	@Before
	public void setUp() throws Exception {
		logic = new SVNNodeLogic();
	}

	@Test
	public void testNotifyIntMin() {
		logic.onReadProgress(-1, null);
	}

	@Test
	public void testNotifyIntMax() {
		logic.onReadProgress(101, null);
	}

	@Test
	public void testNotifyRange() {
		logic.onReadProgress(0, null);
		logic.onReadProgress(100, null);
	}

}
