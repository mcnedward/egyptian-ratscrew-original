package com.egyptianratscrew.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.egyptianratscrew.dao.IUser;

public class WinnerActivity extends Activity {

	private static final int DELAY_INTERVAL = 5000;
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
		
		Timer winnerDisplayTimer = new Timer();
		winnerDisplayTimer.schedule(new WinnerDisplayTask(this), DELAY_INTERVAL);
	}
	class WinnerDisplayTask extends TimerTask {

		private Context mContext;
		public WinnerDisplayTask(Context context) {
			mContext = context;
		}

		@Override
		public void run() {
			Intent playGameIntent = new Intent(mContext, PlayGameActivity.class);
			playGameIntent.putExtra("User", MainActivity.loggedInUser);
			startActivity(playGameIntent);
		}
	}
}

