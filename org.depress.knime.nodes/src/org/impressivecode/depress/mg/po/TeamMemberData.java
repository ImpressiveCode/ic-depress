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
package org.impressivecode.depress.mg.po;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class TeamMemberData {
    private String name;
    private boolean exEngineer;
    private Integer exLevel;
    private String organizationPath;

    public String getOrganizationPath() {
        return organizationPath;
    }

    public void setOrganizationPath(final String organizationPath) {
        this.organizationPath = organizationPath;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setExEngineer(final boolean exEngineer) {
        this.exEngineer = exEngineer;
    }

    public void setExLevel(final Integer exLevel) {
        this.exLevel = exLevel;
    }

    public String getName() {
        return name;
    }

    public boolean isExEngineer() {
        return exEngineer;
    }

    public Integer getExLevel() {
        return exLevel;
    }

    @Override
    public String toString() {
        return String.format("TeamMemberData [name=%s, exEngineer=%s, exLevel=%s, organizationPath=%s]",
                name, exEngineer, exLevel, organizationPath);
    }
}
