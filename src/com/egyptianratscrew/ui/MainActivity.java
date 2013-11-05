package com.egyptianratscrew.ui;


import com.egyptianratscrew.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * This page is the Main Menu for the Egyptian Ratscrew game
 * Contains methods for:
 * - Viewing rules activity
 * - Starting a new game against AI
 * - Starting a new game against a human
 * 
 * Created 9/12/13
 * 
 * @author Edward
 * 
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void viewRegister(View view){
	Intent viewRegistration = new Intent(this, ViewRegister.class);
	startActivity(viewRegistration);
	}
	
	public void viewRules(View view) {
		Intent viewRules = new Intent(this, ViewRules.class);
		startActivity(viewRules);
	}

	public void startNewGame(View view) {
		Intent game = new Intent(this, GameActivity.class);
		startActivity(game);
	}
	
	public void viewStatistics(View view) {
		Intent stats = new Intent(this, ViewStatistics.class);
		startActivity(stats);
	}
	
	public void fbLogin(View view) {
		Intent fblogin = new Intent(this, fbLoginActivity.class);
		startActivity(fblogin);
	}

}
