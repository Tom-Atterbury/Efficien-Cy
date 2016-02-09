package com.coms309.efficiency_coms309_iastate;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import tester.testSuite1;
import AppInfoRetrieval.GPSManager;
import AppInfoRetrieval.TopApp;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	public static SQLiteHelper database;
	public static String defaultTimeframe = "day";
	private TopApp topApp;
	private long appStartTime;
	public long totalTime;
	public double avgProductivity;
	private TextView dateDisplay;
	private MyAdaptor adaptor;
	private AppEvent event;
	private static GPSManager GPS;
	private static String userInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		database = new SQLiteHelper(this);
		database = SQLiteHelper.getInstance(this);
		topApp = new TopApp(this);
		appStartTime = System.currentTimeMillis() - 86400000;
		adaptor = new MyAdaptor(this);
		event = new AppEvent("");
		GPS = new GPSManager(this);
		avgProductivity = 1;
		totalTime = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(appStartTime);
		calendar.get(Calendar.DAY_OF_MONTH);
		calendar.get(Calendar.MONTH);
		calendar.get(Calendar.YEAR);

		// start tracking as a service in a new process.
		Intent intent = new Intent(this, Tracking.class);
		startService(intent);

		// start remote database updating as a service in a new process.
		Intent remote = new Intent(this, RemoteHelperDB.class);
		startService(remote);

		// Start listeners
		AppListener.init(this.getApplication(), this);

		setContentView(R.layout.activity_main);

		getActionBar().setHomeButtonEnabled(true);

		// Create the radio group buttons and set the background
		RadioGroup rbg = (RadioGroup) findViewById(R.id.radio_group);
		rbg.setBackgroundColor(Color.BLACK);

		// Display the current date as start time for time tracking
		dateDisplay = (TextView) findViewById(R.id.date_display);

		// Load dummy data to database
		testSuite1 tester = new testSuite1(database, this);
		tester.setTest1Data();
		
		// Call appLister to list apps on screen
		appLister();
		Log.d("OnCreate()", "ActionBar set");
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#770C02")));

	}

	// Method to reset the information in the database
	// User should be warned excessively when this is to be called!
	public void databaseReset() {
		database.onUpgrade(database.getWritableDatabase(),
				database.getDatabaseVersion(), database.getDatabaseVersion());
		appLister();
	}

	/**
	 * appLister is a function to list the apps in the database on the screen
	 */
	public void appLister() {

		// Update database with most recent
		App currentTopApp = topApp.getTopApp();
		database.addApp(currentTopApp);
		database.updateAppEvent(event, currentTopApp.toString(), GPS);

		// Create a listview based on the XML file activity_main.xml
		ExpandableListView list = (ExpandableListView) findViewById(R.id.appList);

		List<AppSummary> appSummaries = new LinkedList<AppSummary>();
		if (adaptor == null) {
			appSummaries = database.getAppSummaries(defaultTimeframe,
					appStartTime, avgProductivity, totalTime);
			Log.d("getAppSummaries()",
					"Default call with start time "
							+ String.valueOf(appStartTime));
		} else if (adaptor.getTimeSelection() == null) {
			appSummaries = database.getAppSummaries(defaultTimeframe,
					appStartTime, avgProductivity, totalTime);
			Log.d("getAppSummaries()",
					"Default call with start time "
							+ String.valueOf(appStartTime));
		} else if (adaptor.getTimeSelection().equals("day")
				|| adaptor.getTimeSelection().equals("week")
				|| adaptor.getTimeSelection().equals("month")
				|| adaptor.getTimeSelection().equals("year")) {
			appSummaries = database.getAppSummaries(adaptor.getTimeSelection(),
					appStartTime, avgProductivity, totalTime);
			Log.d("getAppSummaries()", adaptor.getTimeSelection()
					+ " with start time " + String.valueOf(appStartTime));
		} else {
			appSummaries = database.getAppSummaries(defaultTimeframe,
					appStartTime, avgProductivity, totalTime);
			Log.d("getAppSummaries()",
					"Default call with start time "
							+ String.valueOf(appStartTime));
		}
		Log.d("getAppSummaries()",
				String.valueOf("num of app summaries in app lister: "
						+ appSummaries.size()));
		
		Log.d("key23", "1");
		if(userInput != null) {
			getDiffUser cow = new getDiffUser();
			appSummaries = cow.getUserData(userInput);
			}

		// Data to retrieve from the database
		String[] appNames = new String[appSummaries.size()];
		String[][] appDescriptions = new String[appSummaries.size()][1];
		Bitmap[] icons = new Bitmap[appSummaries.size()];
		int[] percentages = new int[appSummaries.size()];
		long[] times = new long[appSummaries.size()];
		int[] productivity = new int[appSummaries.size()];
		Coordinates[] coords = new Coordinates[appSummaries.size()];

		// Parameters for total productivity and time
		double totalProd = 0;
		long totalTime = 0;

		// Retrieve data from the database in indexed order
		for (int i = 0; i < appSummaries.size(); i++) {
			AppSummary currSummary = appSummaries.get(i);
			icons[i] = currSummary.getIcon();
			appNames[i] = (currSummary.toString());
			appDescriptions[i][0] = currSummary.getDescription();
			productivity[i] = currSummary.getProductivityRating();
			coords[i] = currSummary.getLastLocation();
			
			Toast.makeText(this, appNames[i] + " Lat: " + (coords[i] != null ? coords[i].latitude : "null"), Toast.LENGTH_LONG).show();

			percentages[i] = currSummary.getPercentTime();
			Log.d("appLister()", "Percent time for " + currSummary.toString()
					+ ": " + currSummary.getPercentTime());
			times[i] = currSummary.getTotalTime();

			// Sum up parameters for total productivity
			totalProd += (double) productivity[i] * (double) times[i];
			totalTime += times[i];
		}

		// Creates a list on the main screen from the custom class MyAdaptor
		adaptor.setAdaptor(this, appNames, appDescriptions, percentages, times,
				icons, productivity, coords);
		list.setAdapter(adaptor);

		// Update start date displayed, and other info
		updateDate();

		this.totalTime = totalTime;
		TextView timeDevice = (TextView) findViewById(R.id.total_time_value);
		timeDevice.setText(adaptor.timeToString(this.totalTime));
		this.avgProductivity = (totalProd / (double) totalTime) * 100L;
		TextView prodRating = (TextView) findViewById(R.id.productivity_rating_value);
		prodRating.setText(String.valueOf( (int) avgProductivity) + "%");

		if (avgProductivity >= 75) {
			prodRating.setTextColor(Color.GREEN);
		}
		else if (avgProductivity >= 50) {
			prodRating.setTextColor(Color.YELLOW);
		}
		else {
			prodRating.setTextColor(Color.RED);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.settings) {
			Intent s = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(s);
			return true;
		}else if(itemId == R.id.refresh_data){
			appLister();
			return true;
		} else if (itemId == R.id.reset_system) {
			resetDatabase();
			return true;
		} else if (itemId == R.id.about_app) {
			Intent a = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(a);
			return true;
		} else if (itemId == R.id.account) {
			Intent account = new Intent(MainActivity.this, Account2.class);
			startActivity(account);
			return true;
		} else if (itemId == R.id.reset_user) {
			createLogin(null, new String(""), new String(""));
			appLister();
			return true;
		} else if (itemId == R.id.share) {
			String tweetUrl = "https://twitter.com/intent/tweet?text=My productivity is "
					+ (int) avgProductivity
					+ " percent according to EfficienCy! "
					+ "\n"
					+ "Check out your productivity with &hashtags=EfficienCy"
					+ "!";
			Uri uri = Uri.parse(tweetUrl);
			this.startActivity(new Intent(Intent.ACTION_VIEW, uri));
			return true;
		} else if (itemId == R.id.choose_user) {
			// get prompts.xml view
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			
			View promptView = layoutInflater.inflate(R.layout.create_user_prompt, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			// set prompts.xml to be the layout file of the alertdialog builder
			alertDialogBuilder.setView(promptView);

			final EditText input = (EditText) promptView.findViewById(R.id.userInput);
			final EditText editTextMainScreen = new EditText(this);

			// setup a dialog window
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// get user input and set it to result
									editTextMainScreen.setText(input.getText());
									userInput = input.getText().toString();
									appLister();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,	int id) {
									dialog.cancel();
								}
							});

			// create an alert dialog
			AlertDialog alertD = alertDialogBuilder.create();

			alertD.show();
			
			
			
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Changes the time setting based on the radio button group
	 * 
	 * @param view
	 */
	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		
		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.day:
			if (checked) {
				adaptor.setTimeSelection("day");
				resetStartTime();
			}
			break;
		case R.id.week:
			if (checked) {
				adaptor.setTimeSelection("week");
				resetStartTime();
			}
			break;
		case R.id.month:
			if (checked) {
				adaptor.setTimeSelection("month");
				resetStartTime();
			}
			break;
		case R.id.year:
			if (checked) {
				adaptor.setTimeSelection("year");
				resetStartTime();
			}
			break;
		}
		appLister();
	}

	/**
	 * Resets the local database
	 */
	public void resetDatabase() {
		showResetAlert();
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * launch Settings Options
	 * */
	public void showResetAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("Data Reset");

		// Setting Dialog Message
		alertDialog
				.setMessage("This will reset your data. \nThis action is non-reversible. \nDo you want to reset your data?");

		// On pressing Yes button
		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						databaseReset();
						appLister();
						dialog.dismiss();
					}
				});

		// on pressing No button
		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						appLister();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * returns the starting time of the app
	 * 
	 * @return appStartTime
	 */
	public long getStartTime() {
		return appStartTime;
	}

	/**
	 * Sets the starting timeframe
	 * 
	 * @param newStartTime
	 */
	public void setStartTime(long newStartTime) {
		appStartTime = newStartTime;
	}

	/**
	 * Changes the value of the current day based on the next arrow click
	 * 
	 * @param view
	 */
	public void nextData(View view) {
		// Add time selection to current start for app listing
		Calendar calendar = Calendar.getInstance();
		if (adaptor == null) {
			// Default to day
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, 1);
			Log.d("nextData()", "Adaptor not found");
		} else if (adaptor.getTimeSelection() == null) {
			Log.d("nextData()", "Time selection is null");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, 1);
		} else if (adaptor.getTimeSelection().equals("day")) {
			Log.d("nextData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, 1);
		} else if (adaptor.getTimeSelection().equals("week")) {
			Log.d("nextData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, 7);
		} else if (adaptor.getTimeSelection().equals("month")) {
			Log.d("nextData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.MONTH, 1);
		} else if (adaptor.getTimeSelection().equals("year")) {
			Log.d("nextData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.YEAR, 1);
		} else {
			// Default to day
			Log.d("nextData()", "day (default)");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, 1);
		}
		appStartTime = calendar.getTimeInMillis();
		Log.d("nextData()", String.valueOf(appStartTime) + "");

		// After time has been added, update the date and show updated app
		// listings
		updateDate();
		appLister();
	}

	/**
	 * Changes the value of the day based on the previous arrow click
	 * 
	 * @param view
	 */
	public void prevData(View view) {
		// Add time selection to current start for app listing
		Calendar calendar = Calendar.getInstance();
		if (adaptor == null) {
			// Default to day
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, -1);
			Log.d("prevData()", "Adaptor not found");
		} else if (adaptor.getTimeSelection() == null) {
			Log.d("prevData()", "Time selection is null");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, -1);
		} else if (adaptor.getTimeSelection().equals("day")) {
			Log.d("prevData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, -1);
		} else if (adaptor.getTimeSelection().equals("week")) {
			Log.d("prevData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, -7);
		} else if (adaptor.getTimeSelection().equals("month")) {
			Log.d("prevData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.MONTH, -1);
		} else if (adaptor.getTimeSelection().equals("year")) {
			Log.d("prevData()", adaptor.getTimeSelection() + "");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.YEAR, -1);
		} else {
			// Default to day
			Log.d("prevData()", "day (default)");
			calendar.setTimeInMillis(appStartTime);
			calendar.add(Calendar.DATE, -1);
		}
		appStartTime = calendar.getTimeInMillis();
		Log.d("prevData()", String.valueOf(appStartTime) + "");

		// After time has been added, update the date and show updated app
		// listings
		updateDate();
		appLister();
	}

	/**
	 * Change the displayed start time for tracked app list
	 */
	public void updateDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(appStartTime);

		String fromDate = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, Locale.US)
				+ " "
				+ calendar.get(Calendar.DAY_OF_MONTH)
				+ ", "
				+ calendar.get(Calendar.YEAR);

		// Get the end time of the current timeframe
		calendar.setTimeInMillis(getWindowEndTime());

		String toDate = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
				Locale.US)
				+ " "
				+ calendar.get(Calendar.DAY_OF_MONTH)
				+ ", "
				+ calendar.get(Calendar.YEAR);

		String date = "From " + fromDate + " to " + toDate;
		
		// date += "\naverage productivity: "
		// + String.valueOf((double) ((int) (avgProductivity * 100)) / 100L)
		// + "\nTotal time: " + String.valueOf(totalTime);

		dateDisplay.setText(date);
	}

	private long getWindowEndTime() {
		switch (adaptor.getTimeSelection()) {
		case "day":
			return appStartTime + 86400 * 1000L;
		case "week":
			return appStartTime + 604800 * 1000L;
		case "month":
			return appStartTime + 2629744 * 1000L;
		case "year":
			return appStartTime + 31556926 * 1000L;
		default:
			return System.currentTimeMillis() / 1000L;
		}
	}

	public void resetStartTime() {
		Calendar calendar = Calendar.getInstance();
		if (adaptor == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DATE, -1);
		}
		if (adaptor.getTimeSelection() == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DATE, -1);
		} else if (adaptor.getTimeSelection().equals("day")) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DATE, -1);
		} else if (adaptor.getTimeSelection().equals("week")) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DATE, -7);
		} else if (adaptor.getTimeSelection().equals("month")) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.MONTH, -1);
		} else if (adaptor.getTimeSelection().equals("year")) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.YEAR, -1);
		} else {
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DATE, -1);
		}
		appStartTime = calendar.getTimeInMillis();
		if (adaptor != null) {
			Log.d("resetStartTime()", adaptor.getTimeSelection() + "");
		} else {
			Log.d("resetStartTime()", "Adaptor not found");
		}
		Log.d("resetStartTime()", String.valueOf(appStartTime) + "");
	}
	
	public static void setProductivityRating(String name, int prodRating) {
		database.updateAppProductivity(name, prodRating);
	}
	
	public static void createLogin(String username, String password, String server) {	
		
		userInput = username;
		
		database.setSettings(username, password, server);
	}
	
	public static Coordinates getCurrentLocation() {
		return new Coordinates(GPS.getLongitude(), GPS.getLatitude());
	}
}
