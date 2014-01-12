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
package org.impressivecode.depress.its.jiraonline.historymodel;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Model class used by Jackson for Jira JSON parsing.
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraOnlineIssueHistory {

    private String key;
    private JiraOnlineIssueChangelog changelog;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JiraOnlineIssueChangelog getChangelog() {
        return changelog;
    }

    public void setChangelog(JiraOnlineIssueChangelog changelog) {
        this.changelog = changelog;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class JiraOnlineIssueChangelog {

        @JsonProperty("histories")
        private List<JiraOnlineIssueChanges> histories;

        public List<JiraOnlineIssueChanges> getHistories() {
            return histories;
        }

        public void setHistories(List<JiraOnlineIssueChanges> histories) {
            this.histories = histories;
        }

    }

}
