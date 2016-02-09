package com.coms309.efficiency_coms309_iastate;

import android.app.Application;
import android.content.Context;


/**
* ContextProvider
* 
* Purpose is to allow application context to be retrieved from anywhere
* 
* Call:
* ContextProvider.getContext();
* 
* @author James Saylor
*/

public class ContextProvider extends Application {
	private static Context myContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		myContext = getApplicationContext();
	}
	
	public static Context getContext() {
		return myContext;
	}
}
