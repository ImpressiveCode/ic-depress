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
package org.impressivecode.depress.support.sematicanalysis;

import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

import com.google.common.base.Optional;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class Configuration {
    private final Optional<Integer> interval;
    private final Optional<Integer> intervalWeight;
    private List<ITSDataType> itsData;

    public Configuration(final SettingsModelInteger interval, final SettingsModelInteger intervalWeight,  final List<ITSDataType> itsData) {
        this.interval = Optional.of(interval.getIntValue());
        this.intervalWeight = Optional.of(intervalWeight.getIntValue());
        this.itsData = itsData;
    }
}