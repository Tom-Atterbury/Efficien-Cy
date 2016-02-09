package com.coms309.efficiency_coms309_iastate;

import java.util.Timer;
import java.util.TimerTask;

import AppInfoRetrieval.GPSManager;
import AppInfoRetrieval.TopApp;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Main tracking process, sets connection to database and sends the top
 * application to the database every 5 seconds.
 * 
 * @author Richard White, Tom Atterbury
 * 
 */
public class Tracking extends Service {

	private Timer timer;
	public static SQLiteHelper database;
	ActivityManager manager;
	GPSManager GPS;
	private TopApp topApp;
	private static AppEvent event;

	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// Gets top app information and sends it to the database
			App currentTopApp = topApp.getTopApp();
			database.addApp(currentTopApp);
			database.updateAppEvent(event, currentTopApp.toString(), GPS);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		database = new SQLiteHelper(this);
		database = SQLiteHelper.getInstance(this);

		manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

		GPS = new GPSManager(this);

		// Instantiate a TopApp object
		topApp = new TopApp(this);

		// Instantiate an AppEvent object
		event = new AppEvent("");
		task.run();

		int numSecPoll = 5;
		timer = new Timer("timer-test");
		timer.schedule(task, 1000, numSecPoll * 1000);
	}

	/**
	 * Force a top app retrieval
	 */
	public void updateTracker() {
		task.run();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO
	}

}
