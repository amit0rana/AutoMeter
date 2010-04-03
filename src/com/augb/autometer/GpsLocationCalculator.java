package com.augb.autometer;

import android.location.Location;
//import android.location.LocationManager;
import android.os.SystemClock;

public class GpsLocationCalculator {
	private static final float PAUSE_THRESHOLD_METERS = 5;

	//private LocationManager locationManager;
	private Location lastNotifiedLocation;
	private Location currentLocation;

	private long waitingTime;
	private double totalDistance;

	private boolean isCurrentlyPaused;
	private long lastPauseUpdateTime;

	// When OS updates
	void locationUpdate(Location locationUpdate) {
	  double distance = currentLocation.distanceTo(locationUpdate);

		// Update the distance
		totalDistance += distance;

		long currentTime =  SystemClock.currentThreadTimeMillis();
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
				waitingTime += (currentTime - currentLocation.getTime());
			}
		}
		currentLocation = locationUpdate;
	}

	// When timer kicks in
	void onThreadTimer() {
		long currentTime = SystemClock.currentThreadTimeMillis();

		// If we haven't received any updates, then treat this as a pause
		if (lastNotifiedLocation == currentLocation) {
			waitingTime += (currentTime - lastNotifiedLocation.getTime());
			lastPauseUpdateTime = currentTime;
			isCurrentlyPaused = true;
		}

		lastNotifiedLocation = currentLocation;

	}

}