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
package org.impressivecode.depress.support.sourcecrawler;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * 
 */
public enum ColumnNames {
    RESOURCE("Resource") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.RESOURCE.toString(), StringCell.TYPE).createSpec();
        }
    },
    IS_EXCEPTION("Is exception") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.IS_EXCEPTION.toString(), StringCell.TYPE).createSpec();
        }
    },
    IS_INNER("Is inner") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.IS_INNER.toString(), StringCell.TYPE).createSpec();
        }
    },
    IS_TEST("Is test") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.IS_TEST.toString(), StringCell.TYPE).createSpec();
        }
    },
    TYPE("Type") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.TYPE.toString(), StringCell.TYPE).createSpec();
        }
    },
    PACKAGE("Package") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.PACKAGE.toString(), StringCell.TYPE).createSpec();
        }
    },
    PATH("Path") {
        @Override
        public DataColumnSpec getDataColumnCreator() {
            return new DataColumnSpecCreator(ColumnNames.PATH.toString(), StringCell.TYPE).createSpec();
        }
    };

    private String name;

    private ColumnNames(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract DataColumnSpec getDataColumnCreator();

    public static List<DataColumnSpec> getAllSpec() {
        ArrayList<DataColumnSpec> arrayList = new ArrayList<>();
        ColumnNames[] allSpec = ColumnNames.values();
        for (ColumnNames columnNames : allSpec) {
            arrayList.add(columnNames.getDataColumnCreator());
        }
        return arrayList;

    }
}
