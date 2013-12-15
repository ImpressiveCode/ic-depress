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

package org.impressivecode.depress.mr.judy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="summary">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *                   &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="testsCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="evaluatedClassesCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="mutatedClassesCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                   &lt;element name="allKilledMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="allMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="testsRuns" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="testsDuration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="operators">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="operator" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="killedMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                             &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tests">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="test" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                             &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="classes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="class" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;choice>
 *                               &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;sequence>
 *                                 &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                 &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                                 &lt;element name="mutantsKilledCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                                 &lt;element name="notKilledMutants" minOccurs="0">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;sequence>
 *                                           &lt;element name="mutant" maxOccurs="unbounded">
 *                                             &lt;complexType>
 *                                               &lt;complexContent>
 *                                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                   &lt;sequence>
 *                                                     &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                     &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                                                     &lt;choice minOccurs="0">
 *                                                       &lt;element name="line" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                                                       &lt;sequence>
 *                                                         &lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                         &lt;sequence minOccurs="0">
 *                                                           &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                           &lt;element name="methodDescriptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                         &lt;/sequence>
 *                                                       &lt;/sequence>
 *                                                     &lt;/choice>
 *                                                   &lt;/sequence>
 *                                                 &lt;/restriction>
 *                                               &lt;/complexContent>
 *                                             &lt;/complexType>
 *                                           &lt;/element>
 *                                         &lt;/sequence>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                               &lt;/sequence>
 *                             &lt;/choice>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
@XmlType(name = "", propOrder = { "summary", "operators", "tests", "classes" })
@XmlRootElement(name = "result")
public class JudyXmlResult {

    @XmlElement(required = true)
    protected JudyXmlResult.Summary summary;
    @XmlElement(required = true)
    protected JudyXmlResult.Operators operators;
    @XmlElement(required = true)
    protected JudyXmlResult.Tests tests;
    @XmlElement(required = true)
    protected JudyXmlResult.Classes classes;

    /**
     * Gets the value of the summary property.
     * 
     * @return possible object is {@link JudyXmlResult.Summary }
     * 
     */
    public JudyXmlResult.Summary getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *            allowed object is {@link JudyXmlResult.Summary }
     * 
     */
    public void setSummary(final JudyXmlResult.Summary value) {
        this.summary = value;
    }

    /**
     * Gets the value of the operators property.
     * 
     * @return possible object is {@link JudyXmlResult.Operators }
     * 
     */
    public JudyXmlResult.Operators getOperators() {
        return operators;
    }

    /**
     * Sets the value of the operators property.
     * 
     * @param value
     *            allowed object is {@link JudyXmlResult.Operators }
     * 
     */
    public void setOperators(final JudyXmlResult.Operators value) {
        this.operators = value;
    }

    /**
     * Gets the value of the tests property.
     * 
     * @return possible object is {@link JudyXmlResult.Tests }
     * 
     */
    public JudyXmlResult.Tests getTests() {
        return tests;
    }

    /**
     * Sets the value of the tests property.
     * 
     * @param value
     *            allowed object is {@link JudyXmlResult.Tests }
     * 
     */
    public void setTests(final JudyXmlResult.Tests value) {
        this.tests = value;
    }

    /**
     * Gets the value of the classes property.
     * 
     * @return possible object is {@link JudyXmlResult.Classes }
     * 
     */
    public JudyXmlResult.Classes getClasses() {
        return classes;
    }

    /**
     * Sets the value of the classes property.
     * 
     * @param value
     *            allowed object is {@link JudyXmlResult.Classes }
     * 
     */
    public void setClasses(final JudyXmlResult.Classes value) {
        this.classes = value;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
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
     *                   &lt;choice>
     *                     &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                     &lt;sequence>
     *                       &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                       &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                       &lt;element name="mutantsKilledCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                       &lt;element name="notKilledMutants" minOccurs="0">
     *                         &lt;complexType>
     *                           &lt;complexContent>
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                               &lt;sequence>
     *                                 &lt;element name="mutant" maxOccurs="unbounded">
     *                                   &lt;complexType>
     *                                     &lt;complexContent>
     *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                         &lt;sequence>
     *                                           &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                           &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                                           &lt;choice minOccurs="0">
     *                                             &lt;element name="line" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                                             &lt;sequence>
     *                                               &lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                               &lt;sequence minOccurs="0">
     *                                                 &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                                 &lt;element name="methodDescriptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                               &lt;/sequence>
     *                                             &lt;/sequence>
     *                                           &lt;/choice>
     *                                         &lt;/sequence>
     *                                       &lt;/restriction>
     *                                     &lt;/complexContent>
     *                                   &lt;/complexType>
     *                                 &lt;/element>
     *                               &lt;/sequence>
     *                             &lt;/restriction>
     *                           &lt;/complexContent>
     *                         &lt;/complexType>
     *                       &lt;/element>
     *                     &lt;/sequence>
     *                   &lt;/choice>
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
    @XmlType(name = "", propOrder = { "clazz" })
    public static class Classes {

        @XmlElement(name = "class", required = true)
        protected List<JudyXmlResult.Classes.Class> clazz;

        /**
         * Gets the value of the clazz property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the clazz property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getClazz().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JudyXmlResult.Classes.Class }
         * 
         * 
         */
        public List<JudyXmlResult.Classes.Class> getClazz() {
            if (clazz == null) {
                clazz = new ArrayList<JudyXmlResult.Classes.Class>();
            }
            return this.clazz;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         * 
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;choice>
         *           &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *           &lt;sequence>
         *             &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *             &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *             &lt;element name="mutantsKilledCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *             &lt;element name="notKilledMutants" minOccurs="0">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;sequence>
         *                       &lt;element name="mutant" maxOccurs="unbounded">
         *                         &lt;complexType>
         *                           &lt;complexContent>
         *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                               &lt;sequence>
         *                                 &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                 &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *                                 &lt;choice minOccurs="0">
         *                                   &lt;element name="line" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *                                   &lt;sequence>
         *                                     &lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                     &lt;sequence minOccurs="0">
         *                                       &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                       &lt;element name="methodDescriptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                     &lt;/sequence>
         *                                   &lt;/sequence>
         *                                 &lt;/choice>
         *                               &lt;/sequence>
         *                             &lt;/restriction>
         *                           &lt;/complexContent>
         *                         &lt;/complexType>
         *                       &lt;/element>
         *                     &lt;/sequence>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *           &lt;/sequence>
         *         &lt;/choice>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "name", "type", "score", "mutantsCount", "mutantsKilledCount",
                "notKilledMutants" })
        public static class Class {

            @XmlElement(required = true)
            protected String name;
            protected String type;
            protected BigDecimal score;
            protected Short mutantsCount;
            protected Short mutantsKilledCount;
            protected JudyXmlResult.Classes.Class.NotKilledMutants notKilledMutants;

            /**
             * Gets the value of the name property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setName(final String value) {
                this.name = value;
            }

            /**
             * Gets the value of the type property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setType(final String value) {
                this.type = value;
            }

            /**
             * Gets the value of the score property.
             * 
             * @return possible object is {@link BigDecimal }
             * 
             */
            public BigDecimal getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *            allowed object is {@link BigDecimal }
             * 
             */
            public void setScore(final BigDecimal value) {
                this.score = value;
            }

            /**
             * Gets the value of the mutantsCount property.
             * 
             * @return possible object is {@link Short }
             * 
             */
            public Short getMutantsCount() {
                return mutantsCount;
            }

            /**
             * Sets the value of the mutantsCount property.
             * 
             * @param value
             *            allowed object is {@link Short }
             * 
             */
            public void setMutantsCount(final Short value) {
                this.mutantsCount = value;
            }

            /**
             * Gets the value of the mutantsKilledCount property.
             * 
             * @return possible object is {@link Short }
             * 
             */
            public Short getMutantsKilledCount() {
                return mutantsKilledCount;
            }

            /**
             * Sets the value of the mutantsKilledCount property.
             * 
             * @param value
             *            allowed object is {@link Short }
             * 
             */
            public void setMutantsKilledCount(final Short value) {
                this.mutantsKilledCount = value;
            }

            /**
             * Gets the value of the notKilledMutants property.
             * 
             * @return possible object is
             *         {@link JudyXmlResult.Classes.Class.NotKilledMutants }
             * 
             */
            public JudyXmlResult.Classes.Class.NotKilledMutants getNotKilledMutants() {
                return notKilledMutants;
            }

            /**
             * Sets the value of the notKilledMutants property.
             * 
             * @param value
             *            allowed object is
             *            {@link JudyXmlResult.Classes.Class.NotKilledMutants }
             * 
             */
            public void setNotKilledMutants(final JudyXmlResult.Classes.Class.NotKilledMutants value) {
                this.notKilledMutants = value;
            }

            /**
             * <p>
             * Java class for anonymous complex type.
             * 
             * <p>
             * The following schema fragment specifies the expected content
             * contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="mutant" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}short"/>
             *                   &lt;choice minOccurs="0">
             *                     &lt;element name="line" type="{http://www.w3.org/2001/XMLSchema}short"/>
             *                     &lt;sequence>
             *                       &lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                       &lt;sequence minOccurs="0">
             *                         &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                         &lt;element name="methodDescriptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                       &lt;/sequence>
             *                     &lt;/sequence>
             *                   &lt;/choice>
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
            @XmlType(name = "", propOrder = { "mutant" })
            public static class NotKilledMutants {

                @XmlElement(required = true)
                protected List<JudyXmlResult.Classes.Class.NotKilledMutants.Mutant> mutant;

                /**
                 * Gets the value of the mutant property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object. This is
                 * why there is not a <CODE>set</CODE> method for the mutant
                 * property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * 
                 * <pre>
                 * getMutant().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link JudyXmlResult.Classes.Class.NotKilledMutants.Mutant }
                 * 
                 * 
                 */
                public List<JudyXmlResult.Classes.Class.NotKilledMutants.Mutant> getMutant() {
                    if (mutant == null) {
                        mutant = new ArrayList<JudyXmlResult.Classes.Class.NotKilledMutants.Mutant>();
                    }
                    return this.mutant;
                }

                /**
                 * <p>
                 * Java class for anonymous complex type.
                 * 
                 * <p>
                 * The following schema fragment specifies the expected content
                 * contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="point" type="{http://www.w3.org/2001/XMLSchema}short"/>
                 *         &lt;choice minOccurs="0">
                 *           &lt;element name="line" type="{http://www.w3.org/2001/XMLSchema}short"/>
                 *           &lt;sequence>
                 *             &lt;element name="details" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *             &lt;sequence minOccurs="0">
                 *               &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *               &lt;element name="methodDescriptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *             &lt;/sequence>
                 *           &lt;/sequence>
                 *         &lt;/choice>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "operator", "point", "line", "details", "methodName",
                        "methodDescriptor" })
                public static class Mutant {

                    @XmlElement(required = true)
                    protected String operator;
                    protected short point;
                    protected Short line;
                    protected String details;
                    protected String methodName;
                    protected String methodDescriptor;

                    /**
                     * Gets the value of the operator property.
                     * 
                     * @return possible object is {@link String }
                     * 
                     */
                    public String getOperator() {
                        return operator;
                    }

                    /**
                     * Sets the value of the operator property.
                     * 
                     * @param value
                     *            allowed object is {@link String }
                     * 
                     */
                    public void setOperator(final String value) {
                        this.operator = value;
                    }

                    /**
                     * Gets the value of the point property.
                     * 
                     */
                    public short getPoint() {
                        return point;
                    }

                    /**
                     * Sets the value of the point property.
                     * 
                     */
                    public void setPoint(final short value) {
                        this.point = value;
                    }

                    /**
                     * Gets the value of the line property.
                     * 
                     * @return possible object is {@link Short }
                     * 
                     */
                    public Short getLine() {
                        return line;
                    }

                    /**
                     * Sets the value of the line property.
                     * 
                     * @param value
                     *            allowed object is {@link Short }
                     * 
                     */
                    public void setLine(final Short value) {
                        this.line = value;
                    }

                    /**
                     * Gets the value of the details property.
                     * 
                     * @return possible object is {@link String }
                     * 
                     */
                    public String getDetails() {
                        return details;
                    }

                    /**
                     * Sets the value of the details property.
                     * 
                     * @param value
                     *            allowed object is {@link String }
                     * 
                     */
                    public void setDetails(final String value) {
                        this.details = value;
                    }

                    /**
                     * Gets the value of the methodName property.
                     * 
                     * @return possible object is {@link String }
                     * 
                     */
                    public String getMethodName() {
                        return methodName;
                    }

                    /**
                     * Sets the value of the methodName property.
                     * 
                     * @param value
                     *            allowed object is {@link String }
                     * 
                     */
                    public void setMethodName(final String value) {
                        this.methodName = value;
                    }

                    /**
                     * Gets the value of the methodDescriptor property.
                     * 
                     * @return possible object is {@link String }
                     * 
                     */
                    public String getMethodDescriptor() {
                        return methodDescriptor;
                    }

                    /**
                     * Sets the value of the methodDescriptor property.
                     * 
                     * @param value
                     *            allowed object is {@link String }
                     * 
                     */
                    public void setMethodDescriptor(final String value) {
                        this.methodDescriptor = value;
                    }

                }

            }

        }

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="operator" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="killedMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                   &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
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
    @XmlType(name = "", propOrder = { "operator" })
    public static class Operators {

        @XmlElement(required = true)
        protected List<JudyXmlResult.Operators.Operator> operator;

        /**
         * Gets the value of the operator property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the operator property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getOperator().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JudyXmlResult.Operators.Operator }
         * 
         * 
         */
        public List<JudyXmlResult.Operators.Operator> getOperator() {
            if (operator == null) {
                operator = new ArrayList<JudyXmlResult.Operators.Operator>();
            }
            return this.operator;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         * 
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="killedMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *         &lt;element name="mutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "name", "description", "killedMutantsCount", "mutantsCount" })
        public static class Operator {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String description;
            protected short killedMutantsCount;
            protected short mutantsCount;

            /**
             * Gets the value of the name property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setName(final String value) {
                this.name = value;
            }

            /**
             * Gets the value of the description property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setDescription(final String value) {
                this.description = value;
            }

            /**
             * Gets the value of the killedMutantsCount property.
             * 
             */
            public short getKilledMutantsCount() {
                return killedMutantsCount;
            }

            /**
             * Sets the value of the killedMutantsCount property.
             * 
             */
            public void setKilledMutantsCount(final short value) {
                this.killedMutantsCount = value;
            }

            /**
             * Gets the value of the mutantsCount property.
             * 
             */
            public short getMutantsCount() {
                return mutantsCount;
            }

            /**
             * Sets the value of the mutantsCount property.
             * 
             */
            public void setMutantsCount(final short value) {
                this.mutantsCount = value;
            }

        }

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}time"/>
     *         &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="testsCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="evaluatedClassesCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="mutatedClassesCount" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *         &lt;element name="allKilledMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="allMutantsCount" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="testsRuns" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="testsDuration" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "date", "time", "duration", "testsCount", "evaluatedClassesCount",
            "mutatedClassesCount", "score", "allKilledMutantsCount", "allMutantsCount", "testsRuns", "testsDuration" })
    public static class Summary {

        @XmlElement(required = true)
        protected String date;
        @XmlElement(required = true)
        @XmlSchemaType(name = "time")
        protected XMLGregorianCalendar time;
        @XmlElement(required = true)
        protected String duration;
        protected byte testsCount;
        protected byte evaluatedClassesCount;
        protected byte mutatedClassesCount;
        @XmlElement(required = true)
        protected BigDecimal score;
        protected short allKilledMutantsCount;
        protected short allMutantsCount;
        protected int testsRuns;
        @XmlElement(required = true)
        protected String testsDuration;

        /**
         * Gets the value of the date property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getDate() {
            return date;
        }

        /**
         * Sets the value of the date property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setDate(final String value) {
            this.date = value;
        }

        /**
         * Gets the value of the time property.
         * 
         * @return possible object is {@link XMLGregorianCalendar }
         * 
         */
        public XMLGregorianCalendar getTime() {
            return time;
        }

        /**
         * Sets the value of the time property.
         * 
         * @param value
         *            allowed object is {@link XMLGregorianCalendar }
         * 
         */
        public void setTime(final XMLGregorianCalendar value) {
            this.time = value;
        }

        /**
         * Gets the value of the duration property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getDuration() {
            return duration;
        }

        /**
         * Sets the value of the duration property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setDuration(final String value) {
            this.duration = value;
        }

        /**
         * Gets the value of the testsCount property.
         * 
         */
        public byte getTestsCount() {
            return testsCount;
        }

        /**
         * Sets the value of the testsCount property.
         * 
         */
        public void setTestsCount(final byte value) {
            this.testsCount = value;
        }

        /**
         * Gets the value of the evaluatedClassesCount property.
         * 
         */
        public byte getEvaluatedClassesCount() {
            return evaluatedClassesCount;
        }

        /**
         * Sets the value of the evaluatedClassesCount property.
         * 
         */
        public void setEvaluatedClassesCount(final byte value) {
            this.evaluatedClassesCount = value;
        }

        /**
         * Gets the value of the mutatedClassesCount property.
         * 
         */
        public byte getMutatedClassesCount() {
            return mutatedClassesCount;
        }

        /**
         * Sets the value of the mutatedClassesCount property.
         * 
         */
        public void setMutatedClassesCount(final byte value) {
            this.mutatedClassesCount = value;
        }

        /**
         * Gets the value of the score property.
         * 
         * @return possible object is {@link BigDecimal }
         * 
         */
        public BigDecimal getScore() {
            return score;
        }

        /**
         * Sets the value of the score property.
         * 
         * @param value
         *            allowed object is {@link BigDecimal }
         * 
         */
        public void setScore(final BigDecimal value) {
            this.score = value;
        }

        /**
         * Gets the value of the allKilledMutantsCount property.
         * 
         */
        public short getAllKilledMutantsCount() {
            return allKilledMutantsCount;
        }

        /**
         * Sets the value of the allKilledMutantsCount property.
         * 
         */
        public void setAllKilledMutantsCount(final short value) {
            this.allKilledMutantsCount = value;
        }

        /**
         * Gets the value of the allMutantsCount property.
         * 
         */
        public short getAllMutantsCount() {
            return allMutantsCount;
        }

        /**
         * Sets the value of the allMutantsCount property.
         * 
         */
        public void setAllMutantsCount(final short value) {
            this.allMutantsCount = value;
        }

        /**
         * Gets the value of the testsRuns property.
         * 
         */
        public int getTestsRuns() {
            return testsRuns;
        }

        /**
         * Sets the value of the testsRuns property.
         * 
         */
        public void setTestsRuns(final int value) {
            this.testsRuns = value;
        }

        /**
         * Gets the value of the testsDuration property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getTestsDuration() {
            return testsDuration;
        }

        /**
         * Sets the value of the testsDuration property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setTestsDuration(final String value) {
            this.testsDuration = value;
        }

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="test" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                   &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    @XmlType(name = "", propOrder = { "test" })
    public static class Tests {

        @XmlElement(required = true)
        protected List<JudyXmlResult.Tests.Test> test;

        /**
         * Gets the value of the test property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the test property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getTest().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JudyXmlResult.Tests.Test }
         * 
         * 
         */
        public List<JudyXmlResult.Tests.Test> getTest() {
            if (test == null) {
                test = new ArrayList<JudyXmlResult.Tests.Test>();
            }
            return this.test;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         * 
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "name", "duration", "result" })
        public static class Test {

            @XmlElement(required = true)
            protected String name;
            protected short duration;
            @XmlElement(required = true)
            protected String result;

            /**
             * Gets the value of the name property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setName(final String value) {
                this.name = value;
            }

            /**
             * Gets the value of the duration property.
             * 
             */
            public short getDuration() {
                return duration;
            }

            /**
             * Sets the value of the duration property.
             * 
             */
            public void setDuration(final short value) {
                this.duration = value;
            }

            /**
             * Gets the value of the result property.
             * 
             * @return possible object is {@link String }
             * 
             */
            public String getResult() {
                return result;
            }

            /**
             * Sets the value of the result property.
             * 
             * @param value
             *            allowed object is {@link String }
             * 
             */
            public void setResult(final String value) {
                this.result = value;
            }

        }

    }

}
