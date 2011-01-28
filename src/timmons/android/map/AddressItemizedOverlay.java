package timmons.android.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class AddressItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public AddressItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		 populate();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int j) {
		// TODO Auto-generated method stub
		return mOverlays.get(j);
	}
	
	public AddressItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(defaultMarker);
		  mContext = context;
		}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

}
