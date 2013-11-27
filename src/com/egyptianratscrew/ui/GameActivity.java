package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.UserArrayWrapper;
import com.egyptianratscrew.service.Game;
import com.egyptianratscrew.service.GameSurface;

public class GameActivity extends Activity {

	// declaring variables
	private RelativeLayout table;
	private GameSurface tableCardSurface;
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

//		game = new Game(true, this.getIntent().getIntExtra("Difficulty",3), 
//				(String[])this.getIntent().getCharSequenceArrayExtra("Names"),
//				this);
		// {this.getIntent().getStringExtra(Player1Name),this);
		UserArrayWrapper wrapper = (UserArrayWrapper) this.getIntent().getSerializableExtra("Users");
		game = new Game(true, 3, wrapper.getUsers() , this);

		// setting the relative layout of table
		table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this, game);
		// adding the tableCardSurface
		table.addView(tableCardSurface);

	}

	@Override
	protected void onPause() {
		super.onPause();
		tableCardSurface.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		tableCardSurface.resume();
	}
}
