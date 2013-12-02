package com.egyptianratscrew.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.User;
import com.egyptianratscrew.dao.UserArrayWrapper;

public class PlayGameActivity extends Activity {
	
	private static final int CARD_BACK_REQUEST = 1;
	private int difficulty;
	private Bitmap cardBack;
	private IUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		difficulty = 3;
		cardBack = null;
		user = (User) getIntent().getExtras().getSerializable("User");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_game, menu);
		return true;
	}
	
	//starting activity of game
	public void PlayNewGame(View view) {
		Intent game = new Intent(this, GameActivity.class);
		game.putExtra("Difficulty", difficulty);
		game.putExtra("CardBack", cardBack);
		game.putExtra("Users", new UserArrayWrapper(new IUser[] { user }));
		startActivity(game);
	}
	
	//starting activity of viewRules
	public void ViewRules(View view) {
		Intent viewRules = new Intent(this, ViewRules.class);
		startActivity(viewRules);
	}
	
	//starting activity of viewDevelopers
	public void ViewDevelopers(View view){
		Intent developers = new Intent (this, ViewDevelopers.class);
		startActivity(developers);
	}
	
	public void SelectDifficulty(View v){
		final CharSequence[] items = {
                "Hard", "Medium", "Easy"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Difficulty:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                difficulty = item + 1;
            }
        });
        AlertDialog diffSelect = builder.create();
        diffSelect.show();
	}
	
	public void SelectCardBack(View v){
		Intent cardBack = new Intent(this,CardBackSelectActivity.class);
		startActivityForResult(cardBack,CARD_BACK_REQUEST);
	}
	
	//starting activity of stats
	public void ViewStatistics(View view) {
		Intent stats = new Intent(this, ViewStatistics.class);
		stats.putExtra("User", user);
		startActivity(stats);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			// RESULT_OK means that everything processed successfully.
			
			if (requestCode == CARD_BACK_REQUEST){
				cardBack = (Bitmap) data.getParcelableExtra("IMAGE");
			}
		}
	}

}
