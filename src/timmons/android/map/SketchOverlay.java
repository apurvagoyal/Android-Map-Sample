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
	//private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<MapPoint> points = new ArrayList<MapPoint>();
	private ArrayList<MapLine> lines = new ArrayList<MapLine>();
	private ArrayList<MapPolygon> polygons = new ArrayList<MapPolygon>();
	
	private Projection projection;
	private Point lastPoint=null;
	private ShapeType selectedShape;
	private Paint paint;
	private Path path;
	private MapLine currentLine=null;
	private MapPolygon currentPolygon=null;
	private int color;
	/**
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	**/
	
	public enum ShapeType {
	    POINT,LINE,POLYGON
	}
	
	public void SetShape(ShapeType shape )
	{
		selectedShape=shape;
	}

	public void SetColor(int clor )
	{
		color=clor;
		paint.setColor(color);
	}
	
	public void FinishFeature()
	{
		switch(selectedShape)
		{
		case LINE:
			lines.add(currentLine);
			currentLine=null;
			break;
		case POLYGON:
			polygons.add(currentPolygon);
			currentPolygon=null;
			path=null;
			break;
		}
		
	}
	
	public void StartFeature()
	{
		switch(selectedShape)
		{
		case LINE:
			currentLine=new MapLine();
			currentLine.color=color;
			break;
		case POLYGON:
			currentPolygon=new MapPolygon();
			currentPolygon.color=color;
			break;
		}
		
		
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		
		if(shadow=true)
		{
			paint=new Paint();
			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setAntiAlias(true); 
			
			
			
				for (MapPoint pt : points) {
					int col=pt.color;
					paint.setColor(col);
		            canvas.drawCircle(pt.point.x, pt.point.y, 5, paint);		           
		        }
				
				if(lines.size()>0)
				{
					for(MapLine line:lines)
					{
						if(line!=null)
						{
							if(line.vertices.size()>0)
							{	
								paint.setColor(line.color);
							
								
								if(line.vertices.size()==1){canvas.drawCircle(lastPoint.x, lastPoint.y, 5, paint);}
								else
								{
									for (int number = 0; number <= line.vertices.size()-2; number++) {
										canvas.drawLine(line.vertices.get(number).x, line.vertices.get(number).y, line.vertices.get(number+1).x, line.vertices.get(number+1).y, paint);
									}	
								}
							}
						}
					
					}
				}
				
					if(currentLine!=null)
					{
						if(currentLine.vertices.size()>0)
						{	
							paint.setColor(color);
							if(currentLine.vertices.size()==1){canvas.drawCircle(lastPoint.x, lastPoint.y, 5, paint);}
							else
							{
								for (int number = 0; number <= currentLine.vertices.size()-2; number++) {
									canvas.drawLine(currentLine.vertices.get(number).x, currentLine.vertices.get(number).y, currentLine.vertices.get(number+1).x, currentLine.vertices.get(number+1).y, paint);
								}	
							}
						}
					}
				
				
				/**
				 * Drawing polygons
				 * 
				 */
				
					//if there are multiple polygons in list
					if(polygons.size()>0)
					{
						for(MapPolygon polygon:polygons)
						{			
							if(polygon!=null)
							{
								paint.setColor(polygon.color);
								
								if(polygon.vertices.size()>1)
								{
									Path polyPath=new Path();
									
									for (int number = 0; number <= polygon.vertices.size()-2; number++) {
										polyPath.moveTo(polygon.vertices.get(number).x, polygon.vertices.get(number).y);
										polyPath.lineTo(polygon.vertices.get(number+1).x, polygon.vertices.get(number+1).y);				
								}
								 
									if(polygon.vertices.size()>2)
										{
										polyPath.moveTo(polygon.vertices.get(polygon.vertices.size()-1).x, polygon.vertices.get(polygon.vertices.size()-1).y);
										polyPath.lineTo(polygon.vertices.get(0).x, polygon.vertices.get(0).y);
										polyPath.close();
										}
									
								canvas.drawPath(polyPath, paint);
								}
							}
							
						}
						
					}
					
				if(currentPolygon!=null)
				{	
					paint.setColor(color);
					canvas.drawCircle(lastPoint.x, lastPoint.y, 5, paint);
					
					if(currentPolygon.vertices.size()>1)
					{
						for (int number = 0; number <= currentPolygon.vertices.size()-2; number++) {
							path.moveTo(currentPolygon.vertices.get(number).x, currentPolygon.vertices.get(number).y);
							path.lineTo(currentPolygon.vertices.get(number+1).x, currentPolygon.vertices.get(number+1).y);				
					}
					 
						if(currentPolygon.vertices.size()>2)
							{
							path.moveTo(currentPolygon.vertices.get(currentPolygon.vertices.size()-1).x, currentPolygon.vertices.get(currentPolygon.vertices.size()-1).y);
							path.lineTo(currentPolygon.vertices.get(0).x, currentPolygon.vertices.get(0).y);
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
		case POINT:
			points.add(new MapPoint(screenPoint,color));
			break;
			
		case LINE:
			currentLine.vertices.add(screenPoint);
			break;
			
		case POLYGON:
			path=new Path();
			currentPolygon.vertices.add(screenPoint);
			break;
		}
		
		return true;
	}
}
