package com.coms309.efficiency_coms309_iastate;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An adaptor class to create the app list on the main screen
 * 
 * @authors Niklas Jorve, James Saylor, Tom Atterbury
 * 
 */
public class MyAdaptor extends BaseExpandableListAdapter {

	private static final int APP_NAME_WIDTH = 200;
	// private static final int NORM_WIDTH = 480;
	private static final int ICON_SIZE = 50;
	private static final int FONT_SIZE = 30;
	private static final int FONT_COLOR = Color.WHITE;
	// private static final int BUTTON_WIDTH = 300;
	// private static final int BUTTON_HEIGHT = 50;
	// private static final int FONT_COMPLEX_UNIT_TYPE =
	// TypedValue.COMPLEX_UNIT_SP;
	private float scale_factor;
	private float scaled_font_size;

	/**
	 * Context of the adaptor
	 */
	private Context context;

	/**
	 * Parent array to represent the names of the apps
	 */
	private String[] parent;

	/**
	 * Child array to store the description of the apps
	 */
	private String[][] child;

	/**
	 * Percentage of use by app
	 */
	private int[] percentages;

	/**
	 * Total time use by app
	 */
	private long[] times;

	/**
	 * String that represents the value of the radio button
	 */
	private String timeSelection;

	/**
	 * Picture to use for icon
	 */
	private Bitmap[] icons;

	/**
	 * Productivity Rating for apps
	 */
	private int[] productivity;

	/**
	 * Last Coordinates for apps
	 */
	private Coordinates[] coords;

	/**
	 * Main constructor
	 * 
	 * @param context
	 * @param parent
	 * @param child
	 */
	public MyAdaptor(Context context, String[] parent, String[][] child) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = new int[parent.length];
		this.times = new long[parent.length];
		this.icons = new Bitmap[parent.length];
		this.timeSelection = "day";
		this.productivity = new int[parent.length];
		this.coords = new Coordinates[parent.length];

		Random random = new Random();
		// Arrays.fill(percentages, random.nextInt(100));
		for (int i = 0; i < percentages.length; i++) {
			percentages[i] = random.nextInt(100);
			times[i] = (Math.abs(random.nextLong()) % 86399);
			productivity[i] = 1;
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}

		Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android_icon);

		Arrays.fill(icons, icon);
		setGUIScaleFactor();
	}

	public MyAdaptor(Context context, String[] parent, String[][] child,
			int[] percentages, Bitmap[] icons) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = percentages;
		this.times = new long[parent.length];
		this.icons = icons;
		this.timeSelection = "day";
		this.productivity = new int[parent.length];
		this.coords = new Coordinates[parent.length];

		Random random = new Random();
		for (int i = 0; i < times.length; i++) {
			times[i] = (random.nextLong() % 86399);
			productivity[i] = 1;
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
		setGUIScaleFactor();
	}

	public MyAdaptor(Context context, String[] parent, String[][] child,
			int[] percentages, long[] times, Bitmap[] icons) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = percentages;
		this.times = times;
		this.icons = icons;
		this.timeSelection = "day";
		this.coords = new Coordinates[parent.length];

		Random random = new Random();
		for (int i = 0; i < coords.length; i++) {
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
		setGUIScaleFactor();
	}

	// The main constructor that should be called in final product
	public MyAdaptor(Context context, String[] parent, String[][] child,
			int[] percentages, long[] times, Bitmap[] icons, int[] productivity) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = percentages;
		this.times = times;
		this.icons = icons;
		this.timeSelection = "day";
		this.productivity = productivity;
		this.coords = new Coordinates[parent.length];
		Random random = new Random();
		for (int i = 0; i < coords.length; i++) {
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
		setGUIScaleFactor();
	}

	public MyAdaptor(Context context, String[] parent, String[][] child,
			Bitmap[] icons) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.icons = icons;
		this.percentages = new int[parent.length];
		this.times = new long[parent.length];
		this.timeSelection = "day";
		this.productivity = new int[parent.length];
		this.coords = new Coordinates[parent.length];

		Random random = new Random();
		// Arrays.fill(percentages, random.nextInt(100));
		for (int i = 0; i < percentages.length; i++) {
			percentages[i] = random.nextInt(100);
			times[i] = (Math.abs(random.nextLong()) % 86399);
			productivity[i] = 1;
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
		setGUIScaleFactor();
	}

	public MyAdaptor(Context context, String[] parent, String[][] child,
			Bitmap[] icons, int[] percentages) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.icons = icons;
		this.percentages = percentages;
		this.timeSelection = "day";
		this.productivity = new int[parent.length];
		this.coords = new Coordinates[parent.length];
		setGUIScaleFactor();

		Random random = new Random();
		for (int i = 0; i < percentages.length; i++) {
			productivity[i] = 1;
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
	}

	public MyAdaptor(Context context, String[] parent, String[][] child,
			Bitmap[] icons, int[] percentages, long[] totalTimes) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.icons = icons;
		this.percentages = percentages;
		this.timeSelection = "day";
		this.productivity = new int[parent.length];
		this.coords = new Coordinates[parent.length];
		setGUIScaleFactor();

		Random random = new Random();
		for (int i = 0; i < percentages.length; i++) {
			productivity[i] = 1;
			Coordinates newCoords = new Coordinates(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
			coords[i] = newCoords;
		}
	}

	/**
	 * Constructor for testing purposes
	 * 
	 * @param context
	 */
	public MyAdaptor(Context context) {
		this.context = context;
		this.timeSelection = "day";
		setGUIScaleFactor();
	}

	/**
	 * Child getter method
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.child[groupPosition][childPosition];
	}

	/**
	 * Child ID getter method
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * Changes the view of the child
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater layoutinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutinflater.inflate(R.layout.list_child, null);
		}

		setChildViewText(convertView, groupPosition, childPosition);

		return convertView;
	}

	/**
	 * The main function for populating the child view of the Expandable list
	 * view
	 * 
	 * @param convertView
	 * @param groupPosition
	 * @param childPosition
	 */
	public void setChildViewText(View convertView, int groupPosition,
			int childPosition) {
		ViewGroup.LayoutParams params;
		TextView childText = (TextView) convertView
				.findViewById(R.id.list_child);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView percent = (TextView) convertView.findViewById(R.id.percent);
		TextView share = (TextView) convertView.findViewById(R.id.share);
		childText.setText(child[groupPosition][childPosition]);

		// Convert time from seconds to day/hour/min/sec format (e.g.,
		// 1d 13h 50m 20s)
		String timeText = timeToString(times[groupPosition]);
		time.setText("Time: " + timeText);
		// time.setTextSize(30);
		// time.setTextColor(Color.WHITE);
		time.setTextSize(scaled_font_size);
		time.setTextColor(FONT_COLOR);

		percent.setText("Percentage: " + percentages[groupPosition] + "%");
		// percent.setTextSize(30);
		// percent.setTextColor(Color.WHITE);
		percent.setTextSize(scaled_font_size);
		percent.setTextColor(FONT_COLOR);

		share.setText("Share:");
		// share.setTextSize(30);
		// share.setTextColor(Color.WHITE);
		share.setTextSize(scaled_font_size);
		share.setTextColor(FONT_COLOR);

		Button mapButton = (Button) convertView.findViewById(R.id.map_button);
		Button productivityButton = (Button) convertView
				.findViewById(R.id.productivity_button);

		Button settingsButton = (Button) convertView
				.findViewById(R.id.settings_button);
		settingsButton.setBackgroundResource(R.drawable.settings_button_2);

		final int groupNumber = groupPosition;

		// Scale productivity display button
		// params =productivityButton.getLayoutParams();
		// productivityButton.setTextSize(scaled_font_size);
		// params.width = (int) (BUTTON_WIDTH * scale_factor);
		// params.height = (int) (BUTTON_HEIGHT * scale_factor);
		// productivityButton.setLayoutParams(params);

		productivityButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (productivity[groupNumber] == 1) {
					Toast.makeText(
							context.getApplicationContext(),
							parent[groupNumber]
									+ " is classified as a productive app",
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(
							context.getApplicationContext(),
							parent[groupNumber]
									+ " is classified as an unproductive app",
							Toast.LENGTH_LONG).show();
			}
		});

		// Scale map display button
		// params = mapButton.getLayoutParams();
		// mapButton.setTextSize(scaled_font_size);
		// params.width = (int) (BUTTON_WIDTH * scale_factor);
		// params.height = (int) (BUTTON_HEIGHT * scale_factor);
		// mapButton.setLayoutParams(params);

		mapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				// GET LOCATION DATA FROM APP
				// CREATE A STRING

				Log.d("mapButton",coords[groupNumber] + "");
				Log.d("mapButton",coords[groupNumber].latitude + "");
				
				float key1 = (float) coords[groupNumber].latitude;
				float key2 = (float) coords[groupNumber].longitude;
				
				Log.d("Latitude", String.valueOf(coords[groupNumber].latitude));
				Log.d("Longitude", String.valueOf(coords[groupNumber].longitude));

				
				Log.d("mapButton", key1 + " x " + key2);
				
				String uri = String.format(Locale.ENGLISH,
						"geo:%f,%f?z=17&q=%f,%f", key1, key2, key1, key2);
				
				Log.d("mapButton", uri);
				
				Intent map = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				context.startActivity(map);
			}
		});

		// Scale productivity setting button
		params = settingsButton.getLayoutParams();
		params.width = (int) (ICON_SIZE * scale_factor);
		params.height = (int) (ICON_SIZE * scale_factor);
		settingsButton.setLayoutParams(params);

		settingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						context);

				// Setting Dialog Title
				alertDialog.setTitle("Set Productivity Raiting");

				// Setting Dialog Message
				alertDialog
						.setMessage("Please set the Productivity Rating for "
								+ parent[groupNumber]);

				// on pressing No button
				alertDialog.setNegativeButton("Productive",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								productivity[groupNumber] = 1;
								MainActivity.setProductivityRating(
										parent[groupNumber], 1);
								dialog.cancel();
							}
						});

				// On pressing Yes button
				alertDialog.setPositiveButton("Not Productive",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								productivity[groupNumber] = 0;
								MainActivity.setProductivityRating(
										parent[groupNumber], 0);
								dialog.dismiss();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

		ImageView twitter = (ImageView) convertView.findViewById(R.id.twitter);
		twitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				String tweetUrl = "https://twitter.com/intent/tweet?text=I spent "
						+ percentages[groupNumber]
						+ " percent of my time on "
						+ parent[groupNumber]
						+ " in the past "
						+ timeSelection
						+ "!"
						+ "\n"
						+ "Check out your productivity with &hashtags=EfficienCy"
						+ "!";
				Uri uri = Uri.parse(tweetUrl);
				context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});

		ImageView myspace = (ImageView) convertView.findViewById(R.id.myspace);
		myspace.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Toast.makeText(context.getApplicationContext(),
						"Nobody uses MySpace anymore, grow up!",
						Toast.LENGTH_LONG).show();
			}
		});

	}

	/**
	 * Returns the child count based on the group position
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return child[groupPosition].length;
	}

	/**
	 * Returns the group based on the group position
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	/**
	 * Returns the length of the parent array
	 */
	@Override
	public int getGroupCount() {
		return parent.length;
	}

	/**
	 * Returns the ID of the group based on position
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Changes the view of the parent
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater layoutinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutinflater.inflate(R.layout.list_parent_relative,
					null);
		}

		// Set the image in the expandable list
		setImageView(convertView, groupPosition);

		// Set the progress bar in the expandable list
		setProgressBar(convertView, groupPosition);

		// Set the parent text in the expandable list view
		setParentText(convertView, groupPosition);

		// Set the percentages in the expandable list view
		setPercentages(convertView, groupPosition);

		return convertView;
	}

	/**
	 * Set the progress bar
	 * 
	 * @param convertView
	 * @param groupPosition
	 */
	private void setProgressBar(View convertView, int groupPosition) {
		ProgressBar progressBar = (ProgressBar) convertView
				.findViewById(R.id.progress_bar_relative);
		progressBar.setMax(100);

		if (percentages[groupPosition] > 0) {
			progressBar.setProgress(percentages[groupPosition]);
		} else {
			progressBar.setProgress(0);
		}

		if (percentages[groupPosition] <= 50) {
			progressBar.getProgressDrawable().setColorFilter(Color.GREEN,
					Mode.MULTIPLY);
		} else if (percentages[groupPosition] <= 75) {
			progressBar.getProgressDrawable().setColorFilter(
					Color.parseColor("#FFFF99"), Mode.MULTIPLY);
		} else {
			progressBar.getProgressDrawable().setColorFilter(Color.RED,
					Mode.MULTIPLY);
		}
	}

	/**
	 * Set the percentage next to the progress bar
	 * 
	 * @param convertView
	 * @param groupPosition
	 */
	private void setPercentages(View convertView, int groupPosition) {
		TextView percentage = (TextView) convertView
				.findViewById(R.id.percentage);

		if (percentages[groupPosition] < 10) {
			percentage.setText("0"
					+ String.valueOf(percentages[groupPosition] + "%"));
		} else if (percentages[groupPosition] > 99) {
			percentage.setText("99" + "%");
		} else {
			percentage
					.setText(String.valueOf(percentages[groupPosition] + "%"));
		}
		// percentage.setTextSize(30);
		percentage.setTextSize(scaled_font_size);
		// percentage.setTextSize(FONT_COMPLEX_UNIT_TYPE, scaled_font_size);
	}

	/**
	 * Sets the names of the apps in the expandable list view
	 * 
	 * @param convertView
	 * @param groupPosition
	 */
	private void setParentText(View convertView, int groupPosition) {
		TextView parentText = (TextView) convertView
				.findViewById(R.id.list_parent_relative);

		if (this.parent[groupPosition].length() <= 10) {
			parentText.setText(this.parent[groupPosition]);
		} else {
			parentText.setText(this.parent[groupPosition].substring(0, 8)
					+ "...");
		}
		// parentText.setTextSize(30);
		parentText.setTextSize(25);

		// parentText.setTextSize(FONT_COMPLEX_UNIT_TYPE, scaled_font_size);

		ViewGroup.LayoutParams params = parentText.getLayoutParams();
		params.width = (int) (APP_NAME_WIDTH * scale_factor);
		parentText.setLayoutParams(params);

	}

	/**
	 * Sets the icons of the apps in the expandable list view
	 * 
	 * @param convertView
	 * @param groupPosition
	 */
	private void setImageView(View convertView, int groupPosition) {
		ImageView image = (ImageView) convertView.findViewById(R.id.appIcon);

		// Get a copy of the current layout parameters
		// ViewGroup.LayoutParams params = image.getLayoutParams();

		// Adjust for device resolution and density
		// params.height = (int) (ICON_SIZE * scale_factor);
		// params.width = (int) (ICON_SIZE * scale_factor);

		// Set new parameters
		// image.setLayoutParams(params);

		if (icons[groupPosition] == null) {
			Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.android_icon);
			image.setImageBitmap(icon);
		} else {
			image.setImageBitmap(icons[groupPosition]);
		}

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void setPercentage(int groupPosition, int percent) {
		if (groupPosition >= percentages.length) {
			return;
		} else
			percentages[groupPosition] = percent;
	}

	public void setPercentages(int[] percentages) {
		this.percentages = percentages;
	}

	public void setTime(int groupPosition, long time) {
		if (groupPosition >= times.length) {
			return;
		} else
			times[groupPosition] = time;
	}

	public void setTimes(long[] times) {
		this.times = times;
	}

	public void setIcon(int groupPosition, Bitmap icon) {
		if (groupPosition >= icons.length) {
			return;
		} else {
			icons[groupPosition] = icon;
		}
	}

	public void setIcons(Bitmap[] icons) {
		for (int i = 0; i < icons.length; i++) {
			this.icons[i] = icons[i];
		}
	}

	public void setTimeSelection(String time) {
		timeSelection = time;
	}

	public String getTimeSelection() {
		return timeSelection;
	}

	/**
	 * Set the scale factor to scale GUI components
	 */
	private void setGUIScaleFactor() {
		// DisplayMetrics metrics = this.context.getResources()
		// .getDisplayMetrics();

		// scale_factor = metrics.widthPixels / (float) NORM_WIDTH;
		scale_factor = (float) 1.0;
		scaled_font_size = FONT_SIZE * scale_factor;
	}

	// Used by MainActivity to update all info, but keep time selection
	public void setAdaptor(Context context, String[] parent, String[][] child,
			int[] percentages, long[] times, Bitmap[] icons) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = percentages;
		this.times = times;
		this.icons = icons;
		// this.productivity = productivity;
		setGUIScaleFactor();
	}

	// Used by MainActivity to update all info, but keep time selection
	public void setAdaptor(Context context, String[] parent, String[][] child,
			int[] percentages, long[] times, Bitmap[] icons,
			int[] productivity, Coordinates[] coords) {
		this.context = context;
		this.parent = parent;
		this.child = child;
		this.percentages = percentages;
		this.times = times;
		this.icons = icons;
		this.productivity = productivity;
		this.coords = coords;
		setGUIScaleFactor();
	}

	public String timeToString(long myTime) {
		// Converts number of seconds to day/hour/min/sec format
		// XXd YYh ZZm QQs
		String timeText = "";
		int days = 0, hours = 0, mins = 0, secs = 0;
		if (myTime >= 86400) {
			days = (int) myTime / 86400;
			if (days != 0) {
				timeText = timeText.concat(String.valueOf(days) + "d");
			}
		}
		if (myTime >= 3600) {
			hours = ((int) myTime / 3600) % 24;
			if (hours != 0) {
				if (timeText.length() != 0) {
					timeText = timeText.concat(" " + String.valueOf(hours)
							+ "h");
				} else {
					timeText = timeText.concat(String.valueOf(hours) + "h");
				}
			}
		}
		if (myTime >= 60) {
			mins = ((int) myTime / 60) % 60;
			if (mins != 0) {
				if (timeText.length() != 0) {
					timeText = timeText
							.concat(" " + String.valueOf(mins) + "m");
				} else {
					timeText = timeText.concat(String.valueOf(mins) + "m");
				}
			}
		}
		secs = ((int) myTime % 60);
		if (secs != 0) {
			if (timeText.length() != 0) {
				timeText = timeText.concat(" " + String.valueOf(secs) + "s");
			} else {
				timeText = timeText.concat(String.valueOf(secs) + "s");
			}
		}

		if (timeText == "") {
			timeText = "0s";
		}

		return timeText;
	}
}
