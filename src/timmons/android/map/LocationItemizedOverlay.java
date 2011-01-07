package timmons.android.map;

import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	Location location;
	public Location getLocation() {
	return location;
	}
	public void setLocation(Location location) {
	this.location = location;
	}
	public LocationItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
		populate();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int index) {
		
		Double lat = location.getLatitude()*1E6;
		Double lng = location.getLongitude()*1E6;
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
		OverlayItem oi;
		oi = new OverlayItem(point, "Marker", "Marker Text");
		return oi;
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 1;
	}

}
