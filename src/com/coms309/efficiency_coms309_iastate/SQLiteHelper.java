package com.coms309.efficiency_coms309_iastate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import AppInfoRetrieval.GPSManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//import com.coms309.efficiency_coms309_iastate.SQLiteContract.AppEntries;

/**
 * SQLiteHelper
 * 
 * Assists with database calls for our program -includes database creation and
 * upgrading
 * 
 * @authors James Saylor, Richard White, Tom Atterbury
 * 
 */

// TODO: null values passed in?
public class SQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Table Name
	private static final String DATABASE_NAME = "App.db";
	private static final String APP_TABLE_NAME = "AppTable";
	private static final String SETTINGS_TABLE = "settingsTable";
	private static final String APP_USE_TABLE = "AppUseTable";

	// Database field names for apps
	public static final String COLUMN_APP_NAME = "appName";
	public static final String COLUMN_PRODUCTIVITY = "productivityRating";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_ICON = "icon";
	public static final String COLUMN_DESCRIPTION = "description";
	private static final String[] COLUMNS = { COLUMN_APP_NAME,
			COLUMN_PRODUCTIVITY, COLUMN_CATEGORY, COLUMN_ICON,
			COLUMN_DESCRIPTION };

	// Database field names for settings
	public static final String COLUMN_USER_NAME = "userName";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_LOGGING = "logging";
	private static final String[] SETTINGSCOLUMNS = { COLUMN_USER_NAME,
			COLUMN_PASSWORD, COLUMN_URL, COLUMN_LOGGING };

	// Database field names for app use table
	public static final String COLUMN_START_TIME = "appStartTime";
	public static final String COLUMN_END_TIME = "appEndTime";
	public static final String COLUMN_START_LATITUDE = "appStartLatitude";
	public static final String COLUMN_START_LONGITUDE = "appStartLongitude";
	public static final String COLUMN_END_LATITUDE = "appEndLatitude";
	public static final String COLUMN_END_LONGITUDE = "appEndLongitude";
	public static final String COLUMN_EVENT_TYPE = "appEventType";
	public static final String COLUMN_EVENT_LOCATION = "appEventLocation";

	// Default timeframe for database queries - initial default is day
	public static final long defaultTimeframe = 86400;

	private static SQLiteHelper instance = null;
	private Context mCtx;

	// SQL statements to create and delete app table
	String CREATE_APP_TABLE = "CREATE TABLE if not exists " + APP_TABLE_NAME
			+ " (" + COLUMN_APP_NAME + " TEXT PRIMARY KEY, "
			+ COLUMN_PRODUCTIVITY + " INTEGER, " + COLUMN_CATEGORY + " TEXT,"
			+ COLUMN_ICON + " BLOB, " + COLUMN_DESCRIPTION + " TEXT" + " )";
	String DELETE_APP_TABLE = "DROP TABLE IF EXISTS " + APP_TABLE_NAME;

	// SQL statements to create and delete settings table
	String CREATE_SETTINGS_TABLE = "CREATE TABLE if not exists "
			+ SETTINGS_TABLE + " (" + COLUMN_USER_NAME + " TEXT PRIMARY KEY, "
			+ COLUMN_PASSWORD + " TEXT, " + COLUMN_URL + " TEXT, "
			+ COLUMN_LOGGING + " TEXT" + "LASTUP" + " INTEGER )";
	String DELETE_SETTINGS_TABLE = "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

	// SQL statements to create and delete app time table
	String CREATE_APP_USE_TABLE = "CREATE TABLE if not exists " + APP_USE_TABLE
			+ " (" + COLUMN_APP_NAME + " TEXT, " + COLUMN_START_TIME
			+ " REAL PRIMARY KEY, " + COLUMN_END_TIME + " REAL, "
			+ COLUMN_START_LATITUDE + " REAL, " + COLUMN_START_LONGITUDE
			+ " REAL, " + COLUMN_END_LATITUDE + " REAL, "
			+ COLUMN_END_LONGITUDE + " REAL, " + COLUMN_EVENT_TYPE + " TEXT, "
			+ COLUMN_EVENT_LOCATION + " TEXT )";
	String DELETE_APP_USE_TABLE = "DROP TABLE IF EXISTS " + APP_USE_TABLE;

	public static SQLiteHelper getInstance(Context ctx) {
		/**
		 * use the application context as suggested by CommonsWare. this will
		 * ensure that you dont accidentally leak an Activitys context (see this
		 * article for more information:
		 * http://developer.android.com/resources/articles
		 * /avoiding-memory-leaks.html)
		 */
		if (instance == null) {
			instance = new SQLiteHelper(ctx.getApplicationContext());
		}
		return instance;
	}

	// Constructor
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mCtx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create app table
		db.execSQL(CREATE_APP_TABLE);
		// create settings table
		db.execSQL(CREATE_SETTINGS_TABLE);
		// create app use table
		db.execSQL(CREATE_APP_USE_TABLE);
	}

	// Currently, upgrading will drop the tables
	// This will have to be dealt with at a later date
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older app table if existed
		db.execSQL(DELETE_APP_TABLE);

		// Drop older settings table if existed
		db.execSQL(DELETE_SETTINGS_TABLE);

		// Drop older app use table if existed
		db.execSQL(DELETE_APP_USE_TABLE);

		// create fresh app table
		this.onCreate(db);
	}

	// Method to determine which version our database is currently
	public int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	public long setSettings(Settings settings) {
		// for logging
		Log.d("addApp", settings.toString());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_NAME, settings.getUserName());
		values.put(COLUMN_PASSWORD, settings.getPassword());
		values.put(COLUMN_URL, "");
		values.put(COLUMN_LOGGING, settings.isLogging());

		// 3. insert, and remember primary key value to return it
		long newRowId;
		newRowId = db.insert(SETTINGS_TABLE, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values

		/*
		 * // 4. close db.close();
		 */
		return newRowId;
	}

	public Settings getSettings() {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();

		// 2. build query
		Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE, null);

		// 3. if we got results get the first one
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		// 4. build App object
		Settings settigns = new Settings(cursor.getString(0), // username
				cursor.getString(1), // password
				cursor.getString(2)); // logging
		db.close();
		return settigns;
	}

	public long addApp(App app) {
		// for logging
		Log.d("addApp", app.toString() + " " + app.getDescription());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(COLUMN_APP_NAME, app.toString());
		values.put(COLUMN_PRODUCTIVITY, app.getProductivityRating());
		values.put(COLUMN_CATEGORY, app.getCategory());
		values.put(COLUMN_DESCRIPTION, app.getDescription());
		// 2.1 the below is to convert the bitmap image in our App class to a
		// byte array
		if (app.getIcon() != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap iconBitmap = app.getIcon();
			iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] iconByteArray = baos.toByteArray();
			values.put(COLUMN_ICON, iconByteArray);
		} else {
			byte[] iconByteArray = null;
			values.put(COLUMN_ICON, iconByteArray);
		}

		// 3. insert, and remember primary key value to return it
		long newRowId;
		newRowId = db.insert(APP_TABLE_NAME, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values
		db.close();

		/*
		 * // 4. close db.close();
		 */
		return newRowId;
	}

	public App getApp(String name) {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();

		String[] nameString = new String[1];
		nameString[0] = name;

		Cursor cursor = db.rawQuery("select * from " + APP_TABLE_NAME
				+ " where " + COLUMN_APP_NAME + " = ?", nameString);
		/*
		 * // 2. build query Cursor cursor = db.query(APP_TABLE_NAME, // a.
		 * table COLUMNS, // b. column names " appName = ?", // c. selections
		 * nameString, null, // e. group by null, // f. having null, // g. order
		 * by null); // h. limit
		 */
		// 3. if we got results get the first one

		if (cursor.moveToFirst()) {
			// 4. build App object
			App app = new App(cursor.getString(0));
			app.setProductivityRating(Integer.parseInt(cursor.getString(1)));
			app.setCategory(cursor.getString(2));
			// 4.1 have to convert app from blob to bitmap for our App class
			ByteArrayInputStream myIconStream = new ByteArrayInputStream(
					cursor.getBlob(3));
			if (myIconStream != null) {
				app.setIcon(BitmapFactory.decodeStream(myIconStream));
			}
			app.setDescription(cursor.getString(4));

			db.close();
			return app;
		} else {
			Log.d("getApp()", "No app found with name " + nameString);
			db.close();
			return new App("");
		}
	}

	public void updateApp(App appToUpdate) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(COLUMN_APP_NAME, appToUpdate.toString());
		values.put(COLUMN_PRODUCTIVITY, appToUpdate.getProductivityRating());
		values.put(COLUMN_CATEGORY, appToUpdate.getCategory());
		values.put(COLUMN_DESCRIPTION, appToUpdate.getDescription());
		// 2.1 the below is to convert the bitmap image in our App class to a
		// byte array
		if (appToUpdate.getIcon() != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap iconBitmap = appToUpdate.getIcon();
			iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] iconByteArray = baos.toByteArray();
			values.put(COLUMN_ICON, iconByteArray);
		} else {
			byte[] iconByteArray = null;
			values.put(COLUMN_ICON, iconByteArray);
		}

		// 3. update the database where the name matches
		String whereClause = COLUMN_APP_NAME + " LIKE ?";
		String[] whereArg = { appToUpdate.toString() };
		db.update(APP_TABLE_NAME, values, whereClause, whereArg);

		db.close();

		/*
		 * // 4. close db.close();
		 */
		return;
	}

	public void updateAppProductivity(String appName, int productivity) {
		App appToChange = getApp(appName);
		if (appToChange == null) {
			Log.d("updateAppProductivity()", "App with name " + appName
					+ " not found");
			return;
		} else {
			// Change app's productivity
			appToChange.setProductivityRating(productivity);

			// Update the database
			updateApp(appToChange);
		}
	}

	public List<App> getAllApps() {
		List<App> Apps = new LinkedList<App>();

		// 1. build the query
		String query = "SELECT * FROM " + APP_TABLE_NAME;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build App and add it to list
		App app = null;
		if (cursor.moveToFirst()) {
			do {
				app = new App(cursor.getString(0));
				app.setProductivityRating(Integer.parseInt(cursor.getString(1)));
				app.setCategory(cursor.getString(2));
				try {
					ByteArrayInputStream myIconStream = new ByteArrayInputStream(
							cursor.getBlob(3));
					app.setIcon(BitmapFactory.decodeStream(myIconStream));
				} catch (Exception E) {

				}
				app.setDescription(COLUMN_DESCRIPTION);

				// Add App to Apps
				Apps.add(app);
			} while (cursor.moveToNext());
		}

		Log.d("getAllApps()", Apps.toString());

		db.close();
		// return Apps
		return Apps;
	}

	// Called by the tracker;
	// -throws out old information if the app name is blank,
	// -updates app info if app name is the same,
	// adds app to the db then repopulates the data of the event if app name is
	// different
	public void updateAppEvent(AppEvent oldEvent, String newAppName,
			GPSManager myGPS) {
		// for logging
		// GPSManager myGPS = new GPSManager(mCtx);
		Log.d("updateAppEvent", oldEvent.toString() + " | " + newAppName);
		float longCoords = myGPS.getLongitude(), latCoords = myGPS
				.getLatitude();

		Log.d("updateAppEvent", "Location: " + latCoords + " x " + longCoords);
		if (oldEvent.toString().equals("")) {
			oldEvent.resetAppEvent(newAppName, latCoords, longCoords);
			return;
		}
		if (oldEvent.toString().equals(newAppName)) {
			// TODO: changing the way we do our GPS
			// oldEvent.updateLocationInfo(newLongitude, newLatitude);
			oldEvent.updateEndTime();
			return;
		}
		// if(!oldEvent.toString().equals(newAppName))
		oldEvent.updateLocationInfo(latCoords, longCoords);
		addAppEvent(oldEvent);
		oldEvent.resetAppEvent(newAppName);
	}

	public long addAppEvent(AppEvent appEvent) {
		// for logging
		Log.d("addAppEvent",
				appEvent.toString() + " | " + appEvent.getStartTime() + " | "
						+ appEvent.getEndTime() + " | "
						+ appEvent.getStartLatitude() + " | "
						+ appEvent.getStartLongitude() + " | "
						+ appEvent.getEndLatitude() + " | "
						+ appEvent.getEndLongitude() + " | "
						+ appEvent.eventType() + " | "
						+ appEvent.getLocationList().size());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(COLUMN_APP_NAME, appEvent.toString());
		values.put(COLUMN_START_TIME, appEvent.getStartTime());
		values.put(COLUMN_END_TIME, appEvent.getEndTime());
		values.put(COLUMN_START_LATITUDE, appEvent.getStartLatitude());
		values.put(COLUMN_START_LONGITUDE, appEvent.getStartLongitude());
		values.put(COLUMN_END_LATITUDE, appEvent.getEndLatitude());
		values.put(COLUMN_END_LONGITUDE, appEvent.getEndLongitude());
		values.put(COLUMN_EVENT_TYPE, appEvent.eventType());
		if (appEvent.getLocationList().size() == 0) {
			values.put(COLUMN_EVENT_LOCATION, "");
		} else {
			values.put(COLUMN_EVENT_LOCATION, appEvent.getLocationList().get(0));
		}

		// 3. insert, and remember primary key value to return it
		long newRowId;
		newRowId = db.insert(APP_USE_TABLE, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values

		// 4. close db and return the ID
		db.close();
		return newRowId;
	}

	public List<AppEvent> getAppEvents(String timeInterval) {
		List<AppEvent> listOfEvents = new LinkedList<AppEvent>();
		Log.d("getAppEvents()", "called with " + timeInterval);

		// 1. build the query
		// 1.1 Set the start time
		long myStartTime = 0;
		long myEndTime = 0;
		long myInterval = defaultTimeframe;
		if (timeInterval == null) {
			myStartTime = System.currentTimeMillis() / 1000L - defaultTimeframe;
		}
		if (timeInterval.equals("day")) {
			myStartTime = System.currentTimeMillis() / 1000L - 86400;
			myInterval = 86400;
		} else if (timeInterval.equals("week")) {
			myStartTime = System.currentTimeMillis() / 1000L - 604800;
			myInterval = 604800;
		} else if (timeInterval.equals("month")) {
			myStartTime = System.currentTimeMillis() / 1000L - 2629744;
			myInterval = 2629744;
		} else if (timeInterval.equals("year")) {
			myStartTime = System.currentTimeMillis() / 1000L - 31556926;
			myInterval = 31556926;
		} else {
			// (timeInterval is not represented above)
			myStartTime = System.currentTimeMillis() / 1000L - defaultTimeframe;
			// myInterval = defaultTimeframe;
		}

		String query = "SELECT * FROM " + APP_USE_TABLE + " WHERE "
				+ COLUMN_START_TIME + " >= " + myStartTime + " ORDER BY "
				+ COLUMN_APP_NAME;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build AppEvent and add it to list
		AppEvent appEvent = null;
		if (cursor.moveToFirst()) {
			do {
				// app name, start time, end time, start longitude, start
				// latitude, end longitude, end latitude,
				// event type, location name
				appEvent = new AppEvent(cursor.getString(0), cursor.getLong(1),
						cursor.getLong(2), cursor.getFloat(3),
						cursor.getFloat(4), cursor.getFloat(5),
						cursor.getFloat(6), cursor.getString(7),
						cursor.getString(8));

				// Add AppEvent to listOfEvents
				listOfEvents.add(appEvent);
			} while (cursor.moveToNext());
		}

		Log.d("getAppEvents()",
				"events found: " + String.valueOf(listOfEvents.size()));

		// close db and return the app events
		db.close();
		return listOfEvents;
	}

	// Allow retrieval of app events with a specific start time
	public List<AppEvent> getAppEvents(String timeInterval, long startTime) {
		List<AppEvent> listOfEvents = new LinkedList<AppEvent>();
		Log.d("getAppEvents()",
				"called with " + timeInterval + " " + String.valueOf(startTime));

		// 1. build the query
		// 1.1 Set the start time
		long myStartTime = startTime;
		long myEndTime = 0;

		// 1.2 Initialize calendar
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);

		if (timeInterval.equals("day")) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			myEndTime = calendar.getTimeInMillis();
		} else if (timeInterval.equals("week")) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			myEndTime = calendar.getTimeInMillis();
		} else if (timeInterval.equals("month")) {
			calendar.add(Calendar.MONTH, 1);
			myEndTime = calendar.getTimeInMillis();
		} else if (timeInterval.equals("year")) {
			calendar.add(Calendar.YEAR, 1);
			myEndTime = calendar.getTimeInMillis();
		} else { // Default
			myEndTime = myStartTime + defaultTimeframe * 1000;
		}

		String query = "SELECT * FROM " + APP_USE_TABLE + " WHERE "
				+ COLUMN_START_TIME + " >= " + myStartTime / 1000L + " AND "
				+ COLUMN_END_TIME + " <= " + myEndTime / 1000L + " ORDER BY "
				+ COLUMN_APP_NAME;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build AppEvent and add it to list
		AppEvent appEvent = null;
		if (cursor.moveToFirst()) {
			do {
				// app name, start time, end time, start longitude, start
				// latitude, end longitude, end latitude,
				// event type, location name
				appEvent = new AppEvent(cursor.getString(0), cursor.getLong(1),
						cursor.getLong(2), cursor.getFloat(3),
						cursor.getFloat(4), cursor.getFloat(5),
						cursor.getFloat(6), cursor.getString(7),
						cursor.getString(8));

				// Add AppEvent to listOfEvents
				listOfEvents.add(appEvent);
			} while (cursor.moveToNext());
		}

		Log.d("getAppEvents()",
				"events found: " + String.valueOf(listOfEvents.size()));

		// close db and return the app events
		db.close();
		return listOfEvents;
	}

	// TODO: Finish this, integrate it into MainActivity
	// Allow retrieval of app events with a specific start time
	public ArrayList<AppEvent> getAppEvents(long startTime) {
		ArrayList<AppEvent> listOfEvents = new ArrayList<AppEvent>();
		Log.d("getAppEvents()", "called with " + String.valueOf(startTime));

		// 1. build the query
		// 1.1 Set the start time
		long myStartTime = startTime;

		// 1.2 Initialize calendar
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);

		String query = "SELECT * FROM " + APP_USE_TABLE + " WHERE "
				+ COLUMN_START_TIME + " >= " + myStartTime / 1000L
				+ " ORDER BY " + COLUMN_APP_NAME;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build AppEvent and add it to list
		AppEvent appEvent = null;
		if (cursor.moveToFirst()) {
			do {
				// app name, start time, end time, start longitude, start
				// latitude, end longitude, end latitude,
				// event type, location name
				appEvent = new AppEvent(cursor.getString(0), cursor.getLong(1),
						cursor.getLong(2), cursor.getFloat(3),
						cursor.getFloat(4), cursor.getFloat(5),
						cursor.getFloat(6), cursor.getString(7),
						cursor.getString(8));

				// Add AppEvent to listOfEvents
				listOfEvents.add(appEvent);
			} while (cursor.moveToNext());
		}

		Log.d("getAppEvents()",
				"events found: " + String.valueOf(listOfEvents.size()));

		// close db and return the app events
		db.close();
		return listOfEvents;
	}

	public List<AppSummary> calculateAppSummaries(List<AppEvent> allEvents) {
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
				totalProductivity += getApp(allEvents.get(eventNum).toString())
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
						+ getApp(allEvents.get(0).toString()).toString());
		AppSummary currentAppSummary = new AppSummary(getApp(allEvents.get(0)
				.toString()));

		// For retrieving coordinates
		ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
		Coordinates coordsToAdd = new Coordinates(allEvents.get(0).getStartLatitude(), allEvents.get(0).getStartLongitude());
		coordsToAdd.latitude = allEvents.get(0).getStartLatitude();
		coordsToAdd.longitude = allEvents.get(0).getStartLongitude();
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

				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
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
						+ coordsToAdd.latitude + "," + coordsToAdd.longitude
						+ " added to list");

				currentAppSummary = new AppSummary(getApp(allEvents.get(
						eventNum).toString()));
				currentAppSummary.addTotalTime(allEvents.get(eventNum)
						.getDuration());
				currentAppSummary.addCoordinatesList(coordsList);

				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
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
						+ " with end coords " + coordsToAdd.latitude + ","
						+ coordsToAdd.longitude + " added to list");

		// Output a sorted list of summaries, based on their total times
		Collections.sort(listOfSummaries, new AppSummary.compareTimes());
		Log.d("calculateAppSummaries()", "end");
		return listOfSummaries;
	}

	public List<AppSummary> calculateAppSummaries(List<AppEvent> allEvents,
			double productivityRating, long mainTotalTime) {
		Log.d("calculateAppSummaries()",
				"Num of events: " + String.valueOf(allEvents.size()));

		ArrayList<AppSummary> listOfSummaries = new ArrayList<AppSummary>();
		if (allEvents.isEmpty()) {
			return listOfSummaries;
		}

		// Go through and calculate total app use time, while culling
		// blank-named apps
		long totalTime = 0;
		double totalProductivity = 0;
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
				totalProductivity += getApp(allEvents.get(eventNum).toString())
						.getProductivityRating() * myTime;
				Log.d("calculateAppSummaries()",
						"Adding time from event " + eventNum
								+ ", total time is "
								+ String.valueOf(totalTime));
			}
		}
		if (allEvents.isEmpty()) {
			return listOfSummaries;
		}

		// Calculate total average productivity; default to 50%
		double avgProductivity = 1;
		if (totalTime != 0) {
			avgProductivity = ((double) totalProductivity / (double) totalTime) * 100d;
		}
		productivityRating = avgProductivity;
		mainTotalTime = totalTime;
		Log.d("Total app time (calculateAppSummaries())",
				String.valueOf(mainTotalTime));

		// Initialize the first app summary to contain the information regarding
		// the app corresponding to app event 0
		Log.d("calculateAppSummaries()", "Finding app/making summary for "
				+ allEvents.get(0).toString());
		Log.d("calculateAppSummaries()",
				"Found app for "
						+ getApp(allEvents.get(0).toString()).toString());
		AppSummary currentAppSummary = new AppSummary(getApp(allEvents.get(0)
				.toString()));

		// For retrieving coordinates
		ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
		Coordinates coordsToAdd = new Coordinates(allEvents.get(0).getStartLatitude(), allEvents.get(0).getStartLongitude());
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

				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
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
						+ coordsToAdd.latitude + "," + coordsToAdd.longitude
						+ " added to list");

				currentAppSummary = new AppSummary(getApp(allEvents.get(
						eventNum).toString()));
				currentAppSummary.addTotalTime(allEvents.get(eventNum)
						.getDuration());
				currentAppSummary.addCoordinatesList(coordsList);

				coordsToAdd.latitude = allEvents.get(eventNum)
						.getStartLatitude();
				coordsToAdd.longitude = allEvents.get(eventNum)
						.getStartLongitude();
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
						+ " with end coords " + coordsToAdd.latitude + ","
						+ coordsToAdd.longitude + " added to list");

		// Output a sorted list of summaries, based on their total times
		Collections.sort(listOfSummaries, new AppSummary.compareTimes());
		Log.d("calculateAppSummaries()", "end");
		return listOfSummaries;
	}

	// Basic app summary lister
	public List<AppSummary> getAppSummaries(String timeInterval) {
		Log.d("getAppSummaries()", "start");
		List<AppEvent> myAppEvents = getAppEvents(timeInterval);
		return calculateAppSummaries(myAppEvents);
	}

	// Advanced app summary lister; uses start time
	public List<AppSummary> getAppSummaries(String timeInterval, long startTime) {
		Log.d("getAppSummaries()", "start");
		List<AppEvent> myAppEvents = getAppEvents(timeInterval, startTime);
		return calculateAppSummaries(myAppEvents);
	}

	// Advanced app summary lister; uses start time and can interact with Main
	public List<AppSummary> getAppSummaries(String timeInterval,
			long startTime, double productivityRating, long totalTime) {
		Log.d("getAppSummaries()", "start");
		List<AppEvent> myAppEvents = getAppEvents(timeInterval, startTime);
		return calculateAppSummaries(myAppEvents, productivityRating, totalTime);
	}

	public long getLastUp() {
		long time = 0;

		// 1. build the query
		String query = "SELECT * FROM " + SETTINGS_TABLE;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		cursor.moveToFirst();
		time = cursor.getInt(3);

		db.close();
		// return Apps
		return time;
	}

	public String getUserName() {
		String query = "SELECT userName FROM " + SETTINGS_TABLE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	public String getPassword() {
		String query = "SELECT password FROM " + SETTINGS_TABLE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	public String getURL() {
		String query = "SELECT url FROM " + SETTINGS_TABLE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	public void setSettings(String username, String password, String url) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(DELETE_SETTINGS_TABLE);
		db.execSQL(CREATE_SETTINGS_TABLE);

		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_NAME, username);
		values.put(COLUMN_PASSWORD, password);
		values.put(COLUMN_URL, url);
		values.put(COLUMN_LOGGING, "");

		// 3. insert, and remember primary key value to return it
		long newRowId;
		newRowId = db.insert(SETTINGS_TABLE, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values
	}
}
