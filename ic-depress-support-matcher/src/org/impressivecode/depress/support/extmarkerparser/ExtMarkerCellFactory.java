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
package org.impressivecode.depress.support.extmarkerparser;

import static org.impressivecode.depress.common.Cells.integerOrMissingCell;

import java.util.Set;
import java.util.regex.Matcher;

import org.impressivecode.depress.common.Cells;
import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ExtMarkerCellFactory implements AppendedCellFactory {

    private final Configuration cfg;
    private final int msgCellIndex;

    public ExtMarkerCellFactory(final Configuration configuration, final int findColumnIndex) {
        this.cfg = configuration;
        this.msgCellIndex = findColumnIndex;
    }

    @Override
    public DataCell[] getAppendedCell(final DataRow row) {
        String message = ((StringCell) row.getCell(msgCellIndex)).getStringValue();

        final Set<String> markers = Sets.newHashSet();
        if (this.cfg.getIdRegexp() != null) {
            Matcher matcher = this.cfg.getIdRegexp().matcher(message);
            while (matcher.find()) {
                if (matcher.groupCount() >= 1) {
                    markers.add(matcher.group(1));
                } else {
                    markers.add(matcher.group());
                }
            }
        }

        Integer confidence = null;
        if (!markers.isEmpty()) {
            confidence = checkConfidence(message, markers);
        }

        return new DataCell[] { Cells.stringSetCell(applyBuilder(markers)), integerOrMissingCell(confidence) };
    }

    private int checkConfidence(final String message, final Set<String> markers) {

        if (hasKeywords(message)) {
            return 2;
        } else if (onlyNumbers(message)) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean onlyNumbers(final String message) {
        return cfg.getOnlyNumbers().matcher(message).matches();
    }

    private boolean hasKeywords(final String message) {

        if (cfg.getKeywords() != null) {
            for (String keyword : cfg.getKeywords()) {
                if (message.contains(keyword)) {
                    return true;
                }
            }
        }

        if (cfg.getKeywordsRegexp() != null) {
            if (cfg.getKeywordsRegexp().matcher(message).matches()) {
                return true;
            }
        }

        return false;

    }

    private Set<String> applyBuilder(final Set<String> markers) {
        Set<String> transformed = Sets.newHashSet(Iterables.transform(markers, new Function<String, String>() {
            @Override
            public String apply(final String id) {
                String transformedId = null;
                if (cfg.getBuilderFormat() != null) {
                    transformedId = String.format(cfg.getBuilderFormat(), id);
                } else {
                    transformedId = id;
                }

                return transformedId;
            }
        }));
        return transformed;
    }
}