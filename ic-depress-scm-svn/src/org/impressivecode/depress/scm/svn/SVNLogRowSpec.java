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

package org.impressivecode.depress.scm.svn;

import java.util.ArrayList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnDomainCreator;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;

public class SVNLogRowSpec {

	private static ArrayList<String> columns;

	static {
		columns = new ArrayList<>();
		columns.add("ClassName");
		columns.add("Author");
		columns.add("Message");
		columns.add("Marker");
		columns.add("Action");
		columns.add("Path");
		columns.add("Date");
		columns.add("Uid");
	}

	public static DataColumnSpec[] createColumnSpec() {

		DataColumnSpec[] result = new DataColumnSpec[columns.size()];

		int index = -1;
		for (String item : columns) {
			result[++index] = stringColumnSpec(item);
		}

		return result;
	}

	public static DataRow createRow(int rowId, SVNLogRow inRow) {

		DataCell[] cells = new DataCell[columns.size()];

		cells[0] = new StringCell(inRow.getClassName());
		cells[1] = new StringCell(inRow.getAuthor());
		cells[2] = new StringCell(inRow.getMessage());
		cells[3] = new StringCell(inRow.getMarker());
		cells[4] = new StringCell(inRow.getAction());
		cells[5] = new StringCell(inRow.getPath());
		cells[6] = new StringCell(inRow.getDate());
		cells[7] = new StringCell(inRow.getUid());

		return new DefaultRow(new RowKey(Integer.toString(rowId)), cells);
	}

	public static DataColumnSpec stringColumnSpec(String inName) {

		DataColumnSpecCreator colSpecCreator = new DataColumnSpecCreator(
				inName, StringCell.TYPE);

		DataColumnDomainCreator domainCreator = new DataColumnDomainCreator(
				new StringCell(""), new StringCell(""));

		colSpecCreator.setDomain(domainCreator.createDomain());

		return colSpecCreator.createSpec();
	}
}
