package org.impressivecode.depress.scm.endevor;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.junit.Assert;
import org.junit.Test;

public class EndevorLogParserTest {

	@Test
	public void testParseLogFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParsedCommits() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseMonthWithCorrectMonthName() throws Exception {
		String methodName = "parseMonth";
		String monthName = "JAN";
		String monthNumber = "01";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		String resultMonth = method.invoke(new EndevorLogParser(), monthName).toString();
		
		boolean result = monthNumber.equals(resultMonth);
		Assert.assertTrue(result);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseMonthWithIncorrectMonthName() throws Throwable {
		String methodName = "parseMonth";
		String monthName = "Jan";
		String exceptionMessage = "Could not parse month signature: " + monthName + ".";
				
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		try {
			method.invoke(new EndevorLogParser(), monthName).toString();
		}
		catch (InvocationTargetException e) {
			throw new EndevorLogParserException(exceptionMessage);
		}
	}
	
	@Test
	public void testParseTimeWithCorrectValues() throws Exception {
		String methodName = "parseTime";
		String hours = "13";
		String minutes = "45";
		String columnTime = hours.concat(":").concat(minutes);
		Date now = new Date();
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, Date.class);
		method.setAccessible(true);
		Date resultDate = (Date) method.invoke(new EndevorLogParser(), columnTime, now);
		
		String resultHours = new SimpleDateFormat("H").format(resultDate);
		String resultMinutes = new SimpleDateFormat("mm").format(resultDate);
		
		boolean isHoursEquals = hours.equals(resultHours);
		boolean isMinutesEquals = minutes.equals(resultMinutes);
		Assert.assertTrue(isHoursEquals);
		Assert.assertTrue(isMinutesEquals);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseTimeWithIncorrectValues() throws Throwable {
		String methodName = "parseTime";
		String hours = "25"; //test siê sypie, bo jak godzina bêdzie powy¿ej 24 albo poni¿ej 0, to nie rzuca wyj¹tkiem, a chyba powinien, prawda?
							//wydaje mi siê, ¿e trzeba zrobiæ jak¹œ walidacjê godziny
		String minutes = "45";
		String columnTime = hours.concat(":").concat(minutes);
		Date now = new Date();
		String exceptionMessage = "Could not parse time: " + columnTime + ".";
				
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, Date.class);
		method.setAccessible(true);
				
		try {
			method.invoke(new EndevorLogParser(), columnTime, now);
		}
		catch (InvocationTargetException e) {
			throw new EndevorLogParserException(exceptionMessage);
		}
	}
	
	@Test
	public void testParseDateWithCorrectValues() throws Exception {
		String methodName = "parseDate";
		String columnData = "05SEP13";
		Date data = new SimpleDateFormat ("yyyy-MM-dd").parse("2013-09-05");
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, Date.class);
		method.setAccessible(true);
		Date resultDate = (Date) method.invoke(new EndevorLogParser(), columnData, new Date()); //do wywalenia, wg mnie zbedny argument
		
		boolean isDateEqual = data.equals(resultDate);
		Assert.assertTrue(isDateEqual);
	}
	
	@Test(expected = EndevorLogParserException.class)
	public void testParseDateWithIncorrectValues() throws Throwable {
		String methodName = "parseDate";
		String columnData = "05SEPh13";
		String exceptionMessage = "Could not parse date value: " + columnData + ".";
				
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, Date.class);
		method.setAccessible(true);
				
		try {
			method.invoke(new EndevorLogParser(), columnData, new Date()); //do wywalenia, wg mnie zbedny argument
		}
		catch (InvocationTargetException e) {
			throw new EndevorLogParserException(exceptionMessage);
		}
	}
	
	@Test
	public void testVerifyLineWithLogStart() throws Exception {
		String methodName = "verifyLine";
		String lineToVerify = "this is test to check if SOURCE LEVEL INFORMATION is in this line";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
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
	public void testIsLineASourceLevelInformationDataWithSpace() throws Exception {
		String methodName = "isLineASourceLevelInformationData";
		String lineToVerify = "  this is test to check if this line starts with double space";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testIsLineASourceLevelInformationDataWithNoSpace() throws Throwable {
		String methodName = "isLineASourceLevelInformationData";
		String lineToVerify = "this is test to check if this line starts with double space";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testIsLineSensibleReally() throws Exception {
		String methodName = "isLineSensible";
		String lineToVerify = "*** ---- -- -  **test*** * * * **--**-*-";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testIsLineSensibleNotReally() throws Throwable {
		String methodName = "isLineSensible";
		String lineToVerify = "*** ---- -- -  ***** * * * **--**-*-";
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class);
		method.setAccessible(true);
		boolean result = (boolean) method.invoke(new EndevorLogParser(), lineToVerify);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testSkipUselessLogLinesWithSensible() throws Exception {
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
	
	// wywala siê, bo w metodzie skipUselessLogLines musi byæ sprawdzenie, czy istnieje nextline
	@Test
	public void testSkipUselessLogLinesWithNoSensible() throws Exception {
		String methodName = "skipUselessLogLines";
		String currentLine = "****-- - - *  * - - \n *-*-* * *-  * - *- *--- \n *-*---- *";
		ByteArrayInputStream input = new ByteArrayInputStream(currentLine.getBytes());
		Scanner scanner = new Scanner(input);
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, Scanner.class);
		method.setAccessible(true);
		Object resultLine = method.invoke(new EndevorLogParser(), scanner);
		
		Assert.assertNull(resultLine);
	}
	
	@Test
	public void testRemoveEmptyOrUselessEntriesFromColumnHeadersOrDataArrayWithoutEmpty() throws Exception {
		String methodName = "removeEmptyOrUselessEntriesFromColumnHeadersOrDataArray";
		String[] columnHeadersOrData = {"SYNC", "USER", "SYNC", "DATE", "SYNC", "TIME", "SYNC", "SYNC"};
		String[] wantedColumns = {"USER", "DATE", "TIME"};
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, new Class[]{String[].class});
		method.setAccessible(true);
		String[] resultColumns = (String[]) method.invoke(new EndevorLogParser(), new Object[]{columnHeadersOrData});
		
		boolean isColumnsEqual = Arrays.deepEquals(wantedColumns, resultColumns);
		Assert.assertTrue(isColumnsEqual);
	}
	
	@Test
	public void testRemoveEmptyOrUselessEntriesFromColumnHeadersOrDataArrayWithEmpty() throws Exception {
		String methodName = "removeEmptyOrUselessEntriesFromColumnHeadersOrDataArray";
		String[] columnHeadersOrData = {"SYNC", "SYNC", "SYNC", "SYNC", "SYNC", "SYNC", "SYNC", "SYNC"};
		String[] wantedColumns = {};
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, new Class[]{String[].class});
		method.setAccessible(true);
		String[] resultColumns = (String[]) method.invoke(new EndevorLogParser(), new Object[]{columnHeadersOrData});
		
		boolean isColumnsEqual = Arrays.deepEquals(wantedColumns, resultColumns);
		Assert.assertTrue(isColumnsEqual);
	}
	
	@Test
	public void testSssignSCMRecordFieldValueWithAuthor() throws Exception {
		String methodName = "assignSCMRecordFieldValue";
		String columnHeader = "USER";
		String columnData = "05SEP13";
		SCMDataType scmRecord = new SCMDataType();
		scmRecord.setCommitDate(new Date());
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, String.class, SCMDataType.class);
		method.setAccessible(true);
		method.invoke(new EndevorLogParser(), columnHeader, columnData, scmRecord);
		
		boolean isAuthorCorrect = columnData.equals(scmRecord.getAuthor());
		Assert.assertTrue(isAuthorCorrect);
	}
	
	@Test
	public void testAssignSCMRecordFieldValue() throws Exception {
		//test siê sypie, bo jak wywo³amy najpier metodê dla czasu, a potem dla daty, to leci null pointer
		//
		//wg mnie mo¿na by te¿ trochê walidacji zrobiæ dla innych danych i obs³ugê wyj¹tków
		String methodName = "assignSCMRecordFieldValue";
		String columnHeader1 = "USER";
		String columnData1 = "Test author";
		String columnHeader2 = "TIME";
		String columnData2 = "13:45";
		String columnHeader3 = "DATE";
		String columnData3 = "05SEP13";
		Date dateTimeResult = new SimpleDateFormat ("yyyy-MM-dd H:mm").parse("2013-09-05 13:45");
		String columnHeader4 = "CCID";
		String columnData4 = "156713";
		String columnHeader5 = "COMMENT";
		String columnData5 = "Test message";
		String columnHeader6 = "DELETES";
		String columnData6 = "5";
		String columnHeader7 = "INSERTS";
		String columnData7 = "10";
		SCMOperation result7 = SCMOperation.ADDED;
		SCMDataType scmRecord = new SCMDataType();
		
		Method method = EndevorLogParser.class.getDeclaredMethod(methodName, String.class, String.class, SCMDataType.class);
		method.setAccessible(true);
		method.invoke(new EndevorLogParser(), columnHeader1, columnData1, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader2, columnData2, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader3, columnData3, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader4, columnData4, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader5, columnData5, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader6, columnData6, scmRecord);
		method.invoke(new EndevorLogParser(), columnHeader7, columnData7, scmRecord);
		
		boolean isUserCorrect = columnData1.equals(scmRecord.getAuthor());
		boolean isDateTimeCorrect = dateTimeResult.equals(scmRecord.getCommitDate());
		boolean isCommitIdCorrect = columnData4.equals(scmRecord.getCommitID());
		boolean isCommentCorrect = columnData5.equals(scmRecord.getMessage());
		boolean isOperationCorrect = result7.equals(scmRecord.getOperation());
		Assert.assertTrue(isUserCorrect);
		Assert.assertTrue(isDateTimeCorrect);
		Assert.assertTrue(isCommitIdCorrect);
		Assert.assertTrue(isCommentCorrect);
		Assert.assertTrue(isOperationCorrect);
	}
	
	@Test
	public void testAssignSCMRecordFieldValueWithIncorrectData() throws Exception {
		//todo
	}
	
}
