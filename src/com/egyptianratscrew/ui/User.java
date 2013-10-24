package com.egyptianratscrew.ui;

import com.egyptianratscrew.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class User extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflating the menu
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

