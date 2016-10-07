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
package org.impressivecode.depress.matcher.commonmarker;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerDataType {
    private Set<String> markers = Collections.emptySet();

    private String resourceName;

    private Set<String> amarkers = Collections.emptySet();

    private Set<String> emarkers = Collections.emptySet();

    public void setResourceName(final String resource) {
        this.resourceName = resource;
    }

    public void setMarkers(final Set<String> markers) {
        this.markers = markers;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Set<String> getMarkers() {
        return markers;
    }

    public void setActivityMarkers(final Set<String> markers) {
        this.amarkers = markers;
    }

    public Set<String> getActivityMarkers() {
        return amarkers;
    }

    public void setExtendedMarkers(final Set<String> markers) {
        this.emarkers = markers;
    }

    public Set<String> getExtendedMarkers() {
        return emarkers;
    }

    public Set<String> getAllMarkers() {
        return new ImmutableSet.Builder<String>().addAll(amarkers).addAll(emarkers).addAll(markers).build();
    }
}
