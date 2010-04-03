package com.augb.autometer;

import java.util.Date;

import android.content.Context;
import android.location.Location;

public class GpsLocationCalculator {
	private static final float PAUSE_THRESHOLD_METERS = 5;

	private volatile static GpsLocationCalculator mySelf;
	private volatile Context ctx;
	private volatile GpsNotificationListener listener;
	private volatile Location lastNotifiedLocation;
	private volatile Location currentLocation;
	private volatile boolean exit;
	private volatile long tripStartedDT;
	private volatile long lastLocUpdateDT;
	private volatile long lastNotifiedDT;
	private volatile long waitingTime;
	private volatile double totalDistance;
	private volatile boolean isCurrentlyPaused;
	private volatile long lastPauseUpdateTime;
	private volatile Thread bgThread;
	private volatile RouteData rd;
	
	private GpsLocationCalculator(){}

	public static GpsLocationCalculator getGpsLocationCalculator(GpsNotificationListener listenerObj, Context ctxObj){
		if(mySelf == null){
			mySelf = new GpsLocationCalculator();
			mySelf.startBGTask(listenerObj, ctxObj);
		}
		return mySelf;
		
	}
	
	// When OS updates
	synchronized void locationUpdated(Location locationUpdate) {
		if(currentLocation == null)
			currentLocation = locationUpdate;
	  double distance = currentLocation.distanceTo(locationUpdate);

		// Update the distance
		totalDistance += distance;

		long currentTime =  (new Date()).getTime();
		// If the most recent location from gps is far enough from the last
		// one, then we have moved since then.
		if (distance > PAUSE_THRESHOLD_METERS) {
			if (isCurrentlyPaused) {
				// We used to be paused, cancel that.
				isCurrentlyPaused = false;
				waitingTime += (currentTime  - lastPauseUpdateTime);
			}
		} else {
			// We are paused.  Update waiting time accordingly.
			if (isCurrentlyPaused) {
				waitingTime += (currentTime - lastPauseUpdateTime);
				lastPauseUpdateTime = currentTime;
			} else {
				isCurrentlyPaused = true;
				lastPauseUpdateTime = currentTime;
				waitingTime += (currentTime - lastLocUpdateDT);
			}
		}
		lastLocUpdateDT = (new Date().getTime());
		currentLocation = locationUpdate;
	}

	// When timer kicks in
	synchronized GpsLocation updateCalculation() {
		long currentTime = (new Date()).getTime();

		// If we haven't received any updates, then treat this as a pause
		if (lastNotifiedLocation == currentLocation) {
			waitingTime += (currentTime - lastNotifiedDT);
			lastPauseUpdateTime = currentTime;
			isCurrentlyPaused = true;
		}

		lastNotifiedLocation = currentLocation;
		lastNotifiedDT = new Date().getTime();
		GpsLocation loc = new GpsLocation();
		//as it is
		loc.setLastLocationUpdateDT(currentLocation.getTime());
		loc.setLat(currentLocation.getLatitude());
		loc.setLon(currentLocation.getLongitude());
		loc.setLocAccuracy(currentLocation.getAccuracy());
		loc.setSpeed(currentLocation.getSpeed());
		loc.setBearing(currentLocation.getBearing());
		//calculated
		loc.setTotalDistance(totalDistance);
		loc.setTotalTripDT((new Date().getTime() - tripStartedDT));
		loc.setTotalWaitingDT(waitingTime);
		return loc;
	}
	
	public void stopBGTask(){
		exit = true;
		
		try {
			bgThread.interrupt();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void startBGTask(GpsNotificationListener listenerObj,Context ctxObj){
		listener = listenerObj;
		ctx = ctxObj;
		Date now = new Date();
		tripStartedDT = now.getTime();
	    rd = new RouteData(ctx);
		rd.createTrip(tripStartedDT, 0.0, 0.0);
		//create and run the thread
	    bgThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!exit){
					GpsLocation loc =  updateCalculation();
					if(listener != null){
						listener.onUpdate(loc);
					}
					//TO DO : send to storage
					rd.createPositionInfo(loc);
					loc = null;
					
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		});
		bgThread.start();
	}

}