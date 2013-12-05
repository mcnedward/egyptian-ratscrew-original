package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;

public class WinnerActivity extends Activity {

	/**
	 * creating the winner screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new WinnerView(this));
	}

}
