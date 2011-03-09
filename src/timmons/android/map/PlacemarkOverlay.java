package timmons.android.map;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
			
			if(placemarks!=null)
			{

				for (Placemark pt : placemarks) {
					if(pt!=null)
					{
						if(pt.getLatitude()!=null && pt.getLatitude()!=0 && pt.getLongitude()!=null && pt.getLongitude()!=0)
						{
							projection=mapView.getProjection();
							Point placemarkPoint=new Point();
							Double lat=pt.getLatitude()*1E6;
							Double lng=pt.getLongitude()*1E6;
							GeoPoint point1 = new GeoPoint(lat.intValue(), lng.intValue());
							projection.toPixels(point1, placemarkPoint);
				            canvas.drawCircle(placemarkPoint.x, placemarkPoint.y,2, paint);	
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
		if(isIdentify)
		{
			for (Placemark pt : placemarks) {
				
				if(pt!=null)
				{
					if(pt.getLatitude()!=null && pt.getLatitude()!=0 && pt.getLongitude()!=null && pt.getLongitude()!=0)
					{
						Double alat=pt.getLatitude();
						Double alng=pt.getLongitude();
						
						Double blat=point.getLatitudeE6()/1E6;
						Double blng=point.getLongitudeE6()/1E6;
						
						double currentDistance=Utils.calculateDistanceMeters(alng, alat, blng,blat);
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
				Double lat=nearestPlacemark.getLatitude()*1E6;
				Double lng=nearestPlacemark.getLongitude()*1E6;
				
				GeoPoint balloonTip=new GeoPoint(lat.intValue(), lng.intValue());
				
				mapView.removeView(balloonLayout);
				balloonLayout.setVisibility(View.VISIBLE);
				                     ((TextView)balloonLayout.findViewById(R.id.note_label)).setText(Html.fromHtml(currentDescription));
				                     
				                    
				                     mapView.getController().animateTo(balloonTip);
				mapView.addView(balloonLayout, new MapView.LayoutParams(200,200,balloonTip,MapView.LayoutParams.BOTTOM_CENTER));
				//mapView.setEnabled(false);        
				isIdentify=false;
			}
		}
	
	
		
		
		
		return true;
	}
	
	
}
