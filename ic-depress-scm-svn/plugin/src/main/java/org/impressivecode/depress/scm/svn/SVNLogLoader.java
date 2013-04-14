package org.impressivecode.depress.scm.svn;

import org.knime.core.data.def.StringCell;
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

	public void loadXml(String inPath, String inIsueMarker, String inPackage,
			IReadProgressListener inProgress) {

		Logger.instance().warn("Start loading..");

		Logger.instance().warn("Path : " + inPath);
		Logger.instance().warn("IsueMarker : " + inIsueMarker);
		Logger.instance().warn("Package : " + inPackage);

		try {
			for (int i = 0; i < 100; ++i) {

				SVNLogRow r = new SVNLogRow();

				r.setClassName(new StringCell(" ClassName  " + i));
				r.setAuthor(new StringCell(" Author  " + i));
				r.setAction(new StringCell(" Action  " + i));
				r.setDate(new StringCell(" Date  " + i));
				r.setMarker(new StringCell(" Maker  " + i));
				r.setMessage(new StringCell(" Message  " + i));
				r.setPath(new StringCell(" Path  " + i));
				r.setUid(new StringCell(" Uid  " + i));

				inProgress.onReadProgress(i, r);

			}
		} catch (CanceledExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.instance().warn("End loading..");
	}

}
