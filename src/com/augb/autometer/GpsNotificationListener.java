package com.augb.autometer;

/**
 * This should be implemented by the UI.
 */
public interface GpsNotificationListener {
	void onUpdate(GpsLocation loc);
}