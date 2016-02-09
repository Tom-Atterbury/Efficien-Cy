package com.coms309.efficiency_coms309_iastate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

/**
 * getDiffUser
 * 
 * Uptates remote databse with users appevents.
 * 
 * @authors Richard White
 * 
 */

public class RemoteHelperDB extends Service {

	private Timer timer;
	public static SQLiteHelper database;
	private String url = "http://10.0.2.2:8080/remoteDBserver/server";
	private String user = "testuser2";
	private String password = "testpassword";
	private int defaultIntrival = 86400; // one day
	private int currentIntrival = 86400;
	ActivityManager manager;

	/**
	 * mytask
	 * 
	 * Task updates users info to remote database. It ajdust itself if it fails
	 * to connect.
	 * 
	 * @authors Richard White
	 * 
	 */
	private class mytask extends TimerTask {
		@Override
		public void run() {
			// check if connect to network
			if (haveNetworkConnection()) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				JSONArray jArr = new JSONArray();
				JSONObject json = new JSONObject();
				long newest = 1;
				// send data to server to get last update time.
				try {
					json.put("username", user);
					json.put("password", password);
					json.put("condition", "newest");
					json.put("length", "0");
					jArr.put(json);
				} catch (Exception e) {
					Log.e("rj", e.toString());
				}

				// revieve the upated time.
				try {
					httppost.setEntity(new StringEntity(jArr.toString()));
					HttpResponse resp = httpclient.execute(httppost);
					HttpEntity ent = resp.getEntity();
					String result = EntityUtils.toString(ent);
					newest = Long.parseLong(result);
				} catch (Exception e) {
					Log.e("rj", e.toString());
				}

				// get all appevents newer than the newest on the remote db.
				ArrayList<AppEvent> list = database.getAppEvents(newest + 1);
				jArr = new JSONArray();
				json = new JSONObject();

				// make first json item a identifier for users info.
				try {
					json.put("username", user);
					json.put("password", password);
					json.put("condition", "update");
					json.put("length", String.valueOf(list.size()));
					jArr.put(json);
				} catch (Exception e) {
					Log.e("rj", e.toString());
				}

				// loop over list of appevents and make them json objects and
				// add them to the json array.
				Iterator<AppEvent> it = list.iterator();
				while (it.hasNext()) {
					AppEvent event = it.next();
					json = event.getJSON();
					jArr.put(json);
				}

				// send info to server.
				try {
					httppost.setEntity(new StringEntity(jArr.toString()));
					HttpResponse resp = httpclient.execute(httppost);
					HttpEntity ent = resp.getEntity();
				} catch (Exception e) {
					Log.e("rj", e.toString());
				}

				currentIntrival = defaultIntrival;
			}
			// reduce the time to next update by half it it failed
			else {
				currentIntrival /= 2;
				// 60 seconds is minimus checking intrival
				if (currentIntrival < 60) {
					currentIntrival = 60;
				}
			}
			// rescudle the task.
			timer.cancel();
			timer = new Timer("update remote database");
			TimerTask task = new mytask();
			timer.schedule(task, 1000 * currentIntrival, currentIntrival * 1000);
		}
	};

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

	@Override
	public void onCreate() {
		super.onCreate();

		database = new SQLiteHelper(this);
		database = SQLiteHelper.getInstance(this);

		manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

		// update user info
		//user = database.getUserName();
		//password = database.getPassword();
		//url += database.getURL();
		// start task to update info.
		TimerTask task = new mytask();
		timer = new Timer("update remote database");
		timer.schedule(task, 1000, defaultIntrival * 1000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
	}
}
