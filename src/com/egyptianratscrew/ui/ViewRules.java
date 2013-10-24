package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.egyptianratscrew.R;

/**
 * Activity for viewing the rules to Egyptian Ratscrew
 * 
 * Created 9/12/13
 * 
 * @author Edward
 * 
 */

public class ViewRules extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rules);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
