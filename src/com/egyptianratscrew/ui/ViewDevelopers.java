package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.DefaultPicture;

/**
 * Setting the activity and viewing the developers
 * 
 * @author Julie
 * 
 */

public class ViewDevelopers extends Activity {

	// creating the developers contentview
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

	/**
	 * Testing the game with only 26 cards for developmental purposes
	 * 
	 * @param v
	 */

	public void testGame(View v) {
		Intent game = new Intent(this, GameActivity.class);
		game.putExtra("Difficulty", 3);
		Bitmap cardBack = DefaultPicture.getImage(this);
		game.putExtra("CardBack", cardBack);
		game.putExtra("test", false);
		startActivity(game);
	}

}
