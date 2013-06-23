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
package org.impressivecode.depress.metric.pmd;

/**
 * 
 * @author Tomasz Banach
 * @author �ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDEntry {

    private String beginLine;
    private String endLine;
    private String beginColumn;
    private String endColumn;
    private String rule;
    private String messageText;
    private String ruleSet;
    private String className;
    private String infoUrl;
    private String priority;

    public void setBeginLine(final String beginLineValue) {
        this.beginLine = beginLineValue;
    }

    public void setEndLine(final String endLineValue) {
        this.endLine = endLineValue;
    }

    public void setBeginColumn(final String beginColumnValue) {
        this.beginColumn = beginColumnValue;
    }

    public void setEndColumn(final String endColumnValue) {
        this.endColumn = endColumnValue;
    }

    public void setMessageText(final String messageTextValue) {
        this.messageText = messageTextValue;
    }

    public void setRule(final String ruleValue) {
        this.rule = ruleValue;
    }

    public void setRuleSet(final String ruleSetValue) {
        this.ruleSet = ruleSetValue;
    }

    public void setClassName(final String classNameValue) {
        this.className = classNameValue;
    }

    public void setInfoUrl(final String infoUrlValue) {
        this.infoUrl = infoUrlValue;
    }

    public void setPriority(final String priorityValue) {
        this.priority = priorityValue;
    }

    public String getBeginLine() {
        return beginLine;
    }

    public String getEndLine() {
        return endLine;
    }

    public String getBeginColumn() {
        return beginColumn;
    }

    public String getEndColumn() {
        return endColumn;
    }

    public String getRule() {
        return rule;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public String getClassName() {
        return className;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return String
                .format("PMDEntry [beginLine=%s, endLine=%s, rule=%s, messageText=%s, ruleSet=%s, beginColumn=%s, endColumn=%s, infoUrl=%s, priority=%s]",
                        beginLine, endLine, rule, messageText, ruleSet, beginColumn, endColumn, infoUrl,
                        priority);
    }

}
