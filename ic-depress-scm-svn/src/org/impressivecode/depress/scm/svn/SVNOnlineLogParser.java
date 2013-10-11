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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.impressivecode.depress.scm.SCMOperation;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNOnlineLogParser {

    private final Pattern PATTERN = Pattern.compile("^(.*)");

    public List<SVNCommit> parseEntries(final String path,
            final SVNParserOptions svnParserOptions) throws IOException,
            ParseException, SVNException {
        checkArgument(!isNullOrEmpty(path), "Path has to be set.");

        List<SVNCommit> commitsList = processRepo(path, svnParserOptions);

        return commitsList;
    }

    private List<SVNCommit> processRepo(final String path,
            final SVNParserOptions svnParserOptions) throws IOException,
            SVNException {

        SVNRepository svn = initializeSvn(path);

        List<SVNCommit> analyzedCommits = new ArrayList<SVNCommit>();

        List<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();

        svn.log(new String[] { svnParserOptions.getPackagePrefix() }, entries,
                1, -1, true, false);

        for (SVNLogEntry svnLogEntry : entries) {

            SVNCommit commit = new SVNCommit();

            setHeader(commit, svnLogEntry);
            setMessage(commit, svnLogEntry, svnParserOptions);

            Map<String, SVNLogEntryPath> entryPaths = svnLogEntry
                    .getChangedPaths();

            for (SVNLogEntryPath logFile : entryPaths.values()) {
                setCommitFile(commit, logFile, svnParserOptions);
            }

            analyzedCommits.add(commit);
        }

        return analyzedCommits;
    }

    private void setCommitFile(final SVNCommit commit, final SVNLogEntryPath logFile,
            final SVNParserOptions svnParserOptions) {

        Matcher matcher = PATTERN.matcher(logFile.getPath());

        if (matcher.matches()) {
            String transformed = logFile.getPath().replaceAll("/", ".");
            String origin = matcher.group(1);

            if (include(svnParserOptions, transformed)) {

                SVNCommitFile commitFile = new SVNCommitFile();

                commitFile.setOperation(parseOperation(logFile));
                commitFile.setPath(origin);
                commitFile.setJavaClass(parseJavaClass(svnParserOptions,
                        transformed));

                commit.getFiles().add(commitFile);
            }
        }
    }

    private void setMessage(final SVNCommit commit, final SVNLogEntry svnLogEntry,
            final SVNParserOptions svnParserOptions) {
        String message = getMessage(svnLogEntry);

        commit.addToMessage(message);
    }

    private void setHeader(final SVNCommit commit, final SVNLogEntry svnLogEntry) {
        commit.setId(Long.toString(svnLogEntry.getRevision()));
        commit.setAuthor(svnLogEntry.getAuthor());
        commit.setDate(svnLogEntry.getDate());
    }

    private String getMessage(final SVNLogEntry svnLogEntry) {
        String message = svnLogEntry.getMessage();

        if (message == null) {
            message = "";
        } else if (message.endsWith("\n")) {
            message = message.substring(0, message.length() - 1);
        }
        return message;
    }

    private boolean include(final SVNParserOptions svnParserOptions, final String path) {
        boolean java = path.endsWith(".java");
        if (java) {
            if (svnParserOptions.hasPackagePrefix()) {
                return path.indexOf(svnParserOptions.getPackagePrefix()) != -1;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private String parseJavaClass(final SVNParserOptions svnParserOptions,
            final String path) {
        String javaClass = path.replace(".java", "");
        if (svnParserOptions.hasPackagePrefix()) {
            javaClass = javaClass.substring(javaClass.indexOf(svnParserOptions
                    .getPackagePrefix()));
        }
        return javaClass;
    }

    private SCMOperation parseOperation(final SVNLogEntryPath logFile) {

        switch (logFile.getType()) {
        case SVNLogEntryPath.TYPE_DELETED:
            return SCMOperation.DELETED;
        case SVNLogEntryPath.TYPE_MODIFIED:
            return SCMOperation.MODIFIED;
        case SVNLogEntryPath.TYPE_ADDED:
            return SCMOperation.ADDED;
        default:
            return SCMOperation.OTHER;
        }
    }

    private SVNRepository initializeSvn(final String path) throws SVNException {

        DAVRepositoryFactory.setup();
        FSRepositoryFactory.setup();

        SVNRepository repo = SVNRepositoryFactory.create(SVNURL
                .parseURIEncoded(path));

        ISVNAuthenticationManager authManager = SVNWCUtil
                .createDefaultAuthenticationManager("", "");
        repo.setAuthenticationManager(authManager);

        return repo;
    }
}
