package timmons.android.map;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SketchOverlay extends Overlay{

	//Location location;
	private final int mRadius=5;
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Point> lineVertices = new ArrayList<Point>();
	private ArrayList<Point> polyVertices = new ArrayList<Point>();
	
	private Projection projection;
	private Point lastPoint=null;
	private int selectedShape;
	private Paint paint;
	private Path path;
	/**
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	**/
	
	public void SetShape(int shape )
	{
		selectedShape=shape;
	}

	public void SetPaint(Paint myPaint )
	{
		paint=myPaint;
	}
	
	public void ClosePoly()
	{
		
	}
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		
		if(shadow=true)
		{
			//Double latitude=location.getLatitude()*1E6;
			//Double longitude=location.getLongitude()*1E6;
			//GeoPoint geoPoint=new GeoPoint(latitude.intValue(),longitude.intValue());
			
				for (Point point : points) {
		            canvas.drawCircle(point.x, point.y, 5, paint);		           
		        }
				
				if(lineVertices.size()>0)
				{	
					if(lineVertices.size()==1){canvas.drawCircle(lastPoint.x, lastPoint.y, 5, paint);}
					else
					{
						for (int number = 0; number <= lineVertices.size()-2; number++) {
							canvas.drawLine(lineVertices.get(number).x, lineVertices.get(number).y, lineVertices.get(number+1).x, lineVertices.get(number+1).y, paint);
						}	
					}
				}
				
				if(path!=null)
				{	
					canvas.drawCircle(lastPoint.x, lastPoint.y, 5, paint);
					
					if(polyVertices.size()>1)
					{
						for (int number = 0; number <= polyVertices.size()-2; number++) {
							path.moveTo(polyVertices.get(number).x, polyVertices.get(number).y);
							path.lineTo(polyVertices.get(number+1).x, polyVertices.get(number+1).y);				
					}
					 
						if(polyVertices.size()>2)
							{
							path.moveTo(polyVertices.get(polyVertices.size()-1).x, polyVertices.get(polyVertices.size()-1).y);
							path.lineTo(polyVertices.get(0).x, polyVertices.get(0).y);
							path.close();
							}
						
					canvas.drawPath(path, paint);
					}
						
					
                    
                     
				}
	
			

		}
		super.draw(canvas, mapView, shadow);
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView mapView)
	{		
		projection=mapView.getProjection();
		Point screenPoint=new Point();
		projection.toPixels(point, screenPoint);
		lastPoint=screenPoint;
		switch(selectedShape)
		{
		case 1:
			points.add(screenPoint);
			break;
			
		case 2:
			lineVertices.add(screenPoint);
			break;
			
		case 3:
			//if(path==null){path=new Path();}
			path=new Path();
			polyVertices.add(screenPoint);
			break;
		}
		
		return true;
	}
}
