package org.impressivecode.depress.metric.astcompare.ast;

/**
 * @author Piotr Mitka
 */
public class SingleChangeInfo {
    // revision info
    private String author;
    private String fileName;
    private String revisionId;
    private String comment;
    private long timestamp;
    // change info
    private String changeEntity;
    private String changeType;
    private String methodName;
    // bug prone
    private boolean bugProne;

    @Override
    public String toString() {
        return "SingleChangeInfo [author=" + author + ", fileName=" + fileName + ", revisionId=" + revisionId
                + ", comment=" + comment + ", timestamp=" + timestamp + ", changeEntity=" + changeEntity
                + ", changeType=" + changeType + ", methodName=" + methodName + ", bugProne=" + bugProne + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + (bugProne ? 1231 : 1237);
        result = prime * result + ((changeEntity == null) ? 0 : changeEntity.hashCode());
        result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((revisionId == null) ? 0 : revisionId.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SingleChangeInfo other = (SingleChangeInfo) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (bugProne != other.bugProne)
            return false;
        if (changeEntity == null) {
            if (other.changeEntity != null)
                return false;
        } else if (!changeEntity.equals(other.changeEntity))
            return false;
        if (changeType == null) {
            if (other.changeType != null)
                return false;
        } else if (!changeType.equals(other.changeType))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (revisionId == null) {
            if (other.revisionId != null)
                return false;
        } else if (!revisionId.equals(other.revisionId))
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChangeEntity() {
        return changeEntity;
    }

    public void setChangeEntity(String changeEntity) {
        this.changeEntity = changeEntity;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isBugProne() {
        return bugProne;
    }

    public void setBugProne(boolean bugProne) {
        this.bugProne = bugProne;
    }

    public boolean hasDataCorrect() {
        return !author.equals("") && !fileName.equals("") && !revisionId.equals("") && !comment.equals("")
                && timestamp > 0 && !changeEntity.equals("") && !changeType.equals("") && !methodName.equals("")
                && methodName.toLowerCase().contains(fileName.toLowerCase().replace(".java", ""));
    }

}
