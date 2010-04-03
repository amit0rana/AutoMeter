/**
 * 
 */
package com.augb.autometer;

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
	private GpsLocationCalculator calu;
	private boolean trackingStarted;
	private LocationManager locMgr; 
	private Context cntx;
	private GpsNotificationListener listener;
	
	private GpsLocationManager(){}
	
	public static GpsLocationManager getGPSLocationManger(){
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
			//calu = GpsLocationCalculator.getGpsLocationCalculator(listener,cntx);
			calu = new GpsLocationCalculator();
			calu.startBGTask(listener, cntx);
			onLocationChanged(locMgr.getLastKnownLocation(provider));
			trackingStarted = true;
			
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
			calu.stopBGTask();
			return true;
		}
		return false;
	}
	
	@Override
	public void onLocationChanged(Location newLocation) {
		if (newLocation != null && newLocation.hasAccuracy()) {
			calu.locationUpdated(newLocation);
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
