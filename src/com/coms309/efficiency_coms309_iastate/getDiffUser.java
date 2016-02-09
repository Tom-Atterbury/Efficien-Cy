package com.coms309.efficiency_coms309_iastate;

/**
 * getDiffUser
 * 
 * Retrives different users appevents from remote database;
 * 
 * @authors Richard White
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * gitDiffUser
 * 
 * Datasource for a different users data from the remote db.
 * 
 * @authors Richard White
 * 
 */
class getDiffUser extends Service {

	private SQLiteHelper database;
	private String url = "http://10.0.2.2:8080/remoteDBserver/server";
	private String user = "testuser2";
	private String password = "testpassword";
	List<App> result;

	@Override
	public void onCreate() {
		super.onCreate();
		database = new SQLiteHelper(this);
		database = SQLiteHelper.getInstance(this);
		// get users credentials from local database.
		//user = database.getUserName();
		//password = database.getPassword();
		//url = database.getURL();
	}

	public List<AppSummary> getUserData(String name) {
		Log.d("key23", "first");
		remote remoteDB = new remote();
		remoteDB.execute(new String[] { name });
		try {
			Log.d("key23", "before");
			List<AppEvent> cow = new LinkedList<AppEvent>();
			cow = remoteDB.get();
			Log.d("key23", String.valueOf(cow.size()));
			if(cow == null) {
				Log.d("key23", "null");
			}
			List<AppSummary> test = calculateAppSummaries(cow);
			Log.d("key23", String.valueOf(test.size()));
			Log.d("key23", "after");
			return test;
		} catch (Exception e) {
			Log.d("key23", e.toString());
			e.printStackTrace();
		}
		return new LinkedList<AppSummary>();
	}
	
	private List<AppSummary> calculateAppSummaries(List<AppEvent> allEvents) {
		Log.d("calculateAppSummaries()",
				"Num of events: " + String.valueOf(allEvents.size()));

		ArrayList<AppSummary> listOfSummaries = new ArrayList<AppSummary>();
		if (allEvents.isEmpty()) {
			return listOfSummaries;
		}

		Log.d("key23", "test");
		// Go through and calculate total app use time, while culling
		// blank-named apps
		long totalTime = 0;
		int totalProductivity = 0;
		int totalEvents = allEvents.size();
		for (int eventNum = 0; eventNum < totalEvents; eventNum++) {
			// If the name is blank, remove it and subtract 1 from the total
			// number of events in the list
			Log.d("calculateAppSummaries()", "Looking at event number "
					+ eventNum + " of " + (allEvents.size() - 1));

			if (allEvents.get(eventNum) == null) {
				Log.d("calculateAppSummaries()", "Event " + eventNum
						+ " was null");
				allEvents.remove(eventNum);
				totalEvents--;
				eventNum--;
			} else if (allEvents.get(eventNum).toString().equals("")) {
				Log.d("calculateAppSummaries()", "Removing event " + eventNum);
				allEvents.remove(eventNum);
				totalEvents--;
				eventNum--;
			} else if (allEvents.get(eventNum).getDuration() == 0) {
				Log.d("calculateAppSummaries()", "Removing event " + eventNum);
				allEvents.remove(eventNum);
				totalEvents--;
				eventNum--;
			} else {
				long myTime = allEvents.get(eventNum).getEndTime()
						- allEvents.get(eventNum).getStartTime();
				totalTime += myTime;
				// Increase total productivity rating by app's rating * duration
				totalProductivity += new App(allEvents.get(eventNum).toString())
						.getProductivityRating() * myTime;
				Log.d("calculateAppSummaries()",
						"Adding time from event " + eventNum
								+ ", total time is "
								+ String.valueOf(totalTime));
			}
		}
		// Calculate total average productivity; default to 50%
		int avgProductivityRating = 1;
		if (totalTime != 0) {
			avgProductivityRating = (int) (totalProductivity / totalTime) * 100;
		}

		// Initialize the first app summary to contain the information regarding
		// the app corresponding to app event 0
		Log.d("calculateAppSummaries()", "Finding app/making summary for "
				+ allEvents.get(0).toString());
		Log.d("calculateAppSummaries()",
				"Found app for "
						+ new App(allEvents.get(0).toString()).toString());
		AppSummary currentAppSummary = new AppSummary(new App(allEvents.get(0)
				.toString()));

		// For retrieving coordinates
		ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
		Coordinates coordsToAdd = new Coordinates(allEvents.get(0).getStartLongitude(), allEvents.get(0).getStartLatitude());
		coordsList.add(coordsToAdd);

		Log.d("calculateAppSummaries()", "Made app summary");
		currentAppSummary.addTotalTime(allEvents.get(0).getDuration());
		Log.d("calculateAppSummaries()", "Added event time");
		Log.d("calculateAppSummaries()",
				"Current duration for " + currentAppSummary.toString() + " is "
						+ currentAppSummary.getTotalTime());

		// Go through the app events
		for (int eventNum = 1; eventNum < totalEvents; eventNum++) {
			// If the app name is the same, update the duration and add coords
			Log.d("calculateAppSummaries()", "Adding info from event "
					+ eventNum);
			if (allEvents.get(eventNum).toString()
					.equals(currentAppSummary.toString())) {
				currentAppSummary.addTotalTime(allEvents.get(eventNum)
						.getDuration());
				Log.d("calculateAppSummaries()", "Current duration for "
						+ currentAppSummary.toString() + " is "
						+ currentAppSummary.getTotalTime());

				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsList.add(coordsToAdd);
			} else {
				// If the app name is different, add the summary to the list of
				// summaries and repopulate the information
				// But first, set the percentage time
				Log.d("calculateAppSummaries()", "New summary for "
						+ allEvents.get(eventNum).toString() + " @ event "
						+ eventNum);
				Log.d("calculateAppSummaries()", "Percent time: "
						+ (currentAppSummary.getTotalTime() * 100) / totalTime);
				currentAppSummary.setPercentTime((int) ((currentAppSummary
						.getTotalTime() * 100) / totalTime));
				listOfSummaries.add(currentAppSummary);
				Log.d("calculateAppSummaries()", "Old summary "
						+ currentAppSummary.toString() + " with end coords "
						+ coordsToAdd.longitude + "," + coordsToAdd.latitude
						+ " added to list");

				currentAppSummary = new AppSummary(new App(allEvents.get(
						eventNum).toString()));
				currentAppSummary.addTotalTime(allEvents.get(eventNum)
						.getDuration());
				currentAppSummary.addCoordinatesList(coordsList);

				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsList = new ArrayList<Coordinates>();
				coordsList.add(coordsToAdd);
			}
		}
		// Make sure to add the last AppSummary
		currentAppSummary.setPercentTime((int) (currentAppSummary
				.getTotalTime() * 100 / totalTime));
		currentAppSummary.addCoordinatesList(coordsList);
		listOfSummaries.add(currentAppSummary);
		Log.d("calculateAppSummaries()",
				"Old summary " + currentAppSummary.toString()
						+ " with end coords " + coordsToAdd.longitude + ","
						+ coordsToAdd.latitude + " added to list");

		// Output a sorted list of summaries, based on their total times
		Collections.sort(listOfSummaries, new AppSummary.compareTimes());
		Log.d("calculateAppSummaries()", "end");
		return listOfSummaries;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class remote extends AsyncTask<String, Integer, List<AppEvent>> {

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		protected void onPostExecute(List<AppEvent> result1) {
			// result = result1;
		}

		@Override
		protected List<AppEvent> doInBackground(String... params) {
			List<AppEvent> apps = new LinkedList<AppEvent>();
			// only ever recieves one name
			for (String name : params) {
				Log.v("here", name);
				// checks if network is up
				if (true) {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);
					JSONArray jArr = new JSONArray();
					JSONObject json = new JSONObject();
					// send data to server
					Log.v("here2", name);
					try {
						json.put("username", user);
						json.put("password", password);
						json.put("condition", "getUserData");
						json.put("length", "0");
						json.put("follow", name);
						jArr.put(json);
					} catch (Exception e) {
						Log.e("rj", e.toString());
					}
					// get responce
					try {
						httppost.setEntity(new StringEntity(jArr.toString()));
						HttpResponse resp = httpclient.execute(httppost);
						HttpEntity ent = resp.getEntity();
						jArr = new JSONArray(EntityUtils.toString(ent));
					} catch (Exception e) {
						Log.e("rj", e.toString());
					}
					AppEvent event = new AppEvent("");
					json = new JSONObject();
					for (int i = 0; i < jArr.length(); i++) {
						try {
							json = jArr.getJSONObject(i);
							event = new AppEvent(json.getString("name"),
									json.getLong("starttime"),
									json.getLong("endtime"),
									(float) json.getDouble("startlong"),
									(float) json.getDouble("startlat"),
									(float) json.getDouble("endlong"),
									(float) json.getDouble("endlat"),
									json.getString("type"),
									json.getString("location"));
							Log.d("json", event.toString());
							apps.add(event);
						} catch (Exception e) {
							Log.e("RJ", e.toString());
						}
					}
				}

			}
			return apps;
		}

		/**
		 * haveNetworkConnection
		 * 
		 * Simply checks if the network is up.
		 * 
		 * @authors Richard White
		 * 
		 */
		private boolean haveNetworkConnection() {
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
			return haveConnectedWifi || haveConnectedMobile;
		}
	}

}
