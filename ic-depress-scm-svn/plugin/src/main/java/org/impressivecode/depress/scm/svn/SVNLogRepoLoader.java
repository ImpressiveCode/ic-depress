package org.impressivecode.depress.scm.svn;

import org.knime.core.node.CanceledExecutionException;

public class SVNLogRepoLoader extends SVNLogFileLoader {

	private final int PathId = 0;
	private final int UserId = 1;
	private final int PassId = 2;

	@Override
	public void load(String inPath, String inIssueMarker, String inPackage,
			IReadProgressListener inProgress) {

		if (inPath.startsWith("http://")) {
			String[] params = inPath.split(";");
			loadRepo(params[PathId], params[UserId], params[PassId],
					inIssueMarker, inPackage, inProgress);
		} else {
			loadXml(inPath, inIssueMarker, inPackage, inProgress);
		}

	}

	protected void loadRepo(String inPath, String inUser, String inPass,
			String inIssueMarker, String inPackage,
			IReadProgressListener inProgress) {

		try {

			inProgress.onReadProgress(100, null);

		} catch (CanceledExecutionException e) {
			Logger.instance().error(" loadRepo ", e);
		}

	}
}
