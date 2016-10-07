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
package org.impressivecode.depress.mr.googleaudit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 * @author Katarzyna Debowa, Wroclaw University of Technology
 * @author Pawel Krzos, Wroclaw University of Technology
 */
public class GoogleAuditParserTest {

	
	List<GoogleAuditEntry> googleAuditEntriesList;
	@Before	
	public void init() throws ParserConfigurationException, SAXException, IOException{		
		GoogleAuditEntriesParser parser = mock(GoogleAuditEntriesParser.class);
		when(parser.parseEntries(getClass().getResource("AuditViolations.xml").getPath()))
				.thenCallRealMethod();
		googleAuditEntriesList = parser
				.parseEntries(getClass().getResource("AuditViolations.xml").getPath());
	}
	@Test
	public void testNumberOfViolationClasses() throws JAXBException,
			ParserConfigurationException, SAXException, IOException {
		assertEquals(googleAuditEntriesList.size(), 43);
	}

	@Test
	public void testViolationsForOneClass() throws JAXBException,
			ParserConfigurationException, SAXException, IOException {
		
		GoogleAuditEntry firstClass = googleAuditEntriesList.get(0);
		assertEquals(firstClass.getResourceName(), "AbstractMetricSource.java");
		assertTrue(firstClass.getLow() == 0);
		assertTrue(firstClass.getMedium() == 2);
		assertTrue(firstClass.getHigh() == 0);
	}

	@Test
	public void testHighViolations() throws JAXBException,
			ParserConfigurationException, SAXException, IOException {

		double numberOfHigh = 0;
		for (GoogleAuditEntry gae : googleAuditEntriesList) {

			numberOfHigh += gae.getHigh();
		}
		assertEquals((int) numberOfHigh, 0);
	}

	@Test
	public void testMediumViolations() throws JAXBException,
			ParserConfigurationException, SAXException, IOException {

		double numberOfMedium = 0;
		for (GoogleAuditEntry gae : googleAuditEntriesList) {

			numberOfMedium += gae.getMedium();
		}
		assertEquals((int) numberOfMedium, 98);
	}

	@Test
	public void testLowViolations() throws JAXBException,
			ParserConfigurationException, SAXException, IOException {

		double numberOfLow = 0;
		for (GoogleAuditEntry gae : googleAuditEntriesList) {

			numberOfLow += gae.getLow();
		}
		assertEquals((int) numberOfLow, 5);
	}
}
