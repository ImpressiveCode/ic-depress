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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;

/**
 * Abstract class for time period filters. Provides a two date selection
 * components.
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * 
 */
public abstract class ITSTimePeriodFilter extends ITSFilter {

    public static final int FROM_ID = 0;
    public static final int TO_ID = 1;

    private SettingsModelDate fromModel;
    private SettingsModelDate toModel;

    @Override
    public List<DialogComponent> createDialogComponents() {
        fromModel = new SettingsModelDate(getFilterModelId() + ".from");
        toModel = new SettingsModelDate(getFilterModelId() + ".to");

        List<DialogComponent> dialogComponents = new ArrayList<>();
        dialogComponents.add(FROM_ID, new DialogComponentDate(fromModel, "From"));
        dialogComponents.add(TO_ID, new DialogComponentDate(toModel, "To"));

        return dialogComponents;
    }

    public String getFrom(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(getDateFromComponent(FROM_ID));
    }

    public String getTo(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(getDateFromComponent(TO_ID));
    }

    private Date getDateFromComponent(int componentId) {
        SettingsModelDate settings = (SettingsModelDate) getDialogComponents().get(FROM_ID).getModel();
        return settings.getDate();
    }

    public boolean isFromEnabled() {
        return fromModel.getSelectedFields() > 0;
    }

    public boolean isToEnabled() {
        return toModel.getSelectedFields() > 0;
    }
}
