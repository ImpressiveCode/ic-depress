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
package org.impressivecode.depress.its.bugzilla;

import static org.fest.assertions.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.impressivecode.depress.its.ITSDataType;
import org.junit.Test;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineParserTest {

	public Object[] getBugsSample() throws Exception {
		FileInputStream fin = new FileInputStream(BugzillaOnlineParserTest.class.getResource("mozillaOnline.dat").getPath());
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object[] bugs = (Object[]) ois.readObject();
		ois.close();
		return bugs;
	}

	@Test
	public void shouldParseEntry() throws Exception {
		// given
		Object bug = getBugsSample()[0];
		BugzillaOnlineParser parser = new BugzillaOnlineParser();

		// when
		ITSDataType its = parser.parse(bug);

		// then
		assertThat(its.getIssueId()).isEqualTo("820167");
	}
}
