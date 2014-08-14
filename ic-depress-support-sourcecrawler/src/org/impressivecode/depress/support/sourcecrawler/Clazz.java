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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * 
 */
@XmlRootElement(name = "class")
public class Clazz {
    private String type;
    private String name;
    private String access;
    private boolean isException;
    private boolean isInner;
    private boolean isTest;
    private boolean isFinal;

    @XmlElement
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "exception")
    public boolean isException() {
        return isException;
    }

    public void setException(final boolean isException) {
        this.isException = isException;
    }

    @XmlElement(name = "inner")
    public boolean isInner() {
        return isInner;
    }

    public void setInner(final boolean isInner) {
        this.isInner = isInner;
    }

    @XmlElement(name = "test")
    public boolean isTest() {
        return isTest;
    }

    public void setTest(final boolean isTest) {
        this.isTest = isTest;
    }

    @XmlElement(name = "access")
	public String getAccess() {
		return access;
	}

	public void setAccess(final String access) {
		this.access = access;
	}

	@XmlElement(name = "final")
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

}
