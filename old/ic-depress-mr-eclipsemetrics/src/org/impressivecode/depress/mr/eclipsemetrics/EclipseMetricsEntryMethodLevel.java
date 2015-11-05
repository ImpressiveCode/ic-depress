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
package org.impressivecode.depress.mr.eclipsemetrics;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntryMethodLevel implements EclipseMetricsEntry {

    private String methodName;
    private Double methodLinesOfCode;
    private Double nestedBlockDepth;
    private Double mcCabeCyclomaticComplexity;
    private Double numberOfParameters;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mcCabeCyclomaticComplexity == null) ? 0 : mcCabeCyclomaticComplexity.hashCode());
        result = prime * result + ((methodLinesOfCode == null) ? 0 : methodLinesOfCode.hashCode());
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((nestedBlockDepth == null) ? 0 : nestedBlockDepth.hashCode());
        result = prime * result + ((numberOfParameters == null) ? 0 : numberOfParameters.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EclipseMetricsEntryMethodLevel))
            return false;
        EclipseMetricsEntryMethodLevel other = (EclipseMetricsEntryMethodLevel) obj;
        return other.toString().equals(this.toString());
    }

    public String getMethodName() {
        return methodName;
    }

    public Double getMethodLinesOfCode() {
        return methodLinesOfCode;
    }

    public Double getNestedBlockDepth() {
        return nestedBlockDepth;
    }

    public Double getMcCabeCyclomaticComplexity() {
        return mcCabeCyclomaticComplexity;
    }

    public Double getNumberOfParameters() {
        return numberOfParameters;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethodLinesOfCode(Double methodLinesOfCode) {
        this.methodLinesOfCode = methodLinesOfCode;
    }

    public void setNestedBlockDepth(Double nestedBlockDepth) {
        this.nestedBlockDepth = nestedBlockDepth;
    }

    public void setMcCabeCyclomaticComplexity(Double mcCabeCyclomaticComplexity) {
        this.mcCabeCyclomaticComplexity = mcCabeCyclomaticComplexity;
    }

    public void setNumberOfParameters(Double numberOfParameters) {
        this.numberOfParameters = numberOfParameters;
    }

    @Override
    public String toString() {
        return String.format("EclipseMetricsEntry [methodName=%s, methodLinesOfCode=%s, nestedBlockDepth=%s, "
                + "mcCabeCyclomaticComplexity=%s, numberOfParameters=%s]", methodName, methodLinesOfCode,
                nestedBlockDepth, mcCabeCyclomaticComplexity, numberOfParameters);
    }

    public void setValue(String metricId, Double value) {
        switch (metricId) {
        case "MLOC":
            methodLinesOfCode = value;
            break;
        case "NBD":
            nestedBlockDepth = value;
            break;
        case "VG":
            mcCabeCyclomaticComplexity = value;
            break;
        case "PAR":
            numberOfParameters = value;
            break;
        default:
            break;
        }
    }
}
