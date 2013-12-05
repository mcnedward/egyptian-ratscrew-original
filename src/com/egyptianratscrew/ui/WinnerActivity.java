package com.egyptianratscrew.ui;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;

import com.egyptianratscrew.dao.IUser;

public class WinnerActivity extends Activity {

	/**
	 * creating the winner screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputStream is = null;
		IUser winner = (IUser) this.getIntent().getSerializableExtra("User");
		try {is = getAssets().open("playing_cards_animation.gif");}
		catch (IOException ex) {ex.printStackTrace();}
		setContentView(new WinnerView(this,is, winner));
	}

}
