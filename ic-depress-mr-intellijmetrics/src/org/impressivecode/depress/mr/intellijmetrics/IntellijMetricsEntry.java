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
package org.impressivecode.depress.mr.intellijmetrics;

/**
 * 
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 * 
 */
public class IntellijMetricsEntry {

    private String className;
    private int severityTypoCounter;
    private int severityInfoCounter;
    private int severityServerProblemCounter;
    private int severityWeakWarningCounter;
    private int severityWarningCounter;
    private int severityErrorCounter;

    public void setClassName(final String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setSeverityTypoCounter(int value) {
        this.severityTypoCounter = value;
    }

    public int getSeverityTypoCounter() {
        return this.severityTypoCounter;
    }

    public void setSeverityInfoCounter(int value) {
        this.severityInfoCounter = value;
    }

    public int getSeverityInfoCounter() {
        return this.severityInfoCounter;
    }

    public void setSeverityServerProblemCounter(int value) {
        this.severityServerProblemCounter = value;
    }

    public int getSeverityServerProblemCounter() {
        return this.severityServerProblemCounter;
    }

    public void setSeverityWeakWarningCounter(int value) {
        this.severityWeakWarningCounter = value;
    }

    public int getSeverityWeakWarningCounter() {
        return this.severityWeakWarningCounter;
    }

    public void setSeverityWarningCounter(int value) {
        this.severityWarningCounter = value;
    }

    public int getSeverityWarningCounter() {
        return this.severityWarningCounter;
    }

    public void setSeverityErrorCounter(int value) {
        this.severityErrorCounter = value;
    }

    public int getSeverityErrorCounter() {
        return this.severityErrorCounter;
    }

    public void setValue(String metricId, int value) {
        switch (metricId) {
        case "TYPO":
            this.severityTypoCounter = value;
            break;
        case "INFO":
            this.severityInfoCounter = value;
            break;
        case "WARNING":
            this.severityWarningCounter = value;
            break;
        case "SERVER PROBLEM":
            this.severityServerProblemCounter = value;
            break;
        case "WEAK WARNING":
            this.severityWeakWarningCounter = value;
            break;
        case "ERROR":
            this.severityErrorCounter = value;
            break;
        default:
            break;
        }
    }

    public int getValue(String metricId) {
        switch (metricId) {
        case "TYPO":
            return this.severityTypoCounter;
        case "INFO":
            return this.severityInfoCounter;
        case "WARNING":
            return this.severityWarningCounter;
        case "SERVER PROBLEM":
            return this.severityServerProblemCounter;
        case "WEAK WARNING":
            return this.severityWeakWarningCounter;
        case "ERROR":
            return this.severityErrorCounter;
        default:
            return 0;
        }
    }

    @Override
    public String toString() {
        return String
                .format("IntellijMetricsEntry [className=%s, TYPO=%s, INFO=%s, WARNING=%s, SERVER PROBLEM=%s, WEAK WARNING=%s, ERROR=%s]",
                        className, severityTypoCounter, severityInfoCounter, severityWarningCounter,
                        severityServerProblemCounter, severityWeakWarningCounter, severityErrorCounter);
    }
}