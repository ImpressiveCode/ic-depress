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
package org.impressivecode.depress.metric.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class POData {
    private Integer NOE = 0;
    private Integer NOEE = 0;
    private Integer EF = 0;
    private Double DMO = 1.0;
    private Double PO = 1.0;
    private Double OCO = 1.0;
    private Double OOW = 1.0;
    private Integer OIF = 1;
    private Integer NOE1 = 0;
    private Integer NOE2 = 0;
    private Integer NOE3 = 0;
    private Integer NOE4 = 0;
    private Integer NOE5 = 0;

    private List<String> involvedDevelopers = new ArrayList<String>();

    public List<String> getInvolvedDevelopers() {
        return involvedDevelopers;
    }

    public void setInvolvedDevelopers(final List<String> involvedDevelopers) {
        this.involvedDevelopers = involvedDevelopers;
    }

    public Integer getNOE() {
        return NOE;
    }

    public void setNOE(final Integer nOE) {
        NOE = nOE;
    }

    public Integer getNOEE() {
        return NOEE;
    }

    public void setNOEE(final Integer nOEE) {
        NOEE = nOEE;
    }

    public Integer getEF() {
        return EF;
    }

    public void setEF(final Integer eF) {
        EF = eF;
    }

    public Double getDMO() {
        return DMO;
    }

    public void setDMO(final Double dMO) {
        DMO = dMO;
    }

    public Double getPO() {
        return PO;
    }

    public void setPO(final Double pO) {
        PO = pO;
    }

    public Double getOCO() {
        return OCO;
    }

    public void setOCO(final Double oCO) {
        OCO = oCO;
    }

    public Double getOOW() {
        return OOW;
    }

    public void setOOW(final Double oOW) {
        OOW = oOW;
    }

    public Integer getOIF() {
        return OIF;
    }

    public void setOIF(final Integer oIF) {
        OIF = oIF;
    }

    public Integer getNOE1() {
        return NOE1;
    }

    public void setNOE1(final Integer nOE1) {
        NOE1 = nOE1;
    }

    public Integer getNOE2() {
        return NOE2;
    }

    public void setNOE2(final Integer nOE2) {
        NOE2 = nOE2;
    }

    public Integer getNOE3() {
        return NOE3;
    }

    public void setNOE3(final Integer nOE3) {
        NOE3 = nOE3;
    }

    public Integer getNOE4() {
        return NOE4;
    }

    public void setNOE4(final Integer nOE4) {
        NOE4 = nOE4;
    }

    public Integer getNOE5() {
        return NOE5;
    }

    public void setNOE5(final Integer nOE5) {
        NOE5 = nOE5;
    }
}
