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
package org.impressivecode.depress.support.commonmarker;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerAdapterTableFactory {
    public static final String MARKER = "Marker";
    public static final String EXT_MARKER = "Marker(+)";
    public static final String AM_MARKER = "Marker(a)";
    public final static String SYNTACTIC_CONFIDENCE_COLNAME = "SynConf";
    public final static String SEMANTIC_CONFIDENCE_COLNAME = "SemConf";

    public static final DataColumnSpec MARKER_COLSPEC = new DataColumnSpecCreator(MARKER,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();

    public static final DataColumnSpec EXT_MARKER_COLSPEC = new DataColumnSpecCreator(EXT_MARKER,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();

    public static final DataColumnSpec SYNTACTIC_CONFIDENCE_COLSPEC = new DataColumnSpecCreator(
            SYNTACTIC_CONFIDENCE_COLNAME, IntCell.TYPE).createSpec();

    public static final DataColumnSpec SEMANTIC_CONFIDENCE_COLSPEC = new DataColumnSpecCreator(
            SEMANTIC_CONFIDENCE_COLNAME, SetCell.getCollectionType(IntCell.TYPE)).createSpec();

    public static final DataColumnSpec AM_MARKER_COLSPEC = new DataColumnSpecCreator(AM_MARKER,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();
}
