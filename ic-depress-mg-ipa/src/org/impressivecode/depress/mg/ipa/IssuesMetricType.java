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
package org.impressivecode.depress.mg.ipa;

import java.util.Collections;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;

import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class IssuesMetricType {
    private String className;
    private List<ITSDataType> issues = Collections.emptyList();

    public String getResourceName() {
        return className;
    }

    public void setResourceName(final String className) {
        this.className = className;
    }

    public List<ITSDataType> getIssues() {
        return issues;
    }

    public void setIssues(final List<ITSDataType> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return String.format("NoIDataType [className=%s, issues=%s]", className, Iterables.toString(issues));
    }
}
