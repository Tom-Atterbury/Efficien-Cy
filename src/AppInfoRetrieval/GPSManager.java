package AppInfoRetrieval;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 
 * @author Tom Atterbury
 * 
 */
public class GPSManager implements LocationListener {

	private Context con;
	private LocationManager locman;
	private final float DEFAULT_LATITUDE = (float) 42.028156;
	private final float DEFAULT_LONGITUDE = (float) -93.649955;
	private boolean canGetLocation;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private Location oldLocation;

	/**
	 * GPSManager Constructor
	 * 
	 * @param that
	 *            Context of the calling Application
	 */
	public GPSManager(Context that) {
		con = that;
		locman = (LocationManager) con
				.getSystemService(Context.LOCATION_SERVICE);
		oldLocation = getLocation();
	}

	/**
	 * 
	 * @return
	 */
	public Location getLocation() {
		try {
			// getting GPS status
			isGPSEnabled = locman
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locman
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// no network provider is enabled
				canGetLocation = false;
			} else {
				canGetLocation = true;

				if (isGPSEnabled) {
					return getLocationFromGPS();
				}

				if (isNetworkEnabled) {
					return getLocationFromNetwork();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// No location data available
		return null;

	}

	/**
	 * Use the network provider to get the current latitude and longitude, if
	 * available
	 * 
	 * @return Location
	 */
	private Location getLocationFromNetwork() {
		// Get lat/long using Network Services
		Location netLocation = null;
		locman.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
				1000, this);
		if (locman != null) {
			netLocation = locman
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		if (netLocation != null)
			return netLocation;

		return oldLocation;
	}

	/**
	 * Get the location data from the GPS
	 * 
	 * @return Location object with location information from GPS
	 */
	private Location getLocationFromGPS() {
		// Get lat/long using GPS Services
		Location gpsLocation = null;
		locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000,
				this);
		if (locman != null) {
			gpsLocation = locman
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		if (gpsLocation != null)
			return gpsLocation;

		return oldLocation;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locman != null) {
			locman.removeUpdates(this);
		}
	}

	/**
	 * Function to update latitude
	 * */
	public float getLatitude() {
		// Update with new location data
		Location newLocation = getLocation();

		if (newLocation != null) {
			// Compare the new location information to get best data
			if (oldLocation != null) {
				long newLastUpdated = newLocation.getElapsedRealtimeNanos();
				long oldLastUpdated = oldLocation.getElapsedRealtimeNanos();
				if (newLastUpdated > oldLastUpdated) {
					oldLocation = newLocation;
				}
			} else {
				oldLocation = newLocation;
			}

			// Get the latitude
			return (float) oldLocation.getLatitude();

		} else if (oldLocation != null) {
			// Use old location data, better than nothing
			return (float) oldLocation.getLatitude();
		} else {
			// Use default latitude, literally the worst possible outcome
			return DEFAULT_LATITUDE;
		}

	}

	/**
	 * Function to update longitude
	 * */
	public float getLongitude() {
		// Update with new location data
		Location newLocation = getLocation();

		if (newLocation != null) {
			// Compare the new location information to get best data
			if (oldLocation != null) {
				long newLastUpdated = newLocation.getElapsedRealtimeNanos();
				long oldLastUpdated = oldLocation.getElapsedRealtimeNanos();
				if (newLastUpdated > oldLastUpdated) {
					oldLocation = newLocation;
				}
			} else {
				oldLocation = newLocation;
			}

			// Get the latitude
			return (float) oldLocation.getLongitude();

		} else if (oldLocation != null) {
			// Use old location data, better than nothing
			return (float) oldLocation.getLongitude();
		} else {
			// Use default latitude, literally the worst possible outcome
			return DEFAULT_LONGITUDE;
		}
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return canGetLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
