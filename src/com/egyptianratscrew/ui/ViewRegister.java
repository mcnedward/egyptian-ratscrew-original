package com.egyptianratscrew.ui;


import com.egyptianratscrew.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 *Egyptian Ratscrew
 * 
 * Created 9/12/13
 * 
 * @author Julie
 * 
 */

public class ViewRegister extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}