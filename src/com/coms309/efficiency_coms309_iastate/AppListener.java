package com.coms309.efficiency_coms309_iastate;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

/**
 * AppListener
 * 
 * Listens for app focus changes and records times as appropriate.
 * 
 * @author Tom Atterbury
 * 
 */
public class AppListener implements ActivityLifecycleCallbacks {

	private static AppListener instance;
	private static MainActivity main;

	public static AppListener init(Application application,
			MainActivity mainLink) {
		if (instance == null) {
			instance = new AppListener();
			application.registerActivityLifecycleCallbacks(instance);
		}
		main = mainLink;
		return instance;
	}

	@Override
	public void onActivityCreated(Activity arg0, Bundle arg1) {
		// Occurs when an app first starts? Does nothing for now
		/*
		 * Old Code: Dialog d = new Dialog(arg0); d.setTitle("App Created");
		 * TextView tv = new TextView(arg0); tv.setText("Created");
		 * d.setContentView(tv); d.show();
		 */
	}

	@Override
	public void onActivityDestroyed(Activity arg0) {
		// Occurs when a process is completely killed all dogs? Does nothing for
		// now
	}

	@Override
	public void onActivityPaused(Activity arg0) {
		// Record the time the application leaves the screen (goes to
		// background)
	}

	@Override
	public void onActivityResumed(Activity arg0) {
		// Reset the start time for tracking
		main.resetStartTime();
		main.updateDate();
		// TODO: Update app event before listing apps
		// Update list of apps on resume
		main.appLister();
	}

	@Override
	public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
		// Not sure when this is triggered, does nothing
	}

	@Override
	public void onActivityStarted(Activity arg0) {
		// Record the time the application starts? (enters foreground)
	}

	@Override
	public void onActivityStopped(Activity arg0) {
		// Record the time the application is closed? (process is killed)
	}

}
