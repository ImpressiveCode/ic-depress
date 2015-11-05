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
package org.impressivecode.depress.support.syntacticanalysis;

import static org.impressivecode.depress.common.Cells.integerOrMissingCell;

import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * To jest testowy commit
 */
public class SyntacticAnalysisCellFactory implements AppendedCellFactory {

    private final Configuration cfg;
    private final int msgCellIndex;

    public SyntacticAnalysisCellFactory(final Configuration configuration, final int msgColIndex) {
        this.cfg = configuration;
        this.msgCellIndex = msgColIndex;
    }

    @Override
    public DataCell[] getAppendedCell(final DataRow row) {
        String message = ((StringCell) row.getCell(msgCellIndex)).getStringValue();

        Integer confidence = checkConfidence(message);

        return new DataCell[] {integerOrMissingCell(confidence) };
    }

    private int checkConfidence(final String message) {

        if (hasKeywords(message)) {
            return 2;
        } else if (onlyNumbers(message)) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean onlyNumbers(final String message) {
        return cfg.getOnlyNumbers().isPresent() ? cfg.getOnlyNumbers().get().matcher(message).matches() : false;
    }

    private boolean hasKeywords(final String message) {

        if (cfg.getKeywords().isPresent()) {
            for (String keyword : cfg.getKeywords().get()) {
                if (message.contains(keyword)) {
                    return true;
                }
            }
        }

        if (cfg.getKeywordsRegexp().isPresent()) {
            if (cfg.getKeywordsRegexp().get().matcher(message).matches()) {
                return true;
            }
        }

        return false;
    }
}