package timmons.android.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import timmons.android.map.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

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
	 public static final int TEST = 25;
	
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
	private Paint paint;
	@Override
	protected boolean isRouteDisplayed() {
	return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);

		MapView googleMapView=(MapView)findViewById(R.id.myMapView);
	
		mapController=googleMapView.getController();
		
		gc=new Geocoder(this,Locale.US);
		
		googleMapView.setSatellite(false);
		googleMapView.setStreetView(true);
		googleMapView.displayZoomControls(true);
		googleMapView.setBuiltInZoomControls(true);
		
		//position of new york-statue of liberty
		//Double lat = 40.689213*1E6;
		//Double lng = -74.044558*1E6;
		//navigateToLocation(lat,lng);
		
		//add the overlay
		sketchOverlay=new SketchOverlay();
		List<Overlay> mapOverlays=googleMapView.getOverlays();
		mapOverlays.add(sketchOverlay);
			
		
		LocationManager locationManager;
		String context=Context.LOCATION_SERVICE;
		locationManager=(LocationManager)getSystemService(context);
		String provider=LocationManager.GPS_PROVIDER;
		Location location=locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
		
		paint=new Paint();
		paint.setARGB(250, 255, 0, 0);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true); 
		sketchOverlay.SetPaint(paint);
		
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
		MenuItem menuItemSwitchView = menu.add(groupId, MENU_ITEM_SWITCH_VIEW,Menu.NONE, R.string.switch_satellite_view_menu_text);
		
		MenuItem menuItemSave = menu.add(groupId, MENU_ITEM_SAVE,Menu.NONE, R.string.save_menu_text);
		MenuItem menuItemMore = menu.add(groupId, MENU_ITEM_MORE,Menu.NONE, R.string.more_options_menu_text);
		MenuItem menuItemSnapGPS = menu.add(groupId, MENU_ITEM_SNAP_GPS,Menu.NONE, R.string.snap_gps_menu_text);
		MenuItem menuItemSearchAddress = menu.add(groupId, MENU_ITEM_SEARCH_ADDRESS,Menu.NONE, R.string.search_address_menu_text);
		
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
	return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// Find which menu item has been selected
		switch (item.getItemId()) {
		// Check for each known menu item
		case (MENU_ITEM_POINT):
			sketchOverlay.SetShape(1);
		return true;
		case (MENU_ITEM_LINE):
			sketchOverlay.SetShape(2);
			return true;
		case (MENU_ITEM_POLYGON):
			sketchOverlay.SetShape(3);
			return true;
		case (MENU_ITEM_SEARCH_ADDRESS):
			getAddress();
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
			//displayCurrentLocation(location);
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
			      }
		    	  
		      }
		      else{displayMessage("Invalid address","Cannot find any address");}
	      }
	   }
	
}
