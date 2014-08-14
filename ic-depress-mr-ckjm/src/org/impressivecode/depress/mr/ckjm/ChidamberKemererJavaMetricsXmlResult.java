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


package org.impressivecode.depress.mr.ckjm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="class" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="wmc" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="dit" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="noc" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="cbo" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="rfc" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="lcom" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="ca" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="ce" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="npm" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="lcom3" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="loc" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="dam" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="moa" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="mfa" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="cam" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="ic" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="cbm" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="amc" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="cc" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clazz"
})
@XmlRootElement(name = "ckjm")
public class ChidamberKemererJavaMetricsXmlResult {

    @XmlElement(name = "class", required = true)
    protected List<ChidamberKemererJavaMetricsXmlResult.Class> clazz;

    /**
     * Gets the value of the clazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ckjm.Class }
     * 
     * 
     */
    public List<ChidamberKemererJavaMetricsXmlResult.Class> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<ChidamberKemererJavaMetricsXmlResult.Class>();
        }
        return this.clazz;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="wmc" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="dit" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="noc" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="cbo" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="rfc" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="lcom" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="ca" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="ce" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="npm" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="lcom3" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="loc" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="dam" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="moa" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="mfa" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="cam" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="ic" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="cbm" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="amc" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="cc" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "wmc",
        "dit",
        "noc",
        "cbo",
        "rfc",
        "lcom",
        "ca",
        "ce",
        "npm",
        "lcom3",
        "loc",
        "dam",
        "moa",
        "mfa",
        "cam",
        "ic",
        "cbm",
        "amc",
        "cc"
    })
    public static class Class {

        @XmlElement(required = true)
        protected String name;
        protected short wmc;
        protected byte dit;
        protected byte noc;
        protected short cbo;
        protected short rfc;
        protected short lcom;
        protected short ca;
        protected byte ce;
        protected short npm;
        protected float lcom3;
        protected int loc;
        protected float dam;
        protected short moa;
        protected float mfa;
        protected float cam;
        protected byte ic;
        protected byte cbm;
        protected float amc;
        @XmlElement(required = true)
        protected Object cc;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the wmc property.
         * 
         */
        public short getWmc() {
            return wmc;
        }

        /**
         * Sets the value of the wmc property.
         * 
         */
        public void setWmc(short value) {
            this.wmc = value;
        }

        /**
         * Gets the value of the dit property.
         * 
         */
        public byte getDit() {
            return dit;
        }

        /**
         * Sets the value of the dit property.
         * 
         */
        public void setDit(byte value) {
            this.dit = value;
        }

        /**
         * Gets the value of the noc property.
         * 
         */
        public byte getNoc() {
            return noc;
        }

        /**
         * Sets the value of the noc property.
         * 
         */
        public void setNoc(byte value) {
            this.noc = value;
        }

        /**
         * Gets the value of the cbo property.
         * 
         */
        public short getCbo() {
            return cbo;
        }

        /**
         * Sets the value of the cbo property.
         * 
         */
        public void setCbo(short value) {
            this.cbo = value;
        }

        /**
         * Gets the value of the rfc property.
         * 
         */
        public short getRfc() {
            return rfc;
        }

        /**
         * Sets the value of the rfc property.
         * 
         */
        public void setRfc(short value) {
            this.rfc = value;
        }

        /**
         * Gets the value of the lcom property.
         * 
         */
        public short getLcom() {
            return lcom;
        }

        /**
         * Sets the value of the lcom property.
         * 
         */
        public void setLcom(short value) {
            this.lcom = value;
        }

        /**
         * Gets the value of the ca property.
         * 
         */
        public short getCa() {
            return ca;
        }

        /**
         * Sets the value of the ca property.
         * 
         */
        public void setCa(short value) {
            this.ca = value;
        }

        /**
         * Gets the value of the ce property.
         * 
         */
        public byte getCe() {
            return ce;
        }

        /**
         * Sets the value of the ce property.
         * 
         */
        public void setCe(byte value) {
            this.ce = value;
        }

        /**
         * Gets the value of the npm property.
         * 
         */
        public short getNpm() {
            return npm;
        }

        /**
         * Sets the value of the npm property.
         * 
         */
        public void setNpm(short value) {
            this.npm = value;
        }

        /**
         * Gets the value of the lcom3 property.
         * 
         */
        public float getLcom3() {
            return lcom3;
        }

        /**
         * Sets the value of the lcom3 property.
         * 
         */
        public void setLcom3(float value) {
            this.lcom3 = value;
        }

        /**
         * Gets the value of the loc property.
         * 
         */
        public int getLoc() {
            return loc;
        }

        /**
         * Sets the value of the loc property.
         * 
         */
        public void setLoc(int value) {
            this.loc = value;
        }

        /**
         * Gets the value of the dam property.
         * 
         */
        public float getDam() {
            return dam;
        }

        /**
         * Sets the value of the dam property.
         * 
         */
        public void setDam(float value) {
            this.dam = value;
        }

        /**
         * Gets the value of the moa property.
         * 
         */
        public short getMoa() {
            return moa;
        }

        /**
         * Sets the value of the moa property.
         * 
         */
        public void setMoa(short value) {
            this.moa = value;
        }

        /**
         * Gets the value of the mfa property.
         * 
         */
        public float getMfa() {
            return mfa;
        }

        /**
         * Sets the value of the mfa property.
         * 
         */
        public void setMfa(float value) {
            this.mfa = value;
        }

        /**
         * Gets the value of the cam property.
         * 
         */
        public float getCam() {
            return cam;
        }

        /**
         * Sets the value of the cam property.
         * 
         */
        public void setCam(float value) {
            this.cam = value;
        }

        /**
         * Gets the value of the ic property.
         * 
         */
        public byte getIc() {
            return ic;
        }

        /**
         * Sets the value of the ic property.
         * 
         */
        public void setIc(byte value) {
            this.ic = value;
        }

        /**
         * Gets the value of the cbm property.
         * 
         */
        public byte getCbm() {
            return cbm;
        }

        /**
         * Sets the value of the cbm property.
         * 
         */
        public void setCbm(byte value) {
            this.cbm = value;
        }

        /**
         * Gets the value of the amc property.
         * 
         */
        public float getAmc() {
            return amc;
        }

        /**
         * Sets the value of the amc property.
         * 
         */
        public void setAmc(float value) {
            this.amc = value;
        }

        /**
         * Gets the value of the cc property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getCc() {
            return cc;
        }

        /**
         * Sets the value of the cc property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setCc(Object value) {
            this.cc = value;
        }

    }

}
