import java.awt.image.PackedColorModel;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Parser {

	/**
	 * Actions: A - the item is added D - the item was deleted M - properties or
	 * textual contents on the were changed R - the item was replaced by a
	 * different one at the same location
	 */

	// Methods
	public void loadXml(String inPath, String inIssueMarker, String inPackage /*IReadProgressListener inProgress*/) {
		try
		{
			if( inPackage.endsWith( ".*" ) )
			{
				inPackage = inPackage.substring( 0, inPackage.length() - 2 );
			}
						
			String authorString = new String();
			String messageString = new String();
			String dateString = new String();
			String uidString = new String();
			
			File fXmlFile = new File(inPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// recommended
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("logentry");

			System.out.println("----------------------------");

			for (int logentry = 0; logentry < nList.getLength(); logentry++) 
			{
				Node nNode = nList.item(logentry);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					uidString = eElement.getAttribute("revision");

					authorString = eElement.getElementsByTagName("author").item(0).getTextContent();

					dateString = eElement.getElementsByTagName("date").item(0).getTextContent();

					messageString = eElement
							.getElementsByTagName("msg").item(0)
							.getTextContent();
					
					// if message has not appropriate markers then will not include that row
					if( isMarkerInMessage( messageString, inIssueMarker ) == false )
					{
						continue;
					}
					////////////////////////////////////////////////////////////////////////

					Node pathsNode = eElement.getElementsByTagName("paths")
							.item(0);
					Element ePathsElement = (Element) pathsNode;

					NodeList nPathsList = ePathsElement
							.getElementsByTagName("path");

					for (int path = 0; path < nPathsList.getLength(); path++) 
					{
						Node nPathNode = nPathsList.item(path);
						Element ePathElement = (Element) nPathNode;

						String pathString = ePathElement.getTextContent();

						// if package is not valid then will not include this row
						if( isPackageValid( pathString, inPackage ) == false )
						{
							continue;
						}
						
						if( ePathElement.getAttribute("kind").equals( "dir" ) ) // directory is not important so skip it
						{
							continue;
						}
						
						// Uzupełnienie SVNLogRow
						//SVNLogRow r = new SVNLogRow();
						//r.setMarker(new StringCell(inIssueMarker));
						//System.out.println("Marker: " + inIssueMarker);
						
						//r.setAction(new StringCell(ePathElement.getAttribute("action")));
						//System.out.println("Action: " + ePathElement.getAttribute("action"));
						
						//r.setPath(new StringCell(pathString));
						//System.out.println("Path: " + pathString);
						
						//r.setClassName(new StringCell(getClassNameFromPath(pathString)));
						//r.setUid(new StringCell(uidString));
						//r.setAuthor(new StringCell(authorString));
						//r.setMessage(new StringCell(messageString));
						//r.setDate(new StringCell(dateString));
						
						//inProgress.onReadProgress(logentry, r);
						
						System.out.println("Marker: " + inIssueMarker);
						System.out.println("Action: " + ePathElement.getAttribute("action"));
						System.out.println("Path: " + pathString);
						System.out.println("UID: " + uidString);
						System.out.println("Author: " + authorString);
						System.out.println("Package: " + inPackage);
						System.out.println("Message: " + messageString);
						System.out.println("Class: " + getClassNameFromPath(pathString));
						System.out.println("Date: " + dateString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final String getClassNameFromPath(String path) {
		int lastDot = path.lastIndexOf(".");
		
		if( lastDot == -1 )
		{
			lastDot = path.length() - 1;
		}
		
		int lastSlash = path.lastIndexOf("/") + 1;

		String smallPath = path.substring(lastSlash, lastDot);

		return (smallPath.substring(0, 1).toUpperCase() + smallPath.substring(
				1, smallPath.length()));
	}
	
	public final boolean isPackageValid( String path, String packageString )
	{
		path = path.replaceAll("/", ".");
		
		if( path.charAt( 0 ) == '.' )
		{
			path = path.substring( 1 );
		}
		
		if( packageString.equals( "*" ) )
		{
			return true;
		}
		
		return path.contains( packageString );
	}
	
	public final boolean isMarkerInMessage( String message, String marker )
	{
		return message.contains( marker );
	}
}
