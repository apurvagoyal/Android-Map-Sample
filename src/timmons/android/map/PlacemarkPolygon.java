package timmons.android.map;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;


public class PlacemarkPolygon extends PlacemarkShape{

	private ArrayList<GeoPoint> vertices;
	private Path path;
	
	public PlacemarkPolygon()
	{
		this.vertices=new ArrayList<GeoPoint>();
	}
	
	public ArrayList<GeoPoint> getVertices()
	{
		return this.vertices;
	}
	
	public void setPath(Path path)
	{
		this.path=path;
	}
	
	public Boolean contains(float x, float y)
	{
		Boolean isPointInPolygon=false;
		
		if(this.path!=null)
		{
			RectF bounds=new RectF();
			this.path.computeBounds(bounds, true);
			
			if(bounds!=null)
			{			
				isPointInPolygon= bounds.contains(x, y);
			}
		}
			
		return isPointInPolygon;
	}
}
