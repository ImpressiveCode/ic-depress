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
package org.impressivecode.depress.mg.astmetrics;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.CanceledExecutionException;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstComparator {

    private FileDistiller distiller;

    public AstComparator() {
        distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
    }

    public List<SingleChangeInfo> compareAstOfFiles(File previous, File actual) throws CanceledExecutionException,
            SQLException {
        try {
            distiller.extractClassifiedSourceCodeChanges(previous, actual);
        } catch(Exception e) {
            e.printStackTrace();
        }

        List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
        List<SingleChangeInfo> changeInfoList = new ArrayList<SingleChangeInfo>();

        for (SourceCodeChange scc : changes) {
            SingleChangeInfo info = new SingleChangeInfo();
            String methodName = scc.getRootEntity().getUniqueName();
            if (!methodName.contains("(") || !methodName.contains(")")) {
                methodName = null;
            }
            info.setMethodName(methodName);
            info.setChangeEntity(scc.getChangedEntity().toString());
            info.setChangeType(scc.getChangeType().toString());

            // only method level
            if (info.getMethodName() != null && info.hasDataCorrect()) {
                changeInfoList.add(info);
            }
        }
        return changeInfoList;
    }
}
