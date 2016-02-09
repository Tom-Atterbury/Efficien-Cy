package com.coms309.efficiency_coms309_iastate;

/**
 * Coordinates
 * 
 * Contains coordinate values used by AppSummary class, which will relay information
 * to/from the database and EfficienCy
 * 
 * @author James Saylor
 * 
 */

public class Coordinates {
	public float longitude;
	public float latitude;

	
	public Coordinates(float longitude, float latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
}