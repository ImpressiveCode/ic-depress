package org.impressivecode.depress.mr.astcompare.ast;

import org.eclipse.team.core.history.IFileRevision;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

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

        return info;

    }
}
