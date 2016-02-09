package AppInfoRetrieval;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.coms309.efficiency_coms309_iastate.App;

/**
 * Methods regarding retrieval of the current top application running on the
 * device.
 * 
 * @author Tom Atterbury
 * 
 */
public class TopApp {
	private ActivityManager actman;
	private PackageManager pacman;
	private Context con;

	/**
	 * Constructor to specify system links to retrieve app information
	 * 
	 * @param am
	 *            ActivityManager object
	 * @param pm
	 *            PackageManager object
	 */
	public TopApp(Context that) {
		con = that;
		actman = (ActivityManager) con
				.getSystemService(Context.ACTIVITY_SERVICE);
		pacman = con.getPackageManager();
	}

	/**
	 * Returns the top app (name, icon, and description) for input to the
	 * database.
	 * 
	 * @return an App object with the name, icon, and description set
	 */
	public App getTopApp() {

		String appName = "";
		String appDescription = "";

		// Get the top app process information
		RunningAppProcessInfo topProcInfo = actman.getRunningAppProcesses()
				.get(0);

		String topAppProcessName = topProcInfo.processName;

		// Get the top app application information
		ApplicationInfo topAppInfo;
		try {
			topAppInfo = pacman.getApplicationInfo(topProcInfo.processName,
					PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			// Could not find process in package manager
			topAppInfo = null;
		}

		// Get app specific information if the information exists
		if (topAppInfo != null) {
			// Get the name of the top application
			appName = pacman.getApplicationLabel(topAppInfo) + "";

			// Get the description of the top application
			CharSequence appDesc = topAppInfo.loadDescription(pacman);
			if (appDesc != null)
				appDescription = appDesc + "";
			else
				appDescription = appName;
		}

		Log.d("TopApp", appName + " Description: " + appDescription);

		// Get the icon of the top application
		Bitmap appIcon;
		try {
			appIcon = drawableToBitmap(pacman
					.getApplicationIcon(topAppProcessName));
		} catch (PackageManager.NameNotFoundException e) {
			// Could not find process in package manager
			appIcon = null;
		}

		// Create an App object with the information gathered from the top
		// application
		App topApp = new App(appName, "", 1, appIcon, appDescription);

		// Return the App
		return topApp;
	}

	/**
	 * Convert a drawable object to a Bitmap object
	 * 
	 * @param drawable
	 *            Drawable type object to convert to Bitmap
	 * @return Bitmap type object
	 */
	private static Bitmap drawableToBitmap(Drawable drawable) {

		// Get height and width information
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();

		// Ensure width and height are valid values
		if (width <= 0)
			width = 1;
		if (height <= 0)
			height = 1;

		// Create a Bitmap object with the same width and height as the drawable
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		// Create a canvas from the Bitmap
		Canvas canvas = new Canvas(bitmap);

		// Make sure the drawable bounds are set appropriately
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

		// Draw the drawable on the canvas, setting the Bitmap image
		drawable.draw(canvas);

		// Return the completed Bitmap
		return bitmap;
	}
}
