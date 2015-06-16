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
package org.impressivecode.depress.scm.git;

import java.util.Date;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class GitOnlineAdapterExtendedEntry {

    public String getKey() {
        return key;
    }

    private final String key;
    private String author;
    private String message;
    private Date date;
    private String commitId;
    private String methodName;

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this).addValue(this.commitId).addValue(this.methodName)
                .addValue(this.date).addValue(this.author).toString();
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public static final int AUTHOR = 0;
    public static final int MESSAGE = 1;
    public static final int DATE = 2;
    public static final int COMMIT_ID = 3;
    public static final int METHOD_NAME = 4;

    public GitOnlineAdapterExtendedEntry(String key) {
        this.key = key;
    }

    public boolean hasResults() {
        return hasMetric(date) || hasMetric(commitId);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(author, message, date, commitId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GitOnlineAdapterExtendedEntry other = (GitOnlineAdapterExtendedEntry) obj;

        return com.google.common.base.Objects.equal(key, other.key)
                && com.google.common.base.Objects.equal(author, other.author)
                && com.google.common.base.Objects.equal(message, other.message)
                && com.google.common.base.Objects.equal(date, other.date)
                && com.google.common.base.Objects.equal(commitId, other.commitId);
    }

    public void setValue(int metricName, Object value) {
        switch (metricName) {
        case AUTHOR:
            if (value instanceof String) {
                author = (String) value;
            } else {
                throw new IllegalArgumentException();
            }
            break;
        case MESSAGE:
            if (value instanceof String) {
                message = (String) value;
            } else {
                throw new IllegalArgumentException();
            }
            break;
        case DATE:
            if (value instanceof Date) {
                date = (Date) value;
            } else {
                throw new IllegalArgumentException();
            }
            break;
        case COMMIT_ID:
            if (value instanceof String) {
                commitId = (String) value;
            } else {
                throw new IllegalArgumentException();
            }
            break;
        case METHOD_NAME:
            if (value instanceof String) {
                methodName = (String) value;
            } else {
                throw new IllegalArgumentException();
            }
            break;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    private boolean hasMetric(Date value) {
        if (value != null) {
            return true;
        }
        return false;
    }

    private boolean hasMetric(String value) {
        if (value != null) {
            return !("".equals(value));
        }
        return false;
    }
}