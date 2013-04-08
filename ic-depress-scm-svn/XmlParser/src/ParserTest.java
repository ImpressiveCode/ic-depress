import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Vector;

import org.junit.Test;

public class ParserTest {
	
	@Test
	public void test_class_name() {
		Parser parser = new Parser();
		
		final String expectedElement = "{CLASSNAME=change}";
		
		assertEquals(expectedElement, parser.setAttribute("change", Parser.MARKERS.CLASSNAME).toString());
	}
	
	@Test
	public void test_marker() {
		Parser parser = new Parser();
		
		final String expectedElement = "{MARKER=marker}";
		
		assertEquals(expectedElement, parser.setAttribute("marker", Parser.MARKERS.MARKER).toString());
	}
	
	@Test
	public void test_author() {
		Parser parser = new Parser();
		
		final String expectedElement = "{AUTHOR=author}";
		
		assertEquals(expectedElement, parser.setAttribute("author", Parser.MARKERS.AUTHOR).toString());
	}
	
	@Test
	public void test_action() {
		Parser parser = new Parser();
		
		final String expectedElement = "{ACTION=action}";
		
		assertEquals(expectedElement, parser.setAttribute("action", Parser.MARKERS.ACTION).toString());
	}
	
	@Test
	public void test_message() {
		Parser parser = new Parser();
		
		final String expectedElement = "{MESSAGE=message}";
		
		assertEquals(expectedElement, parser.setAttribute("message", Parser.MARKERS.MESSAGE).toString());
	}
	
	@Test
	public void test_path() {
		Parser parser = new Parser();
		
		final String expectedElement = "{PATH=path}";
		
		assertEquals(expectedElement, parser.setAttribute("path", Parser.MARKERS.PATH).toString());
	}
	
	@Test
	public void test_date() {
		Parser parser = new Parser();
		
		final String expectedElement = "{DATE=date}";
		
		assertEquals(expectedElement, parser.setAttribute("date", Parser.MARKERS.DATE).toString());
	}
	
	@Test
	public void test_revision_number() {
		Parser parser = new Parser();
		
		final String expectedElement = "{REVISION=revision}";
		
		assertEquals(expectedElement, parser.setAttribute("revision", Parser.MARKERS.REVISION).toString());
	}
	
	Parser createParser()
	{
        Parser parser = new Parser();

		parser.setAttribute("classname", Parser.MARKERS.CLASSNAME);
		parser.setAttribute("marker", Parser.MARKERS.MARKER);
		parser.setAttribute("author", Parser.MARKERS.AUTHOR);
		parser.setAttribute("action", Parser.MARKERS.ACTION);
		parser.setAttribute("message", Parser.MARKERS.MESSAGE);
		parser.setAttribute("path", Parser.MARKERS.PATH);
		parser.setAttribute("date", Parser.MARKERS.DATE);
		parser.setAttribute("revision", Parser.MARKERS.REVISION);
		
		return parser;
	}
	
	@Test
	public void test_all_set() {	
		final String expectedElement = "{CLASSNAME=classname, MARKER=marker, AUTHOR=author, ACTION=action, MESSAGE=message, PATH=path, DATE=date, REVISION=revision}";
		
		Parser parser = createParser();
		
		assertEquals(expectedElement, parser.elementToString());
	}
	
	@Test
	public void test_class_name_get() {
		Parser parser = createParser();
		
		assertEquals("classname", parser.getAttribute(Parser.MARKERS.CLASSNAME));
	}
	
	@Test
	public void test_author_get() {
		Parser parser = createParser();
			
		assertEquals("author", parser.getAttribute(Parser.MARKERS.AUTHOR));
	}
	
	@Test
	public void test_action_get() {
		Parser parser = createParser();
		
		assertEquals("action", parser.getAttribute(Parser.MARKERS.ACTION));
	}
	
	@Test
	public void test_message_get() {
		Parser parser = createParser();
			
		assertEquals("message", parser.getAttribute(Parser.MARKERS.MESSAGE));
	}
	
	@Test
	public void test_path_get() {
		Parser parser = createParser();
		
		assertEquals("path", parser.getAttribute(Parser.MARKERS.PATH));
	}
	
	@Test
	public void test_date_get() {
		Parser parser = createParser();
		
		assertEquals("date", parser.getAttribute(Parser.MARKERS.DATE));
	}
	
	@Test
	public void test_revision_get() {
		Parser parser = createParser();
		
		assertEquals("revision", parser.getAttribute(Parser.MARKERS.REVISION));
	}
	
	@Test
	public void test_marker_get() {
		Parser parser = createParser();
		
		assertEquals("marker", parser.getAttribute(Parser.MARKERS.MARKER));
	}
	
	@Test
	public void test_parse() {
		Parser parser = new Parser();
		
		Vector<LinkedHashMap<Parser.MARKERS, String>> current = parser.parse("/home/marcin/workspace/XmlParser/xmlFile.xml");
		
		String expected = new String("");
		expected = "[{REVISION=2, AUTHOR=marcin, DATE=2013-03-29T17:03:01.242468Z, MARKER=#, CLASSNAME=NewClass, PATH=/NewClass.java, ACTION=A}, " +
				    "{REVISION=1, AUTHOR=marcin, DATE=2013-03-12T21:06:37.547970Z, MARKER=#, CLASSNAME=README, PATH=/README.txt, ACTION=A}]";
		
		assertEquals(current.toString(), expected.toString());
	}
	
	@Test
	public void test_parse_1() {
		Parser parser = new Parser();
		
		Vector<LinkedHashMap<Parser.MARKERS, String>> current = parser.parse("/home/marcin/workspace/XmlParser/xmlFile1.xml");
		
		String expected = new String("");
		expected = "[{REVISION=3, AUTHOR=marcin, DATE=2013-03-30T16:21:32.695472Z, MESSAGE=Two another files #marker, MARKER=#, CLASSNAME=File1, PATH=/file1.txt, ACTION=A}, " +
				    "{REVISION=3, AUTHOR=marcin, DATE=2013-03-30T16:21:32.695472Z, MESSAGE=Two another files #marker, MARKER=#, CLASSNAME=File2, PATH=/file2.txt, ACTION=A}, " +
				    "{REVISION=2, AUTHOR=marcin, DATE=2013-03-29T17:03:01.242468Z, MESSAGE=#marker jest bardzo wazny, MARKER=#, CLASSNAME=NewClass, PATH=/NewClass.java, ACTION=A}, " +
				    "{REVISION=1, AUTHOR=marcin, DATE=2013-03-12T21:06:37.547970Z, MARKER=#, CLASSNAME=README, PATH=/README.txt, ACTION=A}]";
		
		assertEquals(current.toString(), expected.toString());
	}
	
	@Test
	public void test_get_class_name() {
		Parser parser = new Parser();
		
		String expected = new String("ClassName");
		String path = new String("/className.txt");
	
		assertEquals(parser.getClassNameFromPath(path), expected);
	}
}
