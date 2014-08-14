package org.impressivecode.depress.mr.astcompare.svn;

import java.util.Comparator;

import org.eclipse.team.core.history.IFileRevision;

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
public class FileRevisionComparable implements Comparator<IFileRevision> {

    @Override
    public int compare(IFileRevision fr1, IFileRevision fr2) {
        if (fr1.getTimestamp() > fr2.getTimestamp()) {
            return 1;
        } else if (fr1.getTimestamp() < fr2.getTimestamp()) {
            return -1;
        } else {
            return 0;
        }
    }

}
