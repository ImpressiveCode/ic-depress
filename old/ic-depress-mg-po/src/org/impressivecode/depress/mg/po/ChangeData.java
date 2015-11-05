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
package org.impressivecode.depress.mg.po;

import java.util.List;

import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ChangeData {
    private String className;
    private List<String> involvedEngineers;

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public List<String> getInvolvedEngineers() {
        return involvedEngineers;
    }

    public void setInvolvedEngineers(final List<String> involvedEngineers) {
        this.involvedEngineers = involvedEngineers;
    }

    @Override
    public String toString() {
        return String.format("ChangeData [className=%s, involvedEngineers=%s]", className,
                Iterables.toString(involvedEngineers));
    }
}
