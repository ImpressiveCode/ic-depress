import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

public class Parser {
	
	public enum MARKERS
	{
		CLASSNAME,
		MARKER,
		AUTHOR,
		ACTION,
		MESSAGE,
		PATH,
		DATE,
		REVISION
	}
	
	private Vector<LinkedHashMap<MARKERS, String>> vec = new Vector<LinkedHashMap<MARKERS,String>>();
	/**
	 * Actions:
     * A - the item is added
     * D - the item was deleted
     * M - properties or textual contents on the were changed
     * R - the item was replaced by a different one at the same location
     */
    private LinkedHashMap<MARKERS,String> element = new LinkedHashMap<MARKERS,String>();
    // which revision is already analyzed
    private String currentRevision = new String("");
    private Vector<String> pathsString = new Vector<String>();
    private Vector<String> actionsString = new Vector<String>();
    private String markerChar = new String("#");
	
    // Methods
    void setMarker(String marker)
    {
    	markerChar = marker;
    }
    
    final String elementToString()
    {    	
    	return element.toString();
    }
    
    final Map<MARKERS, String> setAttribute(String name, MARKERS marker)
    {
    	System.out.println("setClassName with marker: " + marker.toString());
    	
    	element.put(marker, name);
    	
    	return element;
    }
    
    final String getAttribute(MARKERS m)
    {
    	return element.get(m);
    }
    
    final String getClassNameFromPath(String path)
    {
    	int lastDot = path.lastIndexOf(".");
    	int lastSlash = path.lastIndexOf("/") + 1;
    	
    	String smallPath = path.substring(lastSlash, lastDot);
    	
    	return (smallPath.substring(0, 1).toUpperCase()+smallPath.substring(1, smallPath.length()));
    }
    
    Vector<LinkedHashMap<MARKERS, String>> parse(String xmlFilePath)
    {
    	System.out.println("parse - begin");
    	try
        {   		
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                // Informs clients of the XML document structure
                DefaultHandler handler = new DefaultHandler()
                {
                	boolean logentry = false; // it supports revisions
                	boolean author = false; // author of changes 
                	boolean message = false; // commit message
                	boolean paths = false; // begin paths
                	boolean path = false; // path to file
                	boolean date = false; // date of changes

                    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException
                    {

                       System.out.println("Start Element :" + qName);

                       if (qName.equalsIgnoreCase("LOGENTRY"))
                       {
                    	    element.clear();
                    	    currentRevision = attributes.getValue("revision");
                    	    setAttribute(currentRevision, MARKERS.REVISION);
                    	   
                            logentry = true;
                       }
                       
                       if (qName.equalsIgnoreCase("AUTHOR")) 
                       {
                            author = true;
                       }

                       if (qName.equalsIgnoreCase("PATHS")) 
                       {
                    	    pathsString.clear();
                    	    actionsString.clear();
                    	    
                            paths = true;
                       }
                       
                       if (qName.equalsIgnoreCase("MSG"))
                       {
                            message = true;
                       }
                       
                       if (qName.equalsIgnoreCase("PATH")) 
                       {
                    	    actionsString.add(attributes.getValue("action"));
                            path = true;
                       }
                       
                       if (qName.equalsIgnoreCase("DATE"))
                       {
                            date = true;
                       }
                    }

                    @SuppressWarnings("unchecked")
					public void endElement(String uri, String localName, String qName) throws SAXException
                    {
                	    if( qName.equalsIgnoreCase("LOGENTRY") )
                	    {
                		    LinkedHashMap<MARKERS, String> tempElement = (LinkedHashMap<MARKERS, String>) element.clone();
                		    
                		    for(int i = 0 ; i < pathsString.size() ;i++)
                		    {
                		    	setAttribute(getClassNameFromPath(pathsString.elementAt(i)), MARKERS.CLASSNAME);
                		    	setAttribute(pathsString.elementAt(i), MARKERS.PATH);
                    		    setAttribute(actionsString.elementAt(i), MARKERS.ACTION);
                    		    
                    		    System.out.println("Vector: " + vec.toString());
                    		    vec.addElement(element);
                    		    element = (LinkedHashMap<MARKERS, String>) tempElement.clone();
                    		    
                    		    System.out.println("String: " + element.toString());
                		    }
                	    }
                    }

                    public void characters(char ch[], int start, int length) throws SAXException
                    {
                    	if (logentry)
                        {
                            logentry = false;
                        }
                    	
                        if (author)
                        {
                        	setAttribute(new String(ch, start, length), MARKERS.AUTHOR);
      
                            author = false;
                        }

                        if (date)
                        {
                        	setAttribute(new String(ch, start, length), MARKERS.DATE);
                        	
                            date = false;
                        }

                        if (path)
                        {
                        	pathsString.add(new String(ch, start, length));
                        	
                            path = false;
                        }
                        
                        if (paths)
                        {  		
                            paths = false;
                        }
                        
                        if (message)
                        {
                        	String tempMessage = new String(ch, start, length);
                        	
                        	if(tempMessage.contains(markerChar))
                        	{
                        		setAttribute(tempMessage, MARKERS.MESSAGE);	
                        	}
                        	
                        	setAttribute(markerChar, MARKERS.MARKER);
                        	
                            message = false;
                        }
                    }
            };

            saxParser.parse(xmlFilePath, handler);    
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }	
    	
    	return vec;
    }
}