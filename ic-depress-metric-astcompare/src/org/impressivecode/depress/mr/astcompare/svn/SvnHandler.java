package org.impressivecode.depress.mr.astcompare.svn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.history.IFileHistory;
import org.eclipse.team.core.history.IFileHistoryProvider;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.history.provider.FileHistoryProvider;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

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
public class SvnHandler {

    private final ExecutionContext exec;
    private RepositoryProvider repositoryProvider;
    long revisionDateMin;
    long revisionDateMax;

    public SvnHandler(ExecutionContext exec, RepositoryProvider repositoryProvider, long revisionDateMin,
            long revisionDateMax) {
        super();
        this.exec = exec;
        this.repositoryProvider = repositoryProvider;
        this.revisionDateMin = revisionDateMin;
        this.revisionDateMax = revisionDateMax;
    }

    public IFileRevision[] getProperFileRevisions(IResource file) throws CanceledExecutionException {

        List<IFileRevision> revisionList = new ArrayList<IFileRevision>(100);
        IFileRevision[] allRevisions = getFileRevisions(file);
        IFileRevision initRevision = null;

        if (allRevisions != null && allRevisions.length > 1) {
            for (IFileRevision revision : allRevisions) {
                if (exec != null) {
                    exec.checkCanceled();
                }
                // find initial revision
                if (revision.getTimestamp() < revisionDateMin) {
                    if (initRevision == null) {
                        initRevision = revision;
                    }
                    if (initRevision != null && revision.getTimestamp() > initRevision.getTimestamp()) {
                        initRevision = revision;
                    }
                }

                // add revision if in range
                if (isRevisionDateInRange(revision.getTimestamp(), revisionDateMin, revisionDateMax)) {
                    revisionList.add(revision);
                }
            }
            // add initial revision
            if (initRevision != null) {
                revisionList.add(initRevision);
            }
            Collections.sort(revisionList, new FileRevisionComparable());
        }
        return revisionList.toArray(new IFileRevision[revisionList.size()]);
    }

    private IFileRevision[] getFileRevisions(IResource file) {

        IFileRevision[] revisions = null;

        IFileHistoryProvider fileHistoryProvider = repositoryProvider.getFileHistoryProvider();

        if (fileHistoryProvider == null)
            return null;

        IFileHistory iFileHistory = fileHistoryProvider.getFileHistoryFor(file,
                FileHistoryProvider.SINGLE_LINE_OF_DESCENT, null);

        if (iFileHistory != null) {
            revisions = iFileHistory.getFileRevisions();
        }

        return revisions;
    }

    private boolean isRevisionDateInRange(long revisionDate, long rangeMin, long rangeMax) {
        return revisionDate >= rangeMin && revisionDate <= rangeMax;
    }

}
