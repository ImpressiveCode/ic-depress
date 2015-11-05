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
package org.impressivecode.depress.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;


/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public final class TableCellReader {

    private final DataTableSpec spec;
    private final DataRow row;

    public TableCellReader(final DataTableSpec spec, final DataRow row) {
        this.spec = checkNotNull(spec, "Spec have to be set");
        this.row = checkNotNull(row, "Row have to be set");
    }

    public static TableCellReader reader(final DataTableSpec spec, final DataRow row) {
        return new TableCellReader(spec, row);
    }

    public String key() {
        return row.getKey().getString();
    }

    public String string(final String colName) {
        return ((StringCell) row.getCell(spec.findColumnIndex(colName))).getStringValue();
    }

    public String stringOptional(final String colName) {
        return optionalData(colName) ? null :  string(colName);
    }

    public Calendar date(final String colName) {
        return ((DateAndTimeCell) row.getCell(spec.findColumnIndex(colName))).getUTCCalendarClone();
    }

    public Date dateOptional(final String colName) {
        return optionalData(colName)  ? null : date(colName).getTime();
    }

    public Set<String> stringSetOptional(final String colName) {
        return optionalData(colName) ? Collections.<String>emptySet() : stringSet(colName);
    }
    
    public List<String> stringListOptional(final String colName) {
    	return optionalData(colName) ? Collections.<String>emptyList() : stringListCellSet(colName);
    }

    public Set<String> stringSet(final String colName) {
        SetCell set = ((SetCell) row.getCell(spec.findColumnIndex(colName)));
        return newHashSet(Iterables.transform(set, new Function<DataCell, String>() {
            @Override
            public String apply(final DataCell cell) {
                return ((StringCell) cell).getStringValue();
            }
        }));
    }
    
    public List<String> stringListCellSet(final String colName) {
        ListCell set = ((ListCell) row.getCell(spec.findColumnIndex(colName)));
        ArrayList<String> returned = new ArrayList<String>();
        for(DataCell cell : set){
        	String value = ((StringCell)cell).getStringValue(); 
        	returned.add(value);
        }
        
        return returned;
    }

    private boolean optionalData(final String colName) {
        return !spec.containsName(colName) || row.getCell(spec.findColumnIndex(colName)).isMissing();
    }
}
