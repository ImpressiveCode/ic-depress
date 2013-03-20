package org.impressivecode.depress.scm.svn;

public class SVNLogLoader {

	public interface IReadProgressListener {

		void onReadProgress(int inProgres, SVNLogRow inRow)
				throws IndexOutOfBoundsException;

	}

	public void loadXmlL(String inPath, String inIsueMarker, String inPackage,
			IReadProgressListener inProgress) {

		for (int i = 0; i < 100; ++i) {
			inProgress.onReadProgress(i, new SVNLogRow());
		}

	}

}