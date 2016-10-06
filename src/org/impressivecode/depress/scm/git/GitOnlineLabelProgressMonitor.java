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
package org.impressivecode.depress.scm.git;

import org.eclipse.jgit.lib.TextProgressMonitor;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;

public class GitOnlineLabelProgressMonitor extends TextProgressMonitor{

    private final DialogComponentLabel label;
    private final String baseText;

    public GitOnlineLabelProgressMonitor(final DialogComponentLabel label, final String baseText){
        this.label = label;
        this.baseText = baseText;
    }

    @Override
    protected void onUpdate(final String taskName, final int workCurr){
        this.label.setText(this.baseText + taskName+" "+workCurr);
    }

    @Override
    protected void onEndTask(final String taskName, final int workCurr){
        this.label.setText(this.baseText + taskName+" completed");
    }

    @Override
    protected void onUpdate(final String taskName, final int cmp, final int totalWork, final int pcnt){
        this.label.setText(this.baseText + taskName + " " + pcnt + "% ("+cmp+"/"+totalWork+")");
    }

    @Override
    protected void onEndTask(final String taskName, final int cmp, final int totalWork, final int pcnt){
        this.label.setText(this.baseText + taskName+" completed");
    }
}
