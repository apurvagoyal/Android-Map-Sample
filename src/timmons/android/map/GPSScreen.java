package timmons.android.map;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import timmons.android.map.R;
import timmons.android.map.SketchOverlay.ShapeType;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GPSScreen extends MapActivity {
	
	MapController mapController;
	SketchOverlay sketchOverlay;
	Menu menu;
	String searchAddress;
	Geocoder gc=null;
	ProgressDialog pd=null;
	List<Address> foundAdresses=null;
	private MyLocationOverlay locationOverlay;
	static final private int MENU_ITEM_POINT = Menu.FIRST;
	static final private int MENU_ITEM_LINE = Menu.FIRST+1;
	static final private int MENU_ITEM_POLYGON = Menu.FIRST+2;
	static final private int MENU_ITEM_SAVE = Menu.FIRST+3;
	static final private int MENU_ITEM_SWITCH_VIEW = Menu.FIRST+4;
	static final private int MENU_ITEM_MORE = Menu.FIRST+5;
	static final private int MENU_ITEM_SNAP_GPS = Menu.FIRST+6;
	static final private int MENU_ITEM_LAYERS = Menu.FIRST+7;
	static final private int MENU_ITEM_SEARCH_ADDRESS = Menu.FIRST+8;
	static final private int MENU_ITEM_CHANGE_COLOR = Menu.FIRST+9;
	static final private int MENU_ITEM_JOHN_PIZZA_LOCATIONS = Menu.FIRST+10;
	static final private int MENU_ITEM_IDENTIFY = Menu.FIRST+11;
	static final private int MENU_ITEM_RESET = Menu.FIRST+12;
	static final private int MENU_ITEM_TREATMENT = Menu.FIRST+13;
	private static final int COLOR_SUBACTIVITY=1;
	 public static final int TEST = 25;
	 private boolean isFeatureStarted=false;
	private ShapeType currentShapeType=ShapeType.POINT;
	private AddressItemizedOverlay itemizedAddressOverlay;
	private GeoPoint currentAddressLocation=null;
	private Location currentGPSLocation=null;
	private LocationManager locationManager;;
	private PlacemarkOverlay placemarkOverlay;
	private MapView googleMapView;
	private List<Overlay> mapOverlays;
	private final LocationListener locationListener=new LocationListener()
	{
		public void onLocationChanged(Location location){
			
			updateWithNewLocation(location);
			
		}
		
		public void onProviderDisabled(String provider){
			//updateWithNewLocation(null);
		}
		
		public void onProviderEnabled(String provider){}
		public void onStatusChanged(String provider, int status, Bundle extras){}
		
	};
	//private Paint paint;
	@Override
	protected boolean isRouteDisplayed() {
	return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);

		googleMapView=(MapView)findViewById(R.id.myMapView);
	
		mapController=googleMapView.getController();
		
		gc=new Geocoder(this,Locale.US);
		
		googleMapView.setSatellite(false);
		googleMapView.displayZoomControls(true);
		googleMapView.setBuiltInZoomControls(true);
		
		//position of new york-statue of liberty
		//Double lat = 40.689213*1E6;
		//Double lng = -74.044558*1E6;
		//navigateToLocation(lat,lng);
		
		
		mapOverlays=googleMapView.getOverlays();
		
		
		
		
		//Drawable drawable = this.getResources().getDrawable(R.drawable.iconb1);
		//itemizedAddressOverlay= new AddressItemizedOverlay(drawable);
		//mapOverlays.add(itemizedAddressOverlay);
		
		
		String context=Context.LOCATION_SERVICE;
		locationManager=(LocationManager)getSystemService(context);
		String provider=LocationManager.GPS_PROVIDER;
		Location location=locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
		
		/**
		paint=new Paint();
		paint.setARGB(250, 255, 0, 0);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true); 
		sketchOverlay.SetPaint(paint);
		**/
		
		//initLocationOverlay();
		
		updateWithNewLocation(location);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu=menu;
		super.onCreateOptionsMenu(menu);
		
		int groupId=0;
		// Create the menu item and keep a reference to it.
		MenuItem menuItemPoint = menu.add(groupId, MENU_ITEM_POINT,Menu.NONE, R.string.create_point_menu_text);
		MenuItem menuItemLine = menu.add(groupId, MENU_ITEM_LINE,Menu.NONE, R.string.create_line_menu_text);
		MenuItem menuItemPolygon = menu.add(groupId, MENU_ITEM_POLYGON,Menu.NONE, R.string.create_polygon_menu_text);
		MenuItem menuLoadFusionData=menu.add(groupId,MENU_ITEM_JOHN_PIZZA_LOCATIONS,Menu.NONE,R.string.load_fusion_data_menu_title);
		MenuItem menuWMITreatments=menu.add(groupId,MENU_ITEM_TREATMENT,Menu.NONE,R.string.wmi_treatment_menu_title);
		
		MenuItem menuItemSwitchView = menu.add(groupId, MENU_ITEM_SWITCH_VIEW,Menu.NONE, R.string.switch_satellite_view_menu_text);
		
		
		//MenuItem menuItemMore = menu.add(groupId, MENU_ITEM_MORE,Menu.NONE, R.string.more_options_menu_text);
		MenuItem menuItemSave = menu.add(groupId, MENU_ITEM_SAVE,Menu.NONE, R.string.save_menu_text);
		MenuItem menuIdentify=menu.add(groupId,MENU_ITEM_IDENTIFY,Menu.NONE,R.string.identify_menu_title);
		MenuItem menuItemSnapGPS = menu.add(groupId, MENU_ITEM_SNAP_GPS,Menu.NONE, R.string.snap_gps_menu_text);
		MenuItem menuItemSearchAddress = menu.add(groupId, MENU_ITEM_SEARCH_ADDRESS,Menu.NONE, R.string.search_address_menu_text);
		MenuItem menuItemChangeColor=menu.add(groupId,MENU_ITEM_CHANGE_COLOR,Menu.NONE,R.string.change_color_menu_title);
		
		
		MenuItem menuReset=menu.add(groupId,MENU_ITEM_RESET,Menu.NONE,R.string.reset_menu_title);
		//can add option for changing view in more options
		//finish sketch
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);
	MenuItem menuItem = menu.findItem(MENU_ITEM_SWITCH_VIEW);
	MapView googleMapView=(MapView)findViewById(R.id.myMapView);
	if(googleMapView.isSatellite()){
		menuItem.setTitle(R.string.switch_map_view_menu_text);
	
	}
	else{menuItem.setTitle(R.string.switch_satellite_view_menu_text);}
	
	//switch title of line menu item finish_line_text
	if(currentShapeType==ShapeType.LINE)
	{
		MenuItem menuItemLine = menu.findItem(MENU_ITEM_LINE);
		if(isFeatureStarted)
		{
			menuItemLine.setTitle(R.string.finish_line_text);
		}
		else{menuItemLine.setTitle(R.string.create_line_menu_text);}
	}
	else if(currentShapeType==ShapeType.POLYGON)
	{
		MenuItem menuItemPoly = menu.findItem(MENU_ITEM_POLYGON);
		if(isFeatureStarted)
		{
			menuItemPoly.setTitle(R.string.finish_polygon_menu_text);
		}
		else{menuItemPoly.setTitle(R.string.create_polygon_menu_text);}
	}
	
	return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// Find which menu item has been selected
		switch (item.getItemId()) {
		// Check for each known menu item
		case(MENU_ITEM_RESET):
			if(placemarkOverlay!=null)
			{
				placemarkOverlay.ClearBalloonLayout();
			}
			   
			mapOverlays.clear();
			return true;
			
		case (MENU_ITEM_IDENTIFY):
			if(placemarkOverlay!=null)
			{
				placemarkOverlay.SetIdentifyMode();
			}
			return true;
		case (MENU_ITEM_POINT):
			//add the overlay
			if(sketchOverlay==null){sketchOverlay=new SketchOverlay();}
		
		if(!mapOverlays.contains(sketchOverlay)){mapOverlays.add(sketchOverlay);}
			currentShapeType=ShapeType.POINT;
			sketchOverlay.SetShape(currentShapeType);
		return true;
		case (MENU_ITEM_LINE):
			//add the overlay
			if(sketchOverlay==null){sketchOverlay=new SketchOverlay();}
		if(!mapOverlays.contains(sketchOverlay)){mapOverlays.add(sketchOverlay);}
			currentShapeType=ShapeType.LINE;
			sketchOverlay.SetShape(currentShapeType);
			if(isFeatureStarted)
			{
				sketchOverlay.FinishFeature();
			}else{sketchOverlay.StartFeature();}
			isFeatureStarted=!isFeatureStarted;
			return true;
		case (MENU_ITEM_POLYGON):
			//add the overlay
			if(sketchOverlay==null){sketchOverlay=new SketchOverlay();}
		if(!mapOverlays.contains(sketchOverlay)){mapOverlays.add(sketchOverlay);}
			currentShapeType=ShapeType.POLYGON;
			sketchOverlay.SetShape(currentShapeType);
		if(isFeatureStarted)
		{
			sketchOverlay.FinishFeature();
		}else{sketchOverlay.StartFeature();}
		isFeatureStarted=!isFeatureStarted;
			return true;
		case (MENU_ITEM_SEARCH_ADDRESS):
			getAddress();
			return true;
		case (MENU_ITEM_JOHN_PIZZA_LOCATIONS):
			parseFusionData("http://www.google.com/fusiontables/exporttable?query=select+col0%2C+col1%2C+col2%2C+col3+from+297050+&o=kmllink&g=col2");
			return true;
		case (MENU_ITEM_TREATMENT):
			parseFusionData("http://www.google.com/fusiontables/exporttable?query=select+col0%2C+col1%2C+col2%2C+col3%2C+col4%2C+col5%2C+col6%2C+col7%2C+col8%2C+col9%2C+col10%2C+col11%2C+col12%2C+col13%2C+col14%2C+col15%2C+col16%2C+col17%2C+col18%2C+col19%2C+col20%2C+col21%2C+col22%2C+col23%2C+col24%2C+col25%2C+col26%2C+col27%2C+col28%2C+col29%2C+col30%2C+col31%2C+col32%2C+col33%2C+col34%2C+col35%2C+col36%2C+col37%2C+col38+from+502688+&o=kmllink&g=col25");
			return true;
		case (MENU_ITEM_SNAP_GPS):
			if(currentGPSLocation!=null){sketchOverlay.SnapMarker(currentGPSLocation);}
			return true;
		case (MENU_ITEM_SWITCH_VIEW):
			MapView googleMapView=(MapView)findViewById(R.id.myMapView);
		if(googleMapView.isSatellite()){
			googleMapView.setSatellite(false);
			googleMapView.setStreetView(true);
		}else{
			googleMapView.setSatellite(true);
			googleMapView.setStreetView(false);	
		}
			return true;
			
		case (MENU_ITEM_CHANGE_COLOR):
			ColorPickerDialog.OnColorChangedListener colorChangedListener=new ColorPickerDialog.OnColorChangedListener()
		{
			 public void colorChanged(int color) {
				 sketchOverlay.SetColor(color);
	            }
		
		};
			ColorPickerDialog colorPicker=new ColorPickerDialog(this,colorChangedListener,0xFFFF0000);
		colorPicker.show();
		
		
		}
		// Return false if you have not handled the menu item.
		return false;
	}
	
	
	private void updateWithNewLocation(Location location)
	{	
		if(location!=null)
		{			
			Double geoLat=location.getLatitude()*1E6;
			Double geoLng=location.getLongitude()*1E6;
			navigateToLocation(geoLat,geoLng);
			displayAddressMarker(geoLat,geoLng);
			currentGPSLocation=location;
		}
	}
			

	private void getAddress()
	{
		AlertDialog.Builder addressAlert = new AlertDialog.Builder(this);
		addressAlert.setTitle("Enter Address");
		addressAlert.setMessage("Please enter the address to be geocoded");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		addressAlert.setView(input);
		
		addressAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				new SearchAddressTask().execute(input.getText().toString());
				
				/**
				searchAddress = input.getText().toString();
				try {
				       foundAdresses = gc.getFromLocationName(searchAddress, 5); //Search addresses
				      
				    }
				    catch (Exception e) {
				      //@todo: Show error message-see documentation for android error handling
				    	displayMessage("Error",e.getMessage());
				    }
				if(foundAdresses.size()>0)
			      {
			    	  for(int k=0;k<foundAdresses.size();++k)
				      {
				    	  //@todo: show the list in a listview
				    	  Address foundAddress=foundAdresses.get(0);
				    	  double lat=foundAddress.getLatitude();
				    	  double lon=foundAddress.getLongitude();
				    	  
				    	  navigateToLocation((lat * 1000000), (lon * 1000000));
				      }
			    	  
			      }
			      else{displayMessage("Invalid address","Cannot find any address");}
				
				**/
				
				
				
			  
			  }
			});

		addressAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

		addressAlert.show();
	}
	
	private void displayMessage(String title,String message)
	{
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.show();
	}

	private void navigateToLocation(Double lat, Double lon)
	{
		GeoPoint point1 = new GeoPoint(lat.intValue(), lon.intValue());
		mapController.animateTo(point1);
		mapController.setCenter(point1);
		mapController.setZoom(15);
	}
	
	private void parseFusionData(String url)
	{
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			 /** Send URL to parse XML Tags */
			 //URL sourceUrl = new URL("http://www.google.com/fusiontables/exporttable?query=select+col0%2C+col1%2C+col2%2C+col3%2C+col4%2C+col5%2C+col6%2C+col7%2C+col8%2C+col9%2C+col10%2C+col11%2C+col12%2C+col13%2C+col14+from+408650+&o=kmllink&g=col14");
			 /** Create handler to handle XML Tags ( extends DefaultHandler ) */
			 
			//
//http://www.google.com/fusiontables/exporttable?query=select+col0%2C+col1%2C+col2%2C+col3%2C+col4%2C+col5%2C+col6%2C+col7%2C+col8%2C+col9%2C+col10%2C+col11%2C+col12%2C+col13%2C+col14%2C+col15%2C+col16%2C+col17%2C+col18%2C+col19+from+503057+&o=kmllink&g=col15

			 URL sourceUrl=new URL(url);
			 
			 MyXMLHandler myXMLHandler = new MyXMLHandler();
			 xr.setContentHandler(myXMLHandler);
			 InputSource is=new InputSource();
			 is.setEncoding("UTF-8"); 
			
             is.setByteStream(sourceUrl.openStream()); 
			 //xr.parse(new InputSource(sourceUrl.openStream()));
             xr.parse(is);
         	
         	if(myXMLHandler.getPlacemarks().size()>0)
         	{
         		 LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         		BalloonLayout noteBaloon = (BalloonLayout) layoutInflater.inflate(R.layout.balloonlayout, null);
                 RelativeLayout.LayoutParams layoutParams   = new RelativeLayout.LayoutParams(200,100);
                 layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                 layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                 noteBaloon.setLayoutParams(layoutParams);   
                 
         		//mapOverlays.remove(sketchOverlay);
         		placemarkOverlay=new PlacemarkOverlay();
         		//placemarkOverlay.SetContext(this);
         		placemarkOverlay.SetBalloonLayout(noteBaloon);
         		placemarkOverlay.SetPlacemarks(myXMLHandler.getPlacemarks());
mapOverlays.add(placemarkOverlay);

        		googleMapView.postInvalidate();
         	}
         	
			int m=0;
		}
		catch(Exception e)
		{
			System.out.println("XML Pasing Excpetion = " + e);

		}
		



	}
	
	private void displayAddressMarker(Double lat, Double lon)
	{
		currentAddressLocation = new GeoPoint(lat.intValue(), lon.intValue());
		
		OverlayItem overlayitem = new OverlayItem(currentAddressLocation,null,null);
		itemizedAddressOverlay.addOverlay(overlayitem);
		
	}
	
	 private class SearchAddressTask extends AsyncTask<String, Void, Void> {
	      //private final ProgressDialog dialog = new ProgressDialog(Main.this);

	      // can use UI thread here
	      protected void onPreExecute() {
	    	  pd=ProgressDialog.show(GPSScreen.this, "Working..", "Searching your address", true, false);
	      }

	      // automatically done on worker thread (separate from UI thread)
	      protected Void doInBackground(final String... args) {
	    		try {
	    			searchAddress = args[0];
				       foundAdresses = gc.getFromLocationName(searchAddress, 5); //Search addresses
				       //Thread.sleep(1500);
				    }
				    catch (Exception e) {
				      //@todo: Show error message-see documentation for android error handling
				    	displayMessage("Error",e.getMessage());
				    }
	         return null;
	      }

	      // can use UI thread here
	      protected void onPostExecute(final Void unused) {
	    	  pd.dismiss();       
	    	  if(foundAdresses.size()>0)
		      {
		    	  for(int k=0;k<foundAdresses.size();++k)
			      {
			    	  //@todo: show the list in a listview
			    	  Address foundAddress=foundAdresses.get(0);
			    	  double lat=foundAddress.getLatitude();
			    	  double lon=foundAddress.getLongitude();
			    	  
			    	  navigateToLocation((lat * 1000000), (lon * 1000000));
			    	  displayAddressMarker((lat * 1000000), (lon * 1000000));
			      }
		    	  
		      }
		      else{displayMessage("Invalid address","Cannot find any address");}
	      }
	   }
	
}
