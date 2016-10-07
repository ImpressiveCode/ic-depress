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
package org.impressivecode.depress.mr.googleaudit;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 * @author Katarzyna Debowa, Wroclaw University of Technology
 * @author Pawel Krzos, Wroclaw University of Technology
 */
public class GoogleAuditEntry {

    private String resourceName;
    
    private Double high;
    private Double medium;
    private Double low;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((high == null) ? 0 : high.hashCode());
        result = prime * result + ((medium == null) ? 0 : medium.hashCode());
        result = prime * result + ((low == null) ? 0 : low.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
    	
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof GoogleAuditEntry))
            return false;
        GoogleAuditEntry other = (GoogleAuditEntry) obj;
        return other.toString().equals(this.toString());
    }

    public String getResourceName() {
        return resourceName;
    }

    public Double getHigh() {
        return high;
    }

    public Double getMedium() {
        return medium;
    }

    public Double getLow() {
        return low;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public void setMedium(Double medium) {
        this.medium = medium;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return String.format("GoogleAuditEntry [methodName=%s, high=%s, medium=%s, "
                + "low=%s]", resourceName, high, medium, low);
    }

	public void setSeverityValues(Double high, Double medium, Double low) {
		this.high = high;
		this.medium = medium;
		this.low = low;
	}
}
