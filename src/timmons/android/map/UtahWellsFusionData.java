package timmons.android.map;

import java.util.ArrayList;

public class UtahWellsFusionData {
	
	private String name;
	private String plss;
	private String county;
	private String lease_num;
	private String wellstatus;
	private String total_oil;
	private String total_water;
	private String total_gas;
	private String api;
	private String lat;
	private String lng;
	private Placemark placemark;
	
	public void setAPI(String api) {
 		this.api=api;
 	}
	
	 	public String getWellName() {
	 		return name;
	 	}

	 	public void setName(String name) {
	 		this.name=name;
	 	}

	 	public String getWellPLSS() {
	 		return plss;
	 	}

	 	public void setWellPLSS(String plss) {
	 		this.plss=plss;
	 	}
	 	public String getCounty() {
	 		return county;
	 	}

	 	public void setCounty(String county) {
	 		this.county=county;
	 	}

	 	public String getLeaseNum() {
	 		return lease_num;
	 	}

	 	public void setLeaseNum(String leaseNum) {
	 		this.lease_num=leaseNum;
	 	}
	 	
	 	public String getWellStatus() {
	 		return wellstatus;
	 	}

	 	public void setWellStatus(String well_status) {
	 		this.wellstatus=well_status;
	 	}

	 	public String getLatitude() {
	 		return lat;
	 	}

	 	public void setLatitude(String lat) {
	 		this.lat=lat;
	 	}

	 	public String getLongitude() {
	 		return lng;
	 	}

	 	public void setLongitude(String lng) {
	 		this.lng=lng;
	 	}
}
