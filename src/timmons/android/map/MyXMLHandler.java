package timmons.android.map;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;



public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	Placemark placemark;
	
	ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
 Boolean inCoordinateTag=false;
 Boolean inDescriptionTag=false;
 String description;
 


 
	public  ArrayList<Placemark> getPlacemarks() {
		return placemarks;
	}

	

	/** Called when tag starts ( ex:- <name>AndroidPeople</name>
	 * -- <name> )*/
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		try
		{
			currentElement = true;

			if (localName.equals("Placemark"))
			{
				/** Start */
				placemark= new Placemark();
			} else if (localName.equalsIgnoreCase("coordinates")) {
				inCoordinateTag=true;
				
	
			}
			else if(localName.equalsIgnoreCase("Description"))
			{
				inDescriptionTag=true;
				
			}
		
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		try
		{
			currentElement = false;
			if(localName.equalsIgnoreCase("coordinates"))
			{
				inCoordinateTag=false;
				}
			else if(localName.equalsIgnoreCase("description"))
			{				
				placemark.setDescription(description);
				description=null;
				inDescriptionTag=false;
				}
			
			if(localName.equalsIgnoreCase("Placemark"))
					
				{
					if(placemark!=null && placemark.getLongitude()!=0)
					{
					placemarks.add(placemark);
					}
				
				}
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		
		
	}

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
	 * -- to get AndroidPeople Character ) */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		try
		{
			String s = new String(ch, start, length);
			
			if(inCoordinateTag)
			{
				
				
				//113.73493,38.54374,0
				if(!s.equals("\n"))
				{
					int index=s.indexOf(",");
					int lastIndex=s.lastIndexOf(",");
					if(index>-1)
					{
						 try
						    {
						      Double d = Double.valueOf(s.substring(0, index).trim()).doubleValue();
						     
						      placemark.setLongitude(d);
						      if(lastIndex>-1 && index!=lastIndex)
						      {
						    	  d = Double.valueOf(s.substring(index+1, lastIndex).trim()).doubleValue();
						    	  placemark.setLatitude(d);
						      }
						      
						    }
						    catch (NumberFormatException nfe)
						    {
						    	System.out.print(nfe.getMessage());
						    }
					}
					
					
				}
				
				
			}
			else if(inDescriptionTag)
			{
				if(!s.equals("\n"))
				{
					
					if(description==null){description=s;}
					else {
						
						description=description+s;
					}
					
					/**
					
					**/
				}
				
			}
			if (currentElement) {
				//String s = new String(ch, start, length);
				//if(!s.equalsIgnoreCase("\n"))
				//{
					currentValue=s;
				//}
				currentElement = false;
			}
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		

	}
	
	
	

}

