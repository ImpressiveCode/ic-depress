package org.impressivecode.depress.scm.svn;

import org.knime.core.node.CanceledExecutionException;

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

public class SVNLogLoader {

	public interface IReadProgressListener {

		void onReadProgress(int inProgres, SVNLogRow inRow)
				throws CanceledExecutionException;

	}

	public void loadXmlL(String inPath, String inIsueMarker, String inPackage,
			IReadProgressListener inProgress) {
		try {
			for (int i = 0; i < 100; ++i) {

				inProgress.onReadProgress(i, new SVNLogRow());

			}
		} catch (CanceledExecutionException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}