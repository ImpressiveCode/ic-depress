package org.impressivecode.depress.metric.astcompare.ast;

import org.impressivecode.depress.metric.astcompare.bug_prone.BugDetectionHandler;
import org.eclipse.team.core.history.IFileRevision;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Piotr Mitka
 */
public class SourceCodeChangeExtractor {
    public static SingleChangeInfo extractChanges(IFileRevision revision, SourceCodeChange scc) {
        SingleChangeInfo info = new SingleChangeInfo();
        info.setAuthor(revision.getAuthor());
        info.setComment(revision.getComment());
        info.setRevisionId(revision.getContentIdentifier());
        info.setFileName(revision.getName());
        info.setTimestamp(revision.getTimestamp());
        info.setChangeEntity(scc.getChangedEntity().toString());
        info.setChangeType(scc.getChangeType().toString());
        String methodName = scc.getRootEntity().getUniqueName();
        if (!methodName.contains("(") || !methodName.contains(")")) {
            methodName = null;
        }
        info.setMethodName(methodName);
        info.setBugProne(BugDetectionHandler.isMethodBugProne(revision.getComment()));

        return info;

    }
}
