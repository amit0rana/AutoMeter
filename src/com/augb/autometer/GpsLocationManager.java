/**
 * 
 */
package com.augb.autometer;

import java.util.Date;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


/**
 * @author SandeepChoudhary
 *
 */
public class GpsLocationManager implements LocationListener{

	private static GpsLocationManager mySelf;
	private boolean trackingStarted;
	private long trackingStartedDT;
	private long trackingStoppedDT;
	private LocationManager locMgr; 
	private Context cntx;
	private GPSLocation lastKnownLocation;
	private GpsNotificationListener listener;
	
	private GpsLocationManager(){}
	
	public static GpsLocationManager GetGPSLocationManger(){
		if(mySelf == null)
			mySelf = new GpsLocationManager();
		return mySelf;
	}

	public boolean start(GpsNotificationListener listenerObj, Context cntxObj) {
		cntx = cntxObj;
		listener = listenerObj;
		String provider = "";
		locMgr = (LocationManager) cntx.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (locMgr != null) {
			Criteria criteria = new Criteria();
			if (cntx
					.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
			} else if (cntx
					.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			}
			criteria.setCostAllowed(false);
			provider = locMgr.getBestProvider(criteria, true);
		}

		if (locMgr != null && trackingStarted == false) {

			locMgr.requestLocationUpdates(provider, 0, 0, this, cntx
					.getMainLooper());
			trackingStarted = true;
			Date now = new Date();
			this.setTrackingStartedDT(now.getTime());
			// location engine is started
			return true;
		}
		return false;
	}
	
	public boolean stop(){
		if (locMgr != null && trackingStarted == true){
			locMgr.removeUpdates(this);
			trackingStarted = false;
			locMgr = null;
			Date now = new Date();
			this.setTrackingStoppedDT(now.getTime());
			return true;
		}
		return false;
	}
	
	@Override
	public void onLocationChanged(Location newLocation) {
		if (newLocation != null && newLocation.hasAccuracy()) {
			GPSLocation loc = new GPSLocation();
			loc.setLastLocationUpdateDT(newLocation.getTime());
			loc.setLat(newLocation.getLatitude());
			loc.setLon(newLocation.getLongitude());
			loc.setLocAccuracy(newLocation.getAccuracy());
			loc.setSpeed(newLocation.getSpeed());
			loc.setBearing(newLocation.getBearing());
		}
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param trackingStartedDT the trackingStartedDT to set
	 */
	public void setTrackingStartedDT(long trackingStartedDT) {
		this.trackingStartedDT = trackingStartedDT;
	}

	/**
	 * @return the trackingStartedDT
	 */
	public long getTrackingStartedDT() {
		return trackingStartedDT;
	}

	/**
	 * @param trackingStoppedDT the trackingStoppedDT to set
	 */
	public void setTrackingStoppedDT(long trackingStoppedDT) {
		this.trackingStoppedDT = trackingStoppedDT;
	}

	/**
	 * @return the trackingStoppedDT
	 */
	public long getTrackingStoppedDT() {
		return trackingStoppedDT;
	}

	/**
	 * @param lastKnownLocation the lastKnownLocation to set
	 */
	public void setLastKnownLocation(GPSLocation lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}

	/**
	 * @return the lastKnownLocation
	 */
	public GPSLocation getLastKnownLocation() {
		return lastKnownLocation;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(GpsNotificationListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the listener
	 */
	public GpsNotificationListener getListener() {
		return listener;
	}
	
	
}
