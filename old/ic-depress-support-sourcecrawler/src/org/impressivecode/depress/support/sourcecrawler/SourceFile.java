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
package org.impressivecode.depress.support.sourcecrawler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Pawel Nosal, ImpressiveCode
 */
@XmlRootElement(name = "file")
public class SourceFile {
    private String path;
    private String sourcePackage;
    private List<Clazz> classes = new ArrayList<Clazz>();

    @XmlElementWrapper(name = "classes")
    @XmlElement(name = "class")
    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(final List<Clazz> classes) {
        this.classes = classes;
    }

    @XmlElement(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    @XmlElement(name = "package")
    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(final String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

}
