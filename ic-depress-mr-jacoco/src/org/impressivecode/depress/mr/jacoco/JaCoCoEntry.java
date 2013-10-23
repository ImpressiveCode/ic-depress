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
package org.impressivecode.depress.mr.jacoco;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoEntry {

    private Double lineCoverageCounter;
    private String className;
    private Double methodCoverageCounter;
    private Double complexityCoverageCounter;
    private Double branchCoverageCounter;
    private Double instructionCoverageCounter;
    private Double classCoverageCounter;

    public void setLineCoverageCounter(final Double lineCoverageCounter) {
        this.lineCoverageCounter = lineCoverageCounter;
    }

    public void setInstructionCoverageCounter(final Double coverageCounterValue) {
        this.instructionCoverageCounter = coverageCounterValue;
    }

    public void setBranchCoverageCounter(final Double coverageCounterValue) {
        this.branchCoverageCounter = coverageCounterValue;
    }

    public void setComplexityCoverageCounter(final Double coverageCounterValue) {
        this.complexityCoverageCounter = coverageCounterValue;
    }

    public void setMethodCoverageCounter(final Double coverageCounterValue) {
        this.methodCoverageCounter = coverageCounterValue;
    }

    public void setClassCoverageCounter(final Double coverageCounterValue) {
        this.classCoverageCounter = coverageCounterValue;
    }

    public Double getClassCoverageCounter() {
        return classCoverageCounter;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public Double getLineCoverageCounter() {
        return lineCoverageCounter;
    }

    public String getClassName() {
        return className;
    }

    public Double getMethodCoverageCounter() {
        return methodCoverageCounter;
    }

    public Double getComplexityCoverageCounter() {
        return complexityCoverageCounter;
    }

    public Double getBranchCoverageCounter() {
        return branchCoverageCounter;
    }

    public Double getInstructionCoverageCounter() {
        return instructionCoverageCounter;
    }

    @Override
    public String toString() {
        return String
                .format("JaCoCoEntry [lineCoverageCounter=%s, className=%s, methodCoverageCounter=%s, complexityCoverageCounter=%s, branchCoverageCounter=%s, instructionCoverageCounter=%s, classCoverageCounter=%s]",
                        lineCoverageCounter, className, methodCoverageCounter, complexityCoverageCounter,
                        branchCoverageCounter, instructionCoverageCounter, classCoverageCounter);
    }
}
