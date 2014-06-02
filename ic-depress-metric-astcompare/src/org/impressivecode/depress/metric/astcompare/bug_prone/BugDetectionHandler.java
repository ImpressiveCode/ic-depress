package org.impressivecode.depress.metric.astcompare.bug_prone;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Piotr Mitka
 */
public class BugDetectionHandler {
    private static final String[] REG_EXP = { "#[0-9]+", "pr[# \t]*[0-9]+", "show\\_bug\\.cgi\\?id=[0-9]+",
            "\\[[0-9]+\\]", "fix(e[ds])?|bugs?|defects?|patch|jira|issue" };

    public static boolean isMethodBugProne(String comment) {
        return checkApacheJiraIfBugProne(comment);
    }

    private static boolean checkApacheJiraIfBugProne(String comment) {
        String jiraIssue = "";
        Pattern p = Pattern.compile("([A-Z]+-{1}[0-9]+)");
        Matcher m = p.matcher(comment);

        try {
            if (m.find()) {
                jiraIssue = m.group(1);
                if (jiraIssue != null && !jiraIssue.equals("")) {
                    Document doc = Jsoup.connect("https://issues.apache.org/jira/browse/" + jiraIssue).get();
                    Elements span = doc.select("span#type-val");
                    String issueType = span.text();
                    return "bug".equalsIgnoreCase(issueType);
                }
            }
        } catch (IOException e) {
            return checkIfBugProneBaseOnComment(comment);
        }
        return checkIfBugProneBaseOnComment(comment);
    }

    private static boolean checkIfBugProneBaseOnComment(String comment) {
        if (comment != null && !comment.equals("")) {
            for (String regExp : REG_EXP) {
                if (isRegExpMatch(comment.toLowerCase(), regExp)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isRegExpMatch(String comment, String regExp) {
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(comment);
        return m.find();
    }

}