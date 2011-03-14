package timmons.android.map;

public class Placemark {

	String name;
	String description;
	PlacemarkShape shape;
	//Double latitude;
	//Double longitude;
	
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
	
	/**
	public Double getLatitude() {
	    return latitude;
	}
	public void setLatitude(Double latitude) {
	    this.latitude = latitude;
	}
	
	public Double getLongitude() {
	    return longitude;
	}
	public void setLongitude(Double longitude) {
	    this.longitude = longitude;
	}
	**/
	
	public void setShape(PlacemarkShape shape)
	{
		this.shape=shape;
	}
	public PlacemarkShape getShape()
	{
		return shape;
	}
	

}
