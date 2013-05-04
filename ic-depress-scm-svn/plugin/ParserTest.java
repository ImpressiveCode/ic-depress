import org.junit.Test;


public class ParserTest {

	@Test
	public void test() {
		Parser parser = new Parser();
		
		parser.loadXml( "/home/marcin/Marcin/Projects/PWR/SVNPlugin/IndependentCode/xml_example1.xml", "#", "Directory1.*" );
	}

}
