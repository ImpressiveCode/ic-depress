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
package org.impressivecode.depress.its;

import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;

/**
 * Abstract class for creating ITS filters. Provides the basic interface to
 * create dialog components and generate output string, which can be used to add
 * restrictions for any issue tracking system
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * 
 */
public abstract class ITSFilter {

    private List<DialogComponent> dialogComponents;

    protected String filterModelId;

    public ITSFilter() {
        dialogComponents = createDialogComponents();
        filterModelId = getFilterModelId();
    }

    public abstract List<DialogComponent> createDialogComponents();

    public abstract String getName();

    public abstract String getFilterValue();

    public abstract String getFilterModelId();

    @Override
    public String toString() {
        return getName();
    }

    public List<DialogComponent> getDialogComponents() {
        return dialogComponents;
    }
}
