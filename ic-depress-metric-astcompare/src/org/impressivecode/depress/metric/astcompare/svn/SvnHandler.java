package org.impressivecode.depress.metric.astcompare.svn;

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
import org.knime.core.node.NodeLogger;

/**
 * @author Piotr Mitka
 */
public class SvnHandler {

    private static final NodeLogger logger = NodeLogger.getLogger(SvnHandler.class);
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

        try {
            IFileHistory iFileHistory = fileHistoryProvider.getFileHistoryFor(file,
                    FileHistoryProvider.SINGLE_LINE_OF_DESCENT, null);

            if (iFileHistory != null) {
                revisions = iFileHistory.getFileRevisions();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return revisions;
    }

    private boolean isRevisionDateInRange(long revisionDate, long rangeMin, long rangeMax) {
        return revisionDate >= rangeMin && revisionDate <= rangeMax;
    }

}
