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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Pawel Nosal, ImpressiveCode
 */
@XmlRootElement(name = "root")
public class SourceCrawlerOutput {

    private List<SourceFile> sourceFiles;

    @XmlElement(name = "file")
    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(final List<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }
}
