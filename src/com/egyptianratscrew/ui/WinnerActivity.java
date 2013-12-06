package com.egyptianratscrew.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.egyptianratscrew.dto.IPlayer;

public class WinnerActivity extends Activity {

	private static final int DELAY_INTERVAL = 5000;
	/**
	 * creating the winner screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputStream is = null;
		IPlayer player1 = (IPlayer) this.getIntent().getSerializableExtra("Player");
//		try {is = getAssets().open("playing_cards_animation.gif");}
//		catch (IOException ex) {ex.printStackTrace();}
		setContentView(new WinnerView(this,is, player1));
		
		Timer winnerDisplayTimer = new Timer();
		winnerDisplayTimer.schedule(new WinnerDisplayTask(this,this), DELAY_INTERVAL);
	}
	class WinnerDisplayTask extends TimerTask {

		private Context mContext;
		private Activity activity;
		public WinnerDisplayTask(final Activity activity, Context context) {
			mContext = context;
			this.activity = activity;
		}

		@Override
		public void run() {
			Intent playGameIntent = new Intent(mContext, PlayGameActivity.class);
			playGameIntent.putExtra("User", MainActivity.loggedInUser);
			activity.finish();
			startActivity(playGameIntent);
		}
	}
}

