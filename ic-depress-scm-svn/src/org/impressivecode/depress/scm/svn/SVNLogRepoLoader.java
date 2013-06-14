/*
ImpressiveCode Depress Framework
Copyright (C) 2013  ImpressiveCode contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.impressivecode.depress.scm.svn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.knime.core.node.CanceledExecutionException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNLogRepoLoader extends SVNLogLoader {

    private final int PathId = 0;
    private final int UserId = 1;
    private final int PassId = 2;

    @Override
    public void load(String inPath, String inIssueMarker, String inPackage, IReadProgressListener inProgress)
            throws CanceledExecutionException, Exception {

        String[] params = GetParams(inPath.split(";"));

        if (params != null) {
            loadRepo(params[PathId], params[UserId], params[PassId], inIssueMarker, inPackage.split(";"), inProgress);
            return;
        }

        throw new Exception(SVNLocale.eIvalidOnlinePath(inPath));
    }

    private String[] GetParams(String[] inPath) {

        if (inPath.length == 1) {
            return new String[] { inPath[0], "", "" };
        }

        if (inPath.length == 3) {
            return inPath;
        }
        return null;
    }

    protected void loadRepo(String inPath, String inUser, String inPass, String inIssueMarker, String[] inPackages,
            IReadProgressListener inProgress) throws CanceledExecutionException, Exception {

        inProgress.onReadProgress(0, null);

        DAVRepositoryFactory.setup();

        inProgress.onReadProgress(0.25, null);

        String url = inPath;
        String name = inUser;
        String password = inPass;

        try {

            @SuppressWarnings("deprecation")
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));

            inProgress.onReadProgress(0.5, null);

            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
            repository.setAuthenticationManager(authManager);

            inProgress.onReadProgress(0.75, null);

            String targetPaths[] = inPackages;
            List<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();

            long startRevision = 0;
            long endRevision = -1;
            boolean changedPath = true;
            boolean strictNode = false;

            repository.log(targetPaths, entries, startRevision, endRevision, changedPath, strictNode);

            inProgress.onReadProgress(100, null);

            inProgress.onReadProgress(0, null);

            tmpData.issueMarker = inIssueMarker;

            for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {

                inProgress.checkLoading();

                tmpData.uid = Long.toString(entries.get(entryIndex).getRevision());

                tmpData.author = entries.get(entryIndex).getAuthor();
                tmpData.date = entries.get(entryIndex).getDate().toString();
                tmpData.message = entries.get(entryIndex).getMessage();

                if (!isMarkerInMessage(tmpData.message, tmpData.issueMarker)) {
                    continue;
                }

                Map<String, SVNLogEntryPath> entryPaths = entries.get(entryIndex).getChangedPaths();

                for (SVNLogEntryPath mapElement : entryPaths.values()) {

                    inProgress.checkLoading();

                    tmpData.action = Character.toString(mapElement.getType());

                    tmpData.path = mapElement.getPath();

                    if (!isValidFile(tmpData.path)) {
                        continue;
                    }

                    inProgress.onReadProgress(percent(entryIndex, entries.size()), tmpData.createRow());

                }
            }

        } catch (CanceledExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ROOT);
    }

}
