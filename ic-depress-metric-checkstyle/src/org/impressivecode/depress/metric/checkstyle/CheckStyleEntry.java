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
package org.impressivecode.depress.metric.checkstyle;

/**
 * 
 * @author Tomasz Banach
 * @author Łukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class CheckStyleEntry {

    private String lineNumber;
    private String columnNumber;
    private String fileName;
    private String severityType;
    private String messageText;
    private String sourcePlace;

    public void setLineNumber(final String lineNumberValue) {
        this.lineNumber = lineNumberValue;
    }

    public void setColumnNumber(final String columnNumberValue) {
        this.columnNumber = columnNumberValue;
    }

    public void setMessageText(final String messageTextValue) {
        this.messageText = messageTextValue;
    }

    public void setSeverityType(final String severityTypeValue) {
        this.severityType = severityTypeValue;
    }

    public void setSourcePlace(final String sourcePlaceValue) {
        this.sourcePlace = sourcePlaceValue;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getColumnNumber() {
        return columnNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSeverityType() {
        return severityType;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSourcePlace() {
        return sourcePlace;
    }


    @Override
    public String toString() {
        return String
                .format("CheckStyleEntry [lineNumber=%s, fileName=%s, severityType=%s, messageText=%s, sourcePlace=%s, columnNumber=%s]",
                        lineNumber, fileName, severityType, messageText, sourcePlace, columnNumber);
    }
}
