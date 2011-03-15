package timmons.android.map;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PlacemarkOverlay extends Overlay{
	private Paint paint;
	private Projection projection;
	private ArrayList<Placemark> placemarks=null;
	private int color=0xFFFF0000;
	private String currentDescription=null;
	private Boolean isIdentify=false;
	private BalloonLayout balloonLayout=null;
	public void SetPlacemarks( ArrayList<Placemark> newplacemarks)
	{
		placemarks=newplacemarks;
	}
	
	public void SetIdentifyMode()
	{
		
		isIdentify=true;
	}
	
	public void SetBalloonLayout(BalloonLayout balloon)
	{
		balloonLayout=balloon;
	}
	
	public void SetColor(int clor )
	{
		color=clor;
		paint.setColor(color);
	}
		
	public void ClearBalloonLayout( )
	{
		if(balloonLayout!=null)
		{
			balloonLayout.setVisibility(View.GONE);     
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
			paint.setColor(color);
			projection=mapView.getProjection();
			if(placemarks!=null)
			{

				for (Placemark pt : placemarks) {
					if(pt!=null)
					{					
						
						if(pt.getShape()!=null)
						{
							PlacemarkShape shape=pt.getShape();
							if(shape instanceof PlacemarkPoint )
							{
								PlacemarkPoint placemarkPoint=(PlacemarkPoint)shape;
								Point point1=new Point();
								projection.toPixels(placemarkPoint.getShape(),point1);
								canvas.drawCircle(point1.x, point1.y, 2, paint);
							}
							else if(shape instanceof PlacemarkPolygon )
							{
								PlacemarkPolygon placemarkPolygon=(PlacemarkPolygon)shape;
								Path polyPath=new Path();
								
								for (int number = 0; number <= placemarkPolygon.getVertices().size()-2; number++) {
									
									Point point1=new Point();
									GeoPoint vertex=placemarkPolygon.getVertices().get(number);
									projection.toPixels(vertex,point1);
									polyPath.moveTo(point1.x, point1.y);
									vertex=placemarkPolygon.getVertices().get(number+1);
									projection.toPixels(vertex,point1);
									polyPath.lineTo(point1.x, point1.y);				
							}
							 
								if(placemarkPolygon.getVertices().size()>2)
									{
									Point point2=new Point();
									GeoPoint vertex=placemarkPolygon.getVertices().get(placemarkPolygon.getVertices().size()-1);
									projection.toPixels(vertex,point2);
									
									polyPath.moveTo(point2.x, point2.y);
									
									vertex=placemarkPolygon.getVertices().get(0);
									projection.toPixels(vertex,point2);
									
									polyPath.lineTo(point2.x, point2.y);
									polyPath.close();
									
									}
								
								placemarkPolygon.setPath(polyPath);
							canvas.drawPath(polyPath, paint);
							}
							
						}
					
					}
						           
		        }
			}
				

		}
		super.draw(canvas, mapView, shadow);
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView mapView)
	{	
		double distance=0;
		Placemark nearestPlacemark=null;
		GeoPoint geoPoint=null;
		if(isIdentify)
		{
			for (Placemark pt : placemarks) {
				
				if(pt!=null)
				{
					PlacemarkShape shape=pt.getShape();
					if(shape instanceof PlacemarkPoint )
					{
						PlacemarkPoint placemarkPoint=(PlacemarkPoint)shape;
						geoPoint=placemarkPoint.getShape();
						
						Double lat=geoPoint.getLatitudeE6()/1E6;
						Double lng=geoPoint.getLongitudeE6()/1E6;
						
						if(lat!=null && lat!=0 && lng!=null && lng!=0)
						{						
							
							Double blat=point.getLatitudeE6()/1E6;
							Double blng=point.getLongitudeE6()/1E6;
							
							double currentDistance=Utils.calculateDistanceMeters(lng, lat, blng,blat);
							if(distance==0)
							{		
								distance=currentDistance;
								nearestPlacemark=pt;
								
							}else
							{
								if(currentDistance<distance)
								{
									distance=currentDistance;
									nearestPlacemark=pt;
									
								}
							}
						}
					}
					else if(shape instanceof PlacemarkPolygon)
					{
						//position where user clicked on map
						Point point2=new Point();
						projection.toPixels(point,point2);
						//find if that point is inside the polygon
						PlacemarkPolygon placemarkPolygon=(PlacemarkPolygon)shape;
						geoPoint=point;
						if(placemarkPolygon.contains(point2.x, point2.y))
						{
							nearestPlacemark=pt;
						}
					}
					
				}
				
				
			}
			
			if(nearestPlacemark!=null)
			{
				
				currentDescription=nearestPlacemark.getDescription();
				/**
				AlertDialog alertDialog=new AlertDialog.Builder(context).create();
				alertDialog.setTitle("Description");
				alertDialog.setMessage(Html.fromHtml(currentDescription));
				alertDialog.show();
				**/
				/**
				PlacemarkShape shape=nearestPlacemark.getShape();
				if(shape instanceof PlacemarkPoint )
				{				
					
					mapView.removeView(balloonLayout);
					balloonLayout.setVisibility(View.VISIBLE);
					                     ((TextView)balloonLayout.findViewById(R.id.note_label)).setText(Html.fromHtml(currentDescription));
					                     
					                    
					                     mapView.getController().animateTo(geoPoint);
					mapView.addView(balloonLayout, new MapView.LayoutParams(200,200,geoPoint,MapView.LayoutParams.BOTTOM_CENTER));
					//mapView.setEnabled(false);        
					
				
				}
				else if(shape instanceof PlacemarkPolygon)
				{
					PlacemarkPoint placemarkPoint=(PlacemarkPoint)shape;
					GeoPoint geoPoint=placemarkPoint.getShape();
					
				
				}
				**/
				if(geoPoint!=null)
				{
					mapView.removeView(balloonLayout);
					balloonLayout.setVisibility(View.VISIBLE);
					                     ((TextView)balloonLayout.findViewById(R.id.note_label)).setText(Html.fromHtml(currentDescription));
					                     
					                    
					                     mapView.getController().animateTo(geoPoint);
					mapView.addView(balloonLayout, new MapView.LayoutParams(200,200,geoPoint,MapView.LayoutParams.BOTTOM_CENTER));
				}
				
				
				
			}
			isIdentify=false;
		}
	
	
		
		
		
		return true;
	}
	
	
}
