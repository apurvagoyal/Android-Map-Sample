package timmons.android.map;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;


public class PlacemarkPolygon extends PlacemarkShape{

	public ArrayList<GeoPoint> vertices;
	
	public PlacemarkPolygon()
	{
		this.vertices=new ArrayList<GeoPoint>();
	}
	
	public ArrayList<GeoPoint> getVertices()
	{
		return this.vertices;
	}
}
