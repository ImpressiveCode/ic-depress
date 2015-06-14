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
package org.impressivecode.depress.mg.astmetrics;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class SingleChangeInfo {
    private String methodName;
    private String author;
    private String revisionId;
    private String changeEntity;
    private String changeType;

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this).addValue(this.methodName).addValue(this.author)
                .addValue(this.revisionId).addValue(this.changeEntity).addValue(this.changeType).toString();
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(methodName, author, revisionId, changeEntity, changeType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SingleChangeInfo other = (SingleChangeInfo) obj;

        return com.google.common.base.Objects.equal(this.methodName, other.methodName)
                && com.google.common.base.Objects.equal(this.author, other.author)
                && com.google.common.base.Objects.equal(this.revisionId, other.revisionId)
                && com.google.common.base.Objects.equal(this.changeEntity, other.changeEntity)
                && com.google.common.base.Objects.equal(this.changeType, other.changeType);
    }

    public String getMethodName() {
        return methodName;
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

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
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

    public boolean hasDataCorrect() {
        return !"".equals(methodName) && !"".equals(author) && !"".equals(revisionId) && !"".equals(changeEntity)
                && !"".equals(changeType);
    }

}
