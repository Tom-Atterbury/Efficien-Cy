package com.coms309.efficiency_coms309_iastate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CreateAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		
		case R.id.home:
			Intent h = new Intent(CreateAccount.this, MainActivity.class);
			startActivity(h);
			return true;
			
		case R.id.settings:
			Intent s = new Intent(CreateAccount.this, Settings.class);
			startActivity(s);
			return true; 
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
