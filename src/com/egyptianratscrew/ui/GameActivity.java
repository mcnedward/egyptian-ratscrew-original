package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IGameFinishedListener;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.UserArrayWrapper;
import com.egyptianratscrew.service.Game;
import com.egyptianratscrew.service.GameSurface;

public class GameActivity extends Activity implements IGameFinishedListener {

	// declaring variables
	private RelativeLayout table;
	private GameSurface tableCardSurface;
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		// UserArrayWrapper wrapper = (UserArrayWrapper) this.getIntent().getSerializableExtra("Users");

		// game = new Game(true, this.getIntent().getIntExtra("Difficulty",3),
		// wrapper.getUsers(),
		// this);
		// {this.getIntent().getStringExtra(Player1Name),this);

		game = new Game(true, 3, this);

		game.registerGameFinishedListener(this);

		// setting the relative layout of table
		table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this, game);
		// adding the tableCardSurface
		table.addView(tableCardSurface);

	}

	@Override
	public void onGameFinished(Game game) {
		// runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// }
		// });
		Toast t = Toast.makeText(this, "yay", Toast.LENGTH_LONG);
		t.show();
	}
	
	private void updateSatistics(Game game){
		IUser winner = null;
		IUser loser = null;
		
		if (game.player1.hasAllCards()){
			winner = game.player1.getUser();
			loser = game.player2.getUser();
		}
		else if (game.player2.hasAllCards()){
			winner = game.player2.getUser();
			loser = game.player1.getUser();
		}
		else {
			Toast t = Toast.makeText(this, "Error, Game Not Finished.",Toast.LENGTH_LONG);
			//handle error
		}
		
		winner.setTotalGames(winner.getTotalGames() + 1);
		winner.setNumberOfWins(winner.getNumberOfWins() + 1);
		winner.setCurrentWinningStreak(winner.getCurrentWinningStreak() + 1);
		winner.setCurrentLosingStreak(0);
		if (winner.getHighestWinningStreak() < winner.getCurrentWinningStreak()){
			winner.setHighestWinningStreak(winner.getCurrentWinningStreak());
		}
		
		loser.setTotalGames(loser.getTotalGames() +1);
		loser.setNumberOfLosses(loser.getNumberOfLosses() +1);
		loser.setCurrentLosingStreak(loser.getCurrentLosingStreak() + 1);
		loser.setCurrentWinningStreak(0);
		if (loser.getHighestLosingStreak() < loser.getCurrentLosingStreak()){
			loser.setHighestLosingStreak(loser.getCurrentLosingStreak());
		}
		
		RatscrewDatabase rdb = new RatscrewDatabase(this);
		
		if (winner.getUserId() != 0) {
			rdb.updateUser(winner);
		}
		if (loser.getUserId() != 0) {
			rdb.updateUser(loser);
		}
		
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
