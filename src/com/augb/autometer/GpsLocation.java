/**
 * 
 */
package com.augb.autometer;

/**
 * @author SandeepChoudhary
 *
 */
public class GpsLocation {

	  private double lat;
	  private double lon;
	  private double locAccuracy;
	  private double speed;
	  private double bearing;
	  private long lastLocationUpdateDT;
	  private long totalWaitingDT;
	  private long totalTripDT;
	  private double totalDistance;
	 
	  
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}
	/**
	 * @param locAccuracy the locAccuracy to set
	 */
	public void setLocAccuracy(double locAccuracy) {
		this.locAccuracy = locAccuracy;
	}
	/**
	 * @return the locAccuracy
	 */
	public double getLocAccuracy() {
		return locAccuracy;
	}
	/**
	 * @param lastLocationUpdateDT the lastLocationUpdateDT to set
	 */
	public void setLastLocationUpdateDT(long lastLocationUpdateDT) {
		this.lastLocationUpdateDT = lastLocationUpdateDT;
	}
	/**
	 * @return the lastLocationUpdateDT
	 */
	public long getLastLocationUpdateDT() {
		return lastLocationUpdateDT;
	}
	/**
	 * @param waitingDT the waitingDT to set
	 */
	public void setTotalWaitingDT(long waitingDT) {
		this.totalWaitingDT = waitingDT;
	}
	/**
	 * @return the waitingDT
	 */
	public long getTotalWaitingDT() {
		return totalWaitingDT;
	}
	/**
	 * @param totalTripDT the totalTripDT to set
	 */
	public void setTotalTripDT(long totalTripDT) {
		this.totalTripDT = totalTripDT;
	}
	/**
	 * @return the totalTripDT
	 */
	public long getTotalTripDT() {
		return totalTripDT;
	}
	/**
	 * @param totalDistance the totalDistance to set
	 */
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
	/**
	 * @return the totalDistance
	 */
	public double getTotalDistance() {
		return totalDistance;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @param bearing the bearing to set
	 */
	public void setBearing(double bearing) {
		this.bearing = bearing;
	}
	/**
	 * @return the bearing
	 */
	public double getBearing() {
		return bearing;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return lat + " : " + lon + " : " + locAccuracy + " : " + totalDistance + " : " + totalTripDT + " : " + totalWaitingDT;
	}
}
