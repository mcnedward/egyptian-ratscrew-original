package com.egyptianratscrew.ui;

import com.egyptianratscrew.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class DeveloperActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.developer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}