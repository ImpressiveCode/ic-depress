package org.impressivecode.depress.scm.svn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.CanceledExecutionException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNLogRepoLoader extends SVNLogFileLoader {

	private final int PathId = 0;
	private final int UserId = 1;
	private final int PassId = 2;

	@Override
	public void load(String inPath, String inIssueMarker, String inPackage,
			IReadProgressListener inProgress) {

		if (inPath.startsWith("http://")) {
			String[] params = GetParams(inPath.split(";"));

			if (params != null) {
				loadRepo(params[PathId], params[UserId], params[PassId],
						inIssueMarker, inPackage.split(";"), inProgress);
			}
		} else {
			loadXml(inPath, inIssueMarker, inPackage, inProgress);
		}

	}

	private String[] GetParams(String[] inPath) {

		if (inPath.length == 1) {
			return new String[] { inPath[0], "", "" };
		}

		if (inPath.length == 3) {
			return inPath;
		}

		Logger.instance().warn(SVNLocale.iIvalidRepoPath());

		return null;
	}

	protected void loadRepo(String inPath, String inUser, String inPass,
			String inIssueMarker, String[] inPackages,
			IReadProgressListener inProgress) {

		inProgress.onReadProgress(0, null);

		DAVRepositoryFactory.setup();

		inProgress.onReadProgress(0.25, null);

		String url = inPath;
		String name = inUser;
		String password = inPass;

		try {

			Logger.instance().warn(SVNLocale.iInitOnlineRepo(url));

			@SuppressWarnings("deprecation")
			SVNRepository repository = SVNRepositoryFactory.create(SVNURL
					.parseURIDecoded(url));

			inProgress.onReadProgress(0.5, null);

			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);

			inProgress.onReadProgress(0.75, null);

			String targetPaths[] = inPackages;
			List<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();

			long startRevision = 0;
			long endRevision = -1;
			boolean changedPath = true;
			boolean strictNode = false;

			repository.log(targetPaths, entries, startRevision, endRevision,
					changedPath, strictNode);

			inProgress.onReadProgress(100, null);

			Logger.instance().warn(SVNLocale.iStartLoadOnlineRepo());

			inProgress.onReadProgress(0, null);

			tmData.issueMarker = inIssueMarker;

			for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {

				inProgress.checkLoading();

				tmData.uid = Long.toString(entries.get(entryIndex)
						.getRevision());

				tmData.author = entries.get(entryIndex).getAuthor();
				tmData.date = entries.get(entryIndex).getDate().toString();
				tmData.message = entries.get(entryIndex).getMessage();

				if (!isMarkerInMessage(tmData.message, tmData.issueMarker)) {
					continue;
				}

				Map<String, SVNLogEntryPath> entryPaths = entries.get(
						entryIndex).getChangedPaths();

				for (SVNLogEntryPath mapElement : entryPaths.values()) {

					inProgress.checkLoading();

					tmData.action = Character.toString(mapElement.getType());

					tmData.path = mapElement.getPath();

					if (!isValidFile(tmData.path)) {
						continue;
					}

					inProgress.onReadProgress(
							percent(entryIndex, entries.size()),
							tmData.createRow());

				}
			}

		} catch (SVNException e) {
			Logger.instance().error(SVNLocale.iSVNInternalError(), e);
			inProgress.onReadProgress(0, null);
		} catch (CanceledExecutionException e) {
			Logger.instance().warn(SVNLocale.iCancelLoading());
			inProgress.onReadProgress(0, null);
		} catch (Exception e) {
			Logger.instance().error(SVNLocale.iSVNInternalError(), e);
			inProgress.onReadProgress(0, null);
		}

		finally {
			Logger.instance().warn(SVNLocale.iEndLoadOnlineRepo());
		}
	}

}
