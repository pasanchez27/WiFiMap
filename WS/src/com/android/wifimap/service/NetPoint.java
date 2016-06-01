package com.android.wifimap.service;
/**
 * Created by Pablo on 29/05/2016.
 */
public class NetPoint {
    private double lat;
    private double lon;
    private String place;
    private String netName;
    private String netPwd;

    public NetPoint(double lat, double lon, String place, String netName, String netPwd) {
        this.lat = lat;
        this.lon = lon;
        this.place = place;
        this.netName = netName;
        this.netPwd = netPwd;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getNetPwd() {
        return netPwd;
    }

    public void setNetPwd(String netPwd) {
        this.netPwd = netPwd;
    }

	@Override
	public String toString() {
		return String.format("\t\n\tNetPoint [\n\t\tlat=%s, \n\t\tlon=%s, \n\t\tplace=%s, \n\t\tnetName=%s, \n\t\tnetPwd=%s\n\t]", lat, lon, place, netName, netPwd);
	}
    
    
}
