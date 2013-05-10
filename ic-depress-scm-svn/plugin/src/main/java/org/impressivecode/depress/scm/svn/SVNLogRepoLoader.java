package org.impressivecode.depress.scm.svn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.CanceledExecutionException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
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

		DAVRepositoryFactory.setup();

		String url = inPath;
		String name = inUser;
		String password = inPass;

		try {

			Logger.instance().warn(SVNLocale.iInitOnlineRepo(url));

			@SuppressWarnings("deprecation")
			SVNRepository repository = SVNRepositoryFactory.create(SVNURL
					.parseURIDecoded(url));
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);

			String targetPaths[] = inPackages;
			List<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();
			long startRevision = 0;
			long endRevision = -1;
			boolean changedPath = true;
			boolean strictNode = false;
			repository.log(targetPaths, entries, startRevision, endRevision,
					changedPath, strictNode);

			Logger.instance().warn(SVNLocale.iStartLoadOnlineRepo());

			for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
				String marker = new String(inIssueMarker);
				String uid = new String(entries.get(entryIndex).getRevision()
						+ ""); // revision
				String author = new String(entries.get(entryIndex).getAuthor()
						+ "");
				String date = new String(entries.get(entryIndex).getDate()
						.toString()
						+ "");
				String message = new String(entries.get(entryIndex)
						.getMessage() + "");

				// if message has not appropriate markers then will not include
				// that row
				if (isMarkerInMessage(message, marker) == false) {
					continue;
				}
				// //////////////////////////////////////////////////////////////////////

				Map<String, SVNLogEntryPath> entryPaths = entries.get(
						entryIndex).getChangedPaths();

				for (Map.Entry<String, SVNLogEntryPath> mapElement : entryPaths
						.entrySet()) {
					String action = new String(Character.toString(mapElement
							.getValue().getType()) + "");
					String path = new String(mapElement.getValue().getPath()
							+ "");

					SVNNodeKind nodeKind = repository.checkPath(path, -1);

					Logger.instance().warn("File : " + path);

					if (nodeKind != SVNNodeKind.FILE) // directory is not
														// important so skip it
					{
						continue;
					}

					// Uzupe³nienie SVNLogRow
					SVNLogRow r = new SVNLogRow();
					r.setMarker(marker);
					r.setAction(action);
					r.setPath(path);
					r.setClassName(getClassNameFromPath(path));
					r.setUid(uid);
					r.setAuthor(author);
					r.setMessage(message);
					r.setDate(date);

					inProgress.onReadProgress(
							percent(entryIndex, entries.size()), r);

				}
			}

		} catch (SVNException | CanceledExecutionException e) {
			Logger.instance().error("loadRepo ", e);
			try {
				inProgress.onReadProgress(100, null);
			} catch (CanceledExecutionException e1) {
				Logger.instance().error(" loadRepo onReadProgress complete ",
						e1);
			}
		}

		finally {
			Logger.instance().warn(SVNLocale.iEndLoadOnlineRepo());
		}
	}

	public final boolean isMarkerInMessage(String message, String marker) {
		return message.contains(marker);
	}

	public final String getClassNameFromPath(String path) {

		int lastDot = path.lastIndexOf(".");

		if (lastDot == -1) {
			lastDot = path.length() - 1;
		}

		int lastSlash = path.lastIndexOf("/") + 1;

		String smallPath = path.substring(lastSlash, lastDot);

		return (smallPath.substring(0, 1).toUpperCase() + smallPath.substring(
				1, smallPath.length()));
	}
}
