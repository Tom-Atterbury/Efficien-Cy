package com.coms309.efficiency_coms309_iastate;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * 
 * Contains information regarding the settings class, which will relay information
 * to/from the database and EfficienCy
 * 
 * @authors Richard White
 * 
 */

public class Settings extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}

	private String username;
	private String password;
	private String logging;
	
	Settings(String name, String pass, String logg) {
		username = name;
		password = pass;
		logging = logg;
	}
	
	String getUserName() {
		return username;
	}
	
	String getPassword() {
		return password;
	}
	
	String isLogging() {
		return logging;
	}

}
