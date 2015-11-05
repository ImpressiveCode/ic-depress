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
package org.impressivecode.depress.scm.endevor;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Scanner;

import org.impressivecode.depress.scm.endevor.constants.EndevorParsingPhase;
import org.impressivecode.depress.scm.endevor.helpers.StringHelpers;
import org.impressivecode.depress.scm.endevor.models.EndevorLogEntryBase;
import org.impressivecode.depress.scm.endevor.models.EndevorLogNoInsertsDeletesActionEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
public class EndevorLogParserTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testParseDateTimeWithCorrectValues() throws Exception {
		String methodName = "parseDateTime";
		EndevorLogEntryBase testData = new EndevorLogNoInsertsDeletesActionEntry(null, null);
		testData.setDate("12JAN12");
		testData.setTime("13:13");

		Method method = EndevorLogEntryBase.class.getDeclaredMethod(methodName);
		method.setAccessible(true);
		method.invoke(testData);

		// 2012.01.12 13:13
		long expectedResultLong = Date.UTC(112, 0, 12, 13, 13, 0);
		Date expectedResultDate = new Date(expectedResultLong);

		Assert.assertEquals(expectedResultDate, testData.getDateParsed());
	}

	@Test(expected = EndevorLogParserException.class)
	public void testParseDateTimeWithIncorrectValues() throws Throwable {
		String methodName = "parseDateTime";
		EndevorLogEntryBase testData = new EndevorLogNoInsertsDeletesActionEntry(null, null);
		testData.setDate("12abc14");
		testData.setTime("13:43");

		Method method = EndevorLogEntryBase.class.getDeclaredMethod(methodName);
		method.setAccessible(true);
		try {
			method.invoke(testData);
		} catch (InvocationTargetException e) {
			throw new EndevorLogParserException("");
		}
	}

	@Test()
	public void testParseMonthWithCorrectValues() throws Throwable {
		String methodName = "parseMonth";
		String input = "MAY";

		Method method = EndevorLogEntryBase.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);

		int result = (int) method.invoke(new EndevorLogNoInsertsDeletesActionEntry(null, null), input);

		Assert.assertEquals(4, result);
	}

	@Test(expected = EndevorLogParserException.class)
	public void testParseMonthWithIncorrectValues() throws Throwable {
		String methodName = "parseMonth";
		String input = "abc";

		Method method = EndevorLogEntryBase.class.getDeclaredMethod(methodName,String.class);
		method.setAccessible(true);

		try {
			method.invoke(new EndevorLogNoInsertsDeletesActionEntry(null, null), input);
		} catch (InvocationTargetException e) {
			throw new EndevorLogParserException("");
		}
	}

	@Test
	public void testVerifyLineWithLogStart() throws Exception {
		String methodName = "verifyLine";
		String lineToVerify = "this is test to check if SOURCE LEVEL INFORMATION is in this line";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName,String.class);
		method.setAccessible(true);
		EndevorParsingPhase resultPhase = (EndevorParsingPhase) method.invoke(new EndevorLogParser(), lineToVerify);

		boolean isResultPhaseEqual = EndevorParsingPhase.SOURCE_INFO == resultPhase;
		Assert.assertTrue(isResultPhaseEqual);
	}

	@Test
	public void testVerifyLineWithAnotherEndevorLogKeywords() throws Throwable {
		String methodName = "verifyLine";
		String lineToVerify = "this is test to check what with other resolution";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		EndevorParsingPhase resultPhase = (EndevorParsingPhase) method.invoke(new EndevorLogParser(), lineToVerify);

		boolean isResultPhaseEqual = EndevorParsingPhase.SEARCHING == resultPhase;
		Assert.assertTrue(isResultPhaseEqual);
	}

	@Test
	public void testIsLineASourceLevelInformationDataWithSpace()
			throws Exception {
		String methodName = "isLineASourceLevelInformationData";
		String lineToVerify = "   this is test to check if this line starts with triple space";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);

		Assert.assertTrue(result);
	}

	@Test
	public void testIsLineASourceLevelInformationDataWithNoSpace()
			throws Throwable {
		String methodName = "isLineASourceLevelInformationData";
		String lineToVerify = "this is test to check if this line starts with triple space";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);

		Assert.assertFalse(result);
	}

	@Test
	public void testIsLineUsable() throws Exception {
		String methodName = "isLineUsable";
		String lineToVerify = "*** ---- -- -  **test*** * * * **--**-*-";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);

		Assert.assertTrue(result);
	}

	@Test
	public void testIsLineNotUsable() throws Throwable {
		String methodName = "isLineUsable";
		String lineToVerify = "*** ---- -- -  ***** * * * **--**-*-";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);

		Assert.assertFalse(result);
	}

	@Test
	public void testSkipUselessLogLinesWithUsableContent() throws Exception {
		String methodName = "skipUselessLogLines";
		String currentLine = "****-- - - *  * - - \n *-*-* * *- test * - *- *--- \n *-*---- *";
		ByteArrayInputStream input = new ByteArrayInputStream(currentLine.getBytes());
		Scanner scanner = new Scanner(input);
		String wantedLine = " *-*-* * *- test * - *- *--- ";

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, Scanner.class);
		method.setAccessible(true);
		String resultLine = (String) method.invoke(new EndevorLogParser(), scanner);

		boolean isLineEqual = wantedLine.equals(resultLine);
		Assert.assertTrue(isLineEqual);
	}

	@Test
	public void testSkipUselessLogLinesWithNoUsableContent() throws Exception {
		String methodName = "skipUselessLogLines";
		String currentLine = "****-- - - *  * - - \n *-*-* * *-  * - *- *--- \n *-*---- *";
		ByteArrayInputStream input = new ByteArrayInputStream(currentLine.getBytes());
		Scanner scanner = new Scanner(input);

		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, Scanner.class);
		method.setAccessible(true);
		Object resultLine = method.invoke(new EndevorLogParser(), scanner);

		Assert.assertNull(resultLine);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testApplyingMaskWithFirstCorrectValues() throws EndevorLogParserException {
		String mask = "  ----    ----    --";
		String data = "  cos      tst     0";

		String[] expectedResult = new String[] { "cos ", " tst", " 0" };
		
		String[] achievedResult = StringHelpers.applyMask(data, mask);
		
		Assert.assertEquals(expectedResult, achievedResult);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testApplyingMaskWithSecondCorrectValues() throws EndevorLogParserException {
		String mask = "  ----    ----    --  ------------------";
		String data = "  sth1      tst     0  left assign      ";

		String[] expectedResult = new String[] { "sth1", "  ts", "  ", " left assign      " };
		
		String[] achievedResult = StringHelpers.applyMask(data, mask);
		
		Assert.assertEquals(expectedResult, achievedResult);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testApplyingMaskWithFirstInorrectValues() throws EndevorLogParserException {
		String mask = "  ----    ----    --  ------------------";
		String data = "different lengths of input strings";

		StringHelpers.applyMask(data, mask);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testApplyingMaskWithSecondInorrectValues() throws EndevorLogParserException {
		String mask = "a mask without dashes, but with same length";
		String data = "a mask without dashes, but with same length";

		StringHelpers.applyMask(data, mask);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testApplyingMaskWithNulltValues() throws EndevorLogParserException {
		String mask = null;
		String data = null;

		StringHelpers.applyMask(data, mask);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testParseRowNoInsertsAndDeletestCorrectValues() throws EndevorLogParserException{
		String mask = "   ---- ---- -------- ------- ----- -------- ------------ ----------------------------------------                                   ";
		String data = "   vvLT  sth  TstAuth 10FEB14 11:17    22522 123456789    Example comment data                                                       ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
		
		long dateExpectedLong = Date.UTC(114, 1, 10, 11, 17, 0);
		Date dateExpected = new Date(dateExpectedLong);
		
		Assert.assertEquals("vvLT", testEntry.getVvll());
		Assert.assertEquals("sth", testEntry.getSync());
		Assert.assertEquals("TstAuth", testEntry.getUser());
		Assert.assertEquals(dateExpected, testEntry.getDateParsed());
		Assert.assertEquals(22522, testEntry.getStmts());
		Assert.assertEquals("123456789", testEntry.getCcid());
		Assert.assertEquals("Example comment data", testEntry.getComment());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testParseRowNoInsertsAndDeletesWithSomeEmptyCellsFirst() throws EndevorLogParserException{
		String mask = "   ---- ---- -------- ------- ----- -------- ------------ ----------------------------------------                                   ";
		String data = "         sth          10FEB14 11:17          123456789    Example comment data                                                       ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
		
		long dateExpectedLong = Date.UTC(114, 1, 10, 11, 17, 0);
		Date dateExpected = new Date(dateExpectedLong);
		
		Assert.assertEquals("", testEntry.getVvll());
		Assert.assertEquals("sth", testEntry.getSync());
		Assert.assertEquals("", testEntry.getUser());
		Assert.assertEquals(dateExpected, testEntry.getDateParsed());
		Assert.assertEquals(0, testEntry.getStmts());
		Assert.assertEquals("123456789", testEntry.getCcid());
		Assert.assertEquals("Example comment data", testEntry.getComment());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testParseRowNoInsertsAndDeletesWithSomeEmptyCellsSecond() throws EndevorLogParserException{
		String mask = "   ---- ---- -------- ------- ----- -------- ------------ ----------------------------------------                                   ";
		String data = "   1234               10FEB14 11:17     1098    123456789                                                                            ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
		
		long dateExpectedLong = Date.UTC(114, 1, 10, 11, 17, 0);
		Date dateExpected = new Date(dateExpectedLong);
		
		Assert.assertEquals("1234", testEntry.getVvll());
		Assert.assertEquals("", testEntry.getSync());
		Assert.assertEquals("", testEntry.getUser());
		Assert.assertEquals(dateExpected, testEntry.getDateParsed());
		Assert.assertEquals(1098, testEntry.getStmts());
		Assert.assertEquals("123456789", testEntry.getCcid());
		Assert.assertEquals("", testEntry.getComment());
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseRowNoInsertsAndDeletesWithIncorrectDataFirst() throws EndevorLogParserException{
		String mask = "   ---- ---- -------- ------- ----- -------- ------------ -------------------------------------                                   ";
		String data = "   1234     asa          10FEB14 11:17     1098    123  456789                testttt                                                 ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseRowNoInsertsAndDeletesWithIncorrectDataSecond() throws EndevorLogParserException{
		String mask = null;
		String data = "   1234     asa          10FEB14 11:17     1098    123  456789                testttt                                             ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseRowNoInsertsAndDeletesWithIncorrectColumnCount() throws EndevorLogParserException{
		String mask = "   ---- ---- -------- ------- -----  ------------ ----------------------------------------                                   ";
		String data = "   1234               10FEB14 11:17     123456789                                                                            ";
		EndevorLogNoInsertsDeletesActionEntry testEntry = new EndevorLogNoInsertsDeletesActionEntry(data, mask);
		testEntry.parseRow();
	}
}