package com.coms309.efficiency_coms309_iastate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Account2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account2);
		
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	EditText username = (EditText) findViewById(R.id.username);
            	String user = username.getText().toString();
            	if (user == null)	user = "";
            	EditText password = (EditText) findViewById(R.id.password);
            	String pass = password.getText().toString();
            	if (pass == null)	pass = "";
            	EditText server = (EditText) findViewById(R.id.server);
            	String serverName = server.getText().toString();
            	if (serverName == null)	serverName = ""; 
            	
            	MainActivity.createLogin(user, pass, serverName);
            	
            	Intent goHome = new Intent(Account2.this, MainActivity.class);
            	startActivity(goHome);
            }
        });
		
		Button newAccount = (Button) findViewById(R.id.create_account);
		newAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
            	Intent createAccount = new Intent(Intent.ACTION_VIEW);
            	createAccount.setData(Uri.parse(url));
            	startActivity(createAccount);
            }
        }); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		
		case R.id.home:
			Intent h = new Intent(Account2.this, MainActivity.class);
			startActivity(h);
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
