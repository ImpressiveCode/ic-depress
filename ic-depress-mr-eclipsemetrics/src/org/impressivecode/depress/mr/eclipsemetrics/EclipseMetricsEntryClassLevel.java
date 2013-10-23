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
public class EclipseMetricsEntryClassLevel implements EclipseMetricsEntry {

    private String className;
    private Double numberOfOverriddenMethods;
    private Double numberOfAttributes;
    private Double numberOfChildren;
    private Double numberOfMethods;
    private Double depthOfInheritanceTree;
    private Double lackOfCohesionOfMethods;
    private Double numberOfStaticMethods;
    private Double specializationIndex;
    private Double weightedMethodsPerClass;
    private Double numberOfStaticAttributes;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((depthOfInheritanceTree == null) ? 0 : depthOfInheritanceTree.hashCode());
        result = prime * result + ((lackOfCohesionOfMethods == null) ? 0 : lackOfCohesionOfMethods.hashCode());
        result = prime * result + ((numberOfAttributes == null) ? 0 : numberOfAttributes.hashCode());
        result = prime * result + ((numberOfChildren == null) ? 0 : numberOfChildren.hashCode());
        result = prime * result + ((numberOfMethods == null) ? 0 : numberOfMethods.hashCode());
        result = prime * result + ((numberOfOverriddenMethods == null) ? 0 : numberOfOverriddenMethods.hashCode());
        result = prime * result + ((numberOfStaticAttributes == null) ? 0 : numberOfStaticAttributes.hashCode());
        result = prime * result + ((numberOfStaticMethods == null) ? 0 : numberOfStaticMethods.hashCode());
        result = prime * result + ((specializationIndex == null) ? 0 : specializationIndex.hashCode());
        result = prime * result + ((weightedMethodsPerClass == null) ? 0 : weightedMethodsPerClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EclipseMetricsEntryClassLevel))
            return false;
        EclipseMetricsEntryClassLevel other = (EclipseMetricsEntryClassLevel) obj;
        return other.toString().equals(this.toString());
    }

    public String getClassName() {
        return className;
    }

    public Double getNumberOfOverriddenMethods() {
        return numberOfOverriddenMethods;
    }

    public Double getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public Double getNumberOfChildren() {
        return numberOfChildren;
    }

    public Double getNumberOfMethods() {
        return numberOfMethods;
    }

    public Double getDepthOfInheritanceTree() {
        return depthOfInheritanceTree;
    }

    public Double getLackOfCohesionOfMethods() {
        return lackOfCohesionOfMethods;
    }

    public Double getNumberOfStaticMethods() {
        return numberOfStaticMethods;
    }

    public Double getSpecializationIndex() {
        return specializationIndex;
    }

    public Double getWeightedMethodsPerClass() {
        return weightedMethodsPerClass;
    }

    public Double getNumberOfStaticAttributes() {
        return numberOfStaticAttributes;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setNumberOfOverriddenMethods(Double numberOfOverriddenMethods) {
        this.numberOfOverriddenMethods = numberOfOverriddenMethods;
    }

    public void setNumberOfAttributes(Double numberOfAttributes) {
        this.numberOfAttributes = numberOfAttributes;
    }

    public void setNumberOfChildren(Double numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setNumberOfMethods(Double numberOfMethods) {
        this.numberOfMethods = numberOfMethods;
    }

    public void setDepthOfInheritanceTree(Double depthOfInheritanceTree) {
        this.depthOfInheritanceTree = depthOfInheritanceTree;
    }

    public void setLackOfCohesionOfMethods(Double lackOfCohesionOfMethods) {
        this.lackOfCohesionOfMethods = lackOfCohesionOfMethods;
    }

    public void setNumberOfStaticMethods(Double numberOfStaticMethods) {
        this.numberOfStaticMethods = numberOfStaticMethods;
    }

    public void setSpecializationIndex(Double specializationIndex) {
        this.specializationIndex = specializationIndex;
    }

    public void setWeightedMethodsPerClass(Double weightedMethodsPerClass) {
        this.weightedMethodsPerClass = weightedMethodsPerClass;
    }

    public void setNumberOfStaticAttributes(Double numberOfStaticAttributes) {
        this.numberOfStaticAttributes = numberOfStaticAttributes;
    }

    @Override
    public String toString() {
        return String
                .format("EclipseMetricsEntry [className=%s, numberOfOverriddenMethods=%s, numberOfAttributes=%s, "
                        + "numberOfChildren=%s, numberOfMethods=%s, depthOfInheritanceTree=%s, lackOfCohesionOfMethods=%s, "
                        + "numberOfStaticMethods=%s, specializationIndex=%s, weightedMethodsPerClass=%s, numberOfStaticAttributes=%s]",
                        className, numberOfOverriddenMethods, numberOfAttributes, numberOfChildren, numberOfMethods,
                        depthOfInheritanceTree, lackOfCohesionOfMethods, numberOfStaticMethods, specializationIndex,
                        weightedMethodsPerClass, numberOfStaticAttributes);
    }

    public void setValue(String metricId, Double value) {
        switch (metricId) {
        case "NORM":
            numberOfOverriddenMethods = value;
            break;
        case "NOF":
            numberOfAttributes = value;
            break;
        case "NSC":
            numberOfChildren = value;
            break;
        case "NOM":
            numberOfMethods = value;
            break;
        case "DIT":
            depthOfInheritanceTree = value;
            break;
        case "LCOM":
            lackOfCohesionOfMethods = value;
            break;
        case "NSM":
            numberOfStaticMethods = value;
            break;
        case "SIX":
            specializationIndex = value;
            break;
        case "WMC":
            weightedMethodsPerClass = value;
            break;
        case "NSF":
            numberOfStaticAttributes = value;
            break;
        default:
            break;
        }
    }
}
