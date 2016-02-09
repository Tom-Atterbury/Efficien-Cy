package com.coms309.efficiency_coms309_iastate;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * AppEvent
 * 
 * Contains information regarding the AppEvent class, which will relay
 * information to/from the database and EfficienCy
 * 
 * @author James Saylor
 * 
 */

// TODO: null values passed in

public class AppEvent {
	// Add CPU
	// Check into calls to determine if you're moving quickly/freeway speeds
	// Includes potentially useful information regarding map
	// Tap into map's bank of location names (what's nearby, to determine )
	// Geolocation service
	// Break app event table: locations, times
	// DB theory: use incremented values (faster), or hash/GUID (safer)
	private String appName;
	private long startTime;
	private long endTime;
	private long duration;
	// May include start/end locations
	private float startLongitude;
	private float startLatitude;
	private float endLongitude;
	private float endLatitude;
	private ArrayList<String> locationsList;
	private float totalDisplacement;
	private float absoluteDistance;
	// This is currently set to about 1 typical city block
	private float wanderingEventCutoff = (float) 0.001;
	// This is currently set to about 1/2 of a typical city block
	private float movingEventCutoff = (float) 0.0005;
	// This is currently set to about 5 city blocks
	private float travelEventCutoff = (float) 0.005;
	// Currently set to moving about 50 mph;
	private float highSpeedTravelCutoff = (float) 0.0021;
	private String movingEventType;
	private String stationaryEvent = "location";
	private String wanderingEvent = "wandering";
	private String movingEvent = "moving";
	private String travelEvent = "travel";
	private String highSpeedEvent = "highSpeed";

	// Can mark as moving or location event, based on displacement
	// If it's a moving event, mark it as walking/driving/high speed
	// If it's a moving event, mark it as finer location

	// Creation methods
	// If we only know app name
	public AppEvent(String newAppName) {
		appName = newAppName;
		startTime = System.currentTimeMillis() / 1000L;
		endTime = System.currentTimeMillis() / 1000L;
		duration = 0;
		startLongitude = 0;
		startLatitude = 0;
		endLongitude = 0;
		endLatitude = 0;
		totalDisplacement = 0;
		absoluteDistance = 0;
		locationsList = new ArrayList<String>();
		movingEventType = stationaryEvent;
	}

	// If we only know app name and location
	public AppEvent(String newAppName, float newLongitude, float newLatitude) {
		appName = newAppName;
		startTime = System.currentTimeMillis() / 1000L;
		endTime = System.currentTimeMillis() / 1000L;
		duration = 0;
		startLongitude = 0;
		startLatitude = 0;
		endLongitude = 0;
		endLatitude = 0;
		totalDisplacement = 0;
		absoluteDistance = 0;
		locationsList = new ArrayList<String>();
		movingEventType = stationaryEvent;
	}

	// If we only know app name and time used
	public AppEvent(String newAppName, long newStartTime, long newEndTime) {
		appName = newAppName;
		startTime = newStartTime;
		endTime = newEndTime;
		duration = 0;
		startLongitude = 0;
		startLatitude = 0;
		endLongitude = 0;
		endLatitude = 0;
		totalDisplacement = 0;
		absoluteDistance = 0;
		locationsList = new ArrayList<String>();
		movingEventType = stationaryEvent;
	}

	// If we know app name, time used, and location
	public AppEvent(String newAppName, long newStartTime, long newEndTime,
			float newLongitude, float newLatitude) {
		appName = newAppName;
		startTime = newStartTime;
		endTime = newEndTime;
		duration = 0;
		startLongitude = newLongitude;
		startLatitude = newLatitude;
		endLongitude = newLongitude;
		endLatitude = newLatitude;
		totalDisplacement = 0;
		absoluteDistance = 0;
		locationsList = new ArrayList<String>();
		movingEventType = stationaryEvent;
	}

	// If we know app name, time used, locations, event type, and location
	// These are the things known by the local database
	public AppEvent(String newAppName, long newStartTime, long newEndTime,
			float newStartLongitude, float newStartLatitude,
			float newEndLongitude, float newEndLatitude, String newEventType,
			String newLocation) {
		appName = newAppName;
		startTime = newStartTime;
		endTime = newEndTime;
		duration = endTime - startTime;
		startLongitude = newStartLongitude;
		startLatitude = newStartLatitude;
		endLongitude = newEndLongitude;
		endLatitude = newEndLatitude;
		totalDisplacement = (float) Math.sqrt(Math.pow(startLatitude
				- endLatitude, 2)
				+ Math.pow(startLongitude - endLongitude, 2));
		absoluteDistance = (float) Math.sqrt(Math.pow(startLatitude
				- endLatitude, 2)
				+ Math.pow(startLongitude - endLongitude, 2));
		movingEventType = newEventType;
		locationsList = new ArrayList<String>();
		locationsList.add(newLocation);
	}

	// Resetter method to repopulate the always-running app event
	public void resetAppEvent(String newAppName, float newStartLongitude,
			float newStartLatitude) {
		appName = newAppName;
		startTime = System.currentTimeMillis() / 1000L;
		endTime = System.currentTimeMillis() / 1000L;
		duration = endTime - startTime;
		startLongitude = newStartLongitude;
		startLatitude = newStartLatitude;
		endLongitude = newStartLongitude;
		endLatitude = newStartLatitude;
		totalDisplacement = 0;
		absoluteDistance = 0;
		movingEventType = stationaryEvent;
		locationsList = new ArrayList<String>();
	}

	public void resetAppEvent(String newAppName) {
		appName = newAppName;
		startTime = System.currentTimeMillis() / 1000L;
		endTime = System.currentTimeMillis() / 1000L;
		duration = endTime - startTime;
		startLongitude = endLongitude;
		startLatitude = endLatitude;
		totalDisplacement = 0;
		absoluteDistance = 0;
		movingEventType = stationaryEvent;
		locationsList = new ArrayList<String>();
	}

	// Getter methods for duration, start/end times, coordinates, and name
	public long getDuration() {
		return duration;
	}

	public float getStartLongitude() {
		return startLongitude;
	}

	public float getStartLatitude() {
		return startLatitude;
	}

	public float getEndLongitude() {
		return endLongitude;
	}

	public float getEndLatitude() {
		return endLatitude;
	}

	@Override
	public String toString() {
		return appName;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public float getDisplacement() {
		return totalDisplacement;
	}

	public float getDistance() {
		return absoluteDistance;
	}

	public ArrayList<String> getLocationList() {
		return locationsList;
	}

	public String eventType() {
		return movingEventType;
	}

	// Setter methods
	public void setStartLongitude(float newLongitude) {
		startLongitude = newLongitude;
	}

	public void setStartLatitude(float newLatitude) {
		startLatitude = newLatitude;
	}

	public void setEndLongitude(float newLongitude) {
		endLongitude = newLongitude;
	}

	public void setEndLatitude(float newLatitude) {
		endLatitude = newLatitude;
	}

	// Must be called before we update our longitude and latitude
	public void updateDisplacement(float newLong, float newLat) {
		totalDisplacement += Math.abs(newLong - endLongitude)
				+ Math.abs(newLat - endLatitude);
	}

	public void updateAbsoluteDistance(float newLong, float newLat) {
		absoluteDistance = (float) Math.sqrt((newLong - endLongitude)
				* (newLong - endLongitude) + (newLat - endLatitude)
				* (newLat - endLatitude));
	}

	/*
	 * This combines:latitude/longitude updates,distance and displacement
	 * updates,type of moving event update
	 */
	public void updateLocationInfo(float newLongitude, float newLatitude) {
		// First check if we've crossed the threshold for how fast we can move
		// before considering it to be high speed travel
		/*
		 * Not used at the moment if(Math.abs(newLongitude - endLongitude) +
		 * Math.abs(newLatitude - endLatitude) >= highSpeedTravelCutoff) {
		 * 
		 * movingEventType = highSpeedEvent; }
		 */
		updateDisplacement(newLongitude, newLatitude);
		updateAbsoluteDistance(newLongitude, newLatitude);
		// Compare displacement and distance to determine type of moving event
		// Heirarchy is highSpeed > travel > moving > wandering > stationary
		/*
		 * Not used currently: if(!movingEventType.equals(highSpeedEvent)) {
		 * //See if it should be travel, moving, wandering, or remain stationary
		 * if(absoluteDistance > travelEventCutoff) { movingEventType =
		 * travelEvent; } else if(absoluteDistance > movingEventCutoff) {
		 * movingEventType = movingEvent; } else if(totalDisplacement >
		 * wanderingEventCutoff) { movingEventType = wanderingEvent; } }
		 */
		setEndLongitude(newLongitude);
		setEndLatitude(newLatitude);

		// TODO: Finally, compare location names to see if we've crossed any
		// boundaries
	}

	// TODO: Add calendar capability
	public void updateEndTime() {
		endTime = System.currentTimeMillis() / 1000L;
		duration = endTime - startTime;
	}

	public void updateEndTime(long newEndTime) {
		endTime = newEndTime;
		duration = endTime - startTime;
	}

	public void setStartAndEnd(long newStartTime, long newEndTime) {
		startTime = newStartTime;
		endTime = newEndTime;
		duration = endTime - startTime;
	}

	public void setLocations(ArrayList<String> newLocations) {
		locationsList = newLocations;
	}
	
	public void setName(String newName) {
		this.appName = newName;
	}

	public JSONObject getJSON() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("name", appName);
			jsonObj.put("starttime", String.valueOf(startTime));
			jsonObj.put("endtime", String.valueOf(endTime));
			jsonObj.put("startlong", String.valueOf(startLongitude));
			jsonObj.put("startlat", String.valueOf(startLatitude));
			jsonObj.put("endlong", String.valueOf(endLongitude));
			jsonObj.put("endlat", String.valueOf(endLatitude));
			jsonObj.put("location", locationsList.get(0));
			jsonObj.put("type", movingEventType);

		} catch (Exception E) {
		}
		return jsonObj;
	}
}
