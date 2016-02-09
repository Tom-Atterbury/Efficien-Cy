package tester;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.*;
import android.util.Log;

import com.coms309.efficiency_coms309_iastate.*;

public class testSuite1 {
	private List<AppSummary> testSummaries;
	private List<AppEvent> testEvents;
	private List<App> testApps;
	private SQLiteHelper testDatabase;
	private Context context;

	public testSuite1(SQLiteHelper targetDB, Context context) {
		testSummaries = new LinkedList<AppSummary>();
		testEvents = new LinkedList<AppEvent>();
		testApps = new LinkedList<App>();
		testDatabase = targetDB;
		this.context = context;
	}

	public void changeDB(SQLiteHelper newDB) {
		testDatabase = newDB;
	}

	public void setTest1Data() {
		App testApp = new App("test");
		AppEvent testEvent = new AppEvent("test");
		AppSummary testSummary = new AppSummary("test");
		Bitmap icon;
		icon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android_icon);
		testApp.setIcon(icon);
		testSummary.setIcon(icon);

		// Reset data
		testSummaries = new LinkedList<AppSummary>();
		testEvents = new LinkedList<AppEvent>();
		testApps = new LinkedList<App>();

		// Start making apps to add to the DB
		testApp = changeApp(testApp, "Happy", "The happiest dwarf", "dwarf", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Sleepy", "A sleepy dwarf", "dwarf", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Dopey", "Not the brightest", "dwarf", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Bashful", "hides a lot", "dwarf", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Doc", "Leader of the group", "dwarf", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Grumpy", "Not the same as Happy",
				"dwarf", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Sneezy", "Needs a tissue", "dwarf", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "DelicaCy",
				"App to track the time spent on various foods.",
				"productivity", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Flappy Bird", "Helps you waste time",
				"game", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Facenovel", "Helps you waste time",
				"social", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Tweeter", "*tweet*", "social", 0);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Nile", "Allows user to order products",
				"business", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);
		testApp = changeApp(testApp, "Travel",
				"Find where you are in the world", "navigation", 1);
		testApps.add(testApp);
		testDatabase.addApp(testApp);

		// Make lists of app events to add to the DB
		ArrayList<String> locsList = new ArrayList<String>();
		locsList.add("Here");

		// 2012
		// Jan
		// Mar
		// Jun
		// Nov
		// Dec
		// 2013
		// Jan
		// Feb
		// Mar
		// Apr
		// May
		// Jun
		// Jul
		// Aug
		// Sep
		// Oct
		// Nov
		// Dec
		testEvent = changeAppEvent(testEvent, "Doc",
				dateStringToLong("20131218010720"),
				dateStringToLong("20131229185820"), (float) 48.376112,
				(float) -89.262039, (float) 48.376112, (float) -89.262039,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);
		// 2014
		// Jan
		// Feb
		// Mar
		// Apr
		// May
		// Jun
		// Jul
		// Aug
		// Sep
		// Oct
		// Nov
		testEvent = changeAppEvent(testEvent, "Dopey",
				dateStringToLong("20141107192720"),
				dateStringToLong("20141127192728"), (float) 47.376112,
				(float) -89.262039, (float) 47.376112, (float) -89.262039,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);
		// Dec
		// 01
		// 02
		// 03
		// 04
		// 05
		// 06
		testEvent = changeAppEvent(testEvent, "DelicaCy",
				dateStringToLong("20141206010720"),
				dateStringToLong("20141206010817"), (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);

		testEvent = changeAppEvent(testEvent, "Tweeter",
				dateStringToLong("20141206010817"),
				dateStringToLong("20141206010819"), (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);

		testEvent = changeAppEvent(testEvent, "Flappy Bird",
				dateStringToLong("20141206010819"),
				dateStringToLong("20141206011817"), (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);

		testEvent = changeAppEvent(testEvent, "Tweeter",
				dateStringToLong("20141206011820"),
				dateStringToLong("20141206022835"), (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);

		testDatabase.addAppEvent(testEvent);
		testEvent = changeAppEvent(testEvent, "DelicaCy",
				dateStringToLong("20141206210720"),
				dateStringToLong("20141207015829"), (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);
		// 07
		testEvent = changeAppEvent(testEvent, "Grumpy",
				dateStringToLong("20141207010720"),
				dateStringToLong("20141207185820"),  (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);

		// 08
		testEvent = changeAppEvent(testEvent, "Happy",
				dateStringToLong("20141208185720"),
				dateStringToLong("20141208185820"),  (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);

		testEvent = changeAppEvent(testEvent, "Tweeter",
				dateStringToLong("20141208235720"),
				dateStringToLong("20141208235820"),  (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
		testDatabase.addAppEvent(testEvent);

		// 09
		testEvent = changeAppEvent(testEvent, "DelicaCy",
				dateStringToLong("20141209010720"),
				dateStringToLong("20141209081721"),  (float) -89.262039,
				(float) 48.376112, (float) -89.262039, (float) 48.376112,
				locsList);
		Log.d("add testEvent", String.valueOf(testEvent.getStartTime()));
		testEvents.add(testEvent);
	}

	public App changeApp(App appToChange, String newName,
			String newDescription, String newCategory, int newProductivity) {
		appToChange.setName(newName);
		appToChange.setDescription(newDescription);
		appToChange.setCategory(newCategory);
		appToChange.setProductivityRating(newProductivity);
		return appToChange;
	}

	public AppEvent changeAppEvent(AppEvent eventToChange, String newName,
			long newStart, long newEnd, float newStartLat, float newStartLong,
			float newEndLat, float newEndLong, ArrayList<String> newLocsList) {
		eventToChange.setName(newName);
		eventToChange.setStartAndEnd(newStart, newEnd);
		eventToChange.setStartLatitude(newStartLat);
		eventToChange.setStartLongitude(newStartLong);
		eventToChange.setEndLatitude(newEndLat);
		eventToChange.setEndLongitude(newEndLong);
		eventToChange.setLocations(newLocsList);

		return eventToChange;
	}

	// Format: YYYYMMDDHHMMSS
	public long dateStringToLong(String date) {
		if (date == null) {
			return 0;
		}
		long timeToReturn;
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(date.substring(8, 10));
		int min = Integer.parseInt(date.substring(10, 12));
		int sec = Integer.parseInt(date.substring(12));

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		Log.d("Test data date",
				String.valueOf(year) + "/" + String.valueOf(month) + "/"
						+ String.valueOf(day) + "/" + String.valueOf(hour)
						+ "/" + String.valueOf(min) + "/" + String.valueOf(sec));

		timeToReturn = calendar.getTimeInMillis() / 1000L;
		return timeToReturn;
	}
}
