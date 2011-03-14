package timmons.android.map;

import com.google.android.maps.GeoPoint;

public class PlacemarkPoint extends PlacemarkShape{

	GeoPoint shape;
	
	public PlacemarkPoint(GeoPoint shape)
	{
		this.shape=shape;
	}
	public GeoPoint getShape()
	{
		return this.shape;
	}
}
