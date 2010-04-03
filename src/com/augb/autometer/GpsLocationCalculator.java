package com.augb.autometer;

import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class GpsLocationCalculator {
	private static final float PAUSE_THRESHOLD_METERS = 5;

	//private volatile static GpsLocationCalculator mySelf;
	private volatile Context ctx;
	private volatile GpsNotificationListener listener;
	private volatile Location lastNotifiedLocation;
	private volatile Location currentLocation;
	private volatile boolean exit;
	private volatile long tripStartedDT = 0;
	private volatile long lastLocUpdateDT = 0;
	private volatile long lastNotifiedDT = 0;
	private volatile long waitingTime = 0;
	private volatile double totalDistance = 0.0;
	private volatile boolean isCurrentlyPaused;
	private volatile long lastPauseUpdateTime = 0;
	private volatile Thread bgThread;
	private volatile RouteData rd;

	public GpsLocationCalculator() {
	}

	/*public static GpsLocationCalculator getGpsLocationCalculator(
			GpsNotificationListener listenerObj, Context ctxObj) {
		if (mySelf == null) {
			mySelf = new GpsLocationCalculator();
		}
		return mySelf;

	}*/

	// When OS updates
	synchronized void locationUpdated(Location locationUpdate) {
		if (currentLocation == null)
			currentLocation = locationUpdate;
		double distance = currentLocation.distanceTo(locationUpdate);

		// Update the distance
		totalDistance += distance;
		Log.d("AUTO", "totalDistance : " + totalDistance);
		long currentTime = (new Date()).getTime();
		// If the most recent location from gps is far enough from the last
		// one, then we have moved since then.
		if (distance > PAUSE_THRESHOLD_METERS) {
			if (isCurrentlyPaused) {
				Log.d("AUTO", "started moving, isCurrentlyPaused");
				// We used to be paused, cancel that.
				isCurrentlyPaused = false;
				waitingTime += (currentTime - lastPauseUpdateTime);
			}
		} else {
			// We are paused. Update waiting time accordingly.
			if (isCurrentlyPaused) {
				Log.d("AUTO", "continue to pause");
				waitingTime += (currentTime - lastPauseUpdateTime);
				lastPauseUpdateTime = currentTime;
			} else {
				Log.d("AUTO", "start pause");
				isCurrentlyPaused = true;
				lastPauseUpdateTime = currentTime;
				if (lastLocUpdateDT > 0) {
					waitingTime += (currentTime - lastLocUpdateDT);
				}
			}
		}
		lastLocUpdateDT = (new Date().getTime());
		currentLocation = locationUpdate;
		Log.d("AUTO", "waitingTime : " + waitingTime);
	}

	// When timer kicks in
	synchronized GpsLocation updateCalculation() {
		GpsLocation loc = new GpsLocation();
		if (currentLocation != null) {
			long currentTime = (new Date()).getTime();
			Log.d("AUTO", "Timer kicks in");

			// If we haven't received any updates, then treat this as a pause
			if (lastNotifiedLocation == currentLocation) {
				Log.d("AUTO", "No update since last timer");
				waitingTime += (currentTime - lastNotifiedDT);
				lastPauseUpdateTime = currentTime;
				isCurrentlyPaused = true;
			}

			lastNotifiedLocation = currentLocation;
			lastNotifiedDT = new Date().getTime();

			// as it is
			loc.setLastLocationUpdateDT(currentLocation.getTime());
			loc.setLat(currentLocation.getLatitude());
			loc.setLon(currentLocation.getLongitude());
			loc.setLocAccuracy(currentLocation.getAccuracy());
			loc.setSpeed(currentLocation.getSpeed());
			loc.setBearing(currentLocation.getBearing());
			// calculated
			loc.setTotalDistance(totalDistance);
			loc.setTotalTripDT((new Date().getTime() - tripStartedDT));
			loc.setTotalWaitingDT(waitingTime);
			Log.d("AUTO", "On timer update " + loc);
		}
		return loc;

	}

	public void stopBGTask() {
		exit = true;

		try {
			bgThread.interrupt();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void startBGTask(GpsNotificationListener listenerObj, Context ctxObj) {
		listener = listenerObj;
		ctx = ctxObj;
		exit = false;
		Date now = new Date();
		tripStartedDT = now.getTime();
		rd = new RouteData(ctx);
		rd.createTrip(tripStartedDT, 0.0, 0.0);
		// create and run the thread
		bgThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d("AUTO", "Thread started :e " + exit);
				while (!exit) {
					GpsLocation loc = updateCalculation();
					if (listener != null) {
						listener.onUpdate(loc);
					}
					// TO DO : send to storage
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