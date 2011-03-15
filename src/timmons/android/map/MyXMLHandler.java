package timmons.android.map;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;



public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	Placemark placemark;
	
	ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
 Boolean inCoordinateTag=false;
 Boolean inDescriptionTag=false;
 String description;
 Projection projection=null;
Boolean inPolygonTag=false;

 
	public  ArrayList<Placemark> getPlacemarks() {
		return placemarks;
	}

	public void setProjection(Projection projection)
	{
		this.projection=projection;
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
			} 
			else if(localName.equalsIgnoreCase("Description"))
			{
				inDescriptionTag=true;
				
			}
			
			else if(localName.equalsIgnoreCase("Polygon"))
			{
				inPolygonTag=true;
				
			}
			
			else if (localName.equalsIgnoreCase("coordinates")) {
				inCoordinateTag=true;
				
	
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
			else if(localName.equalsIgnoreCase("polygon"))
			{
				inPolygonTag=false;
				
			}
			
			if(localName.equalsIgnoreCase("Placemark"))
					
				{
					if(placemark!=null && placemark.getShape()!=null)
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
			
			if(inCoordinateTag && !inPolygonTag)
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
							  Double lng=d*1E6;
						      //placemark.setLongitude(d);
						      if(lastIndex>-1 && index!=lastIndex)
						      {
						    	  d = Double.valueOf(s.substring(index+1, lastIndex).trim()).doubleValue();
						    	  //placemark.setLatitude(d);
						      }
		
						  	Double lat=d*1E6;
						  	if(lat!=Double.NaN && lat!=0 & lng!=Double.NaN && lng!=0)
						  	{
							
						  	PlacemarkPoint placemarkPoint=new PlacemarkPoint(new GeoPoint(lat.intValue(), lng.intValue()));
							PlacemarkShape placemarkShape=(PlacemarkShape)placemarkPoint;
						    placemark.setShape(placemarkShape);
						  	}
							
						    }
						    catch (NumberFormatException nfe)
						    {
						    	System.out.print(nfe.getMessage());
						    }
					}
					
					
				}
				
				
			}
			else if(inPolygonTag && inCoordinateTag)
			{
				if(!s.equals("\n"))
				{
					//split the different vertices into one set
					String[] vertices=s.split("\\ ");
					PlacemarkPolygon poly=new PlacemarkPolygon();
					//loop through vertices
					for(int i=0;i<vertices.length;i++)
					{
						//now split each set vertex
						String[] vertex=vertices[i].split("\\,");
						try
						{
							Double lng=(Double.valueOf(vertex[0]).doubleValue())*1E6;
							Double lat=(Double.valueOf(vertex[1]).doubleValue())*1E6;
							
							if(lat!=Double.NaN && lat!=0 & lng!=Double.NaN && lng!=0)
						  	{
							
								poly.getVertices().add(new GeoPoint(lat.intValue(), lng.intValue()));
						  	
							
						  	}
						}
						 catch (NumberFormatException nfe)
						    {
						    	System.out.print(nfe.getMessage());
						    }
					}
					
					PlacemarkShape placemarkShape=(PlacemarkShape)poly;
				    placemark.setShape(placemarkShape);
					
					
					
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

