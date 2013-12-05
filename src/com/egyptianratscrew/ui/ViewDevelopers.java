package com.egyptianratscrew.ui;


import com.egyptianratscrew.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Setting the activity and viewing the developers
 * @author Julie
 *
 */

public class ViewDevelopers extends Activity {

	//creating the developers contentview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.developers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

