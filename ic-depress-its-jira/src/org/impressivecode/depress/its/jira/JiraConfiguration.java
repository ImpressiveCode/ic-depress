package org.impressivecode.depress.its.jira;

import java.util.Arrays;

import org.impressivecode.depress.its.ITSPriority;

public enum JiraConfiguration {
    TRIVIAL("depress.its.jira.trivial"), MINOR("depress.its.jira.minor"), MAJOR("depress.its.jira.major"),
    CRITICAL("depress.its.jira.critical"), BLOCKER("depress.its.jira.blocker"), UNKNOWN("depress.its.jira.unknown");
    
    private final String label;
    
    private JiraConfiguration(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static String[] labels() {
        return Arrays.toString(ITSPriority.values()).replaceAll("^.|.$", "").split(", ");
    }
}
