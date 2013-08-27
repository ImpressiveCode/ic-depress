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
package org.impressivecode.depress.support.commonmarker;

import java.util.Collections;
import java.util.Set;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerDataType {
    private Set<String> markers = Collections.emptySet();

    private String resourceName;

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
}
