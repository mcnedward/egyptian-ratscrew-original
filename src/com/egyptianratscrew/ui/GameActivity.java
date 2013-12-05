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

	//creating the content view of game_layout
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
	
	/**
	 * 
	 * @param game
	 */
	private void updateSatistics(Game game){
		IUser winner = null;
		IUser loser = null;
		//player 1 wins the game and set the winner to the winner and loser to player 2
		if (game.player1.hasAllCards()){
			winner = game.player1.getUser();
			loser = game.player2.getUser();
		}
		//player 2 winner and player 1 loser
		else if (game.player2.hasAllCards()){
			winner = game.player2.getUser();
			loser = game.player1.getUser();
		}
		//game tied or not finished
		else {
			Toast t = Toast.makeText(this, "Error, Game Not Finished.",Toast.LENGTH_LONG);
			//handle error
		}
		//setting information about the winner
		winner.setTotalGames(winner.getTotalGames() + 1);
		winner.setNumberOfWins(winner.getNumberOfWins() + 1);
		winner.setCurrentWinningStreak(winner.getCurrentWinningStreak() + 1);
		winner.setCurrentLosingStreak(0);
		if (winner.getHighestWinningStreak() < winner.getCurrentWinningStreak()){
			winner.setHighestWinningStreak(winner.getCurrentWinningStreak());
		}
		//setting information about the loser
		loser.setTotalGames(loser.getTotalGames() +1);
		loser.setNumberOfLosses(loser.getNumberOfLosses() +1);
		loser.setCurrentLosingStreak(loser.getCurrentLosingStreak() + 1);
		loser.setCurrentWinningStreak(0);
		if (loser.getHighestLosingStreak() < loser.getCurrentLosingStreak()){
			loser.setHighestLosingStreak(loser.getCurrentLosingStreak());
		}
		//using the database
		RatscrewDatabase rdb = new RatscrewDatabase(this);
		
		//updating information given the game results
		if (winner.getUserId() != 0) {
			rdb.updateUser(winner);
		}
		if (loser.getUserId() != 0) {
			rdb.updateUser(loser);
		}
		
	}

	//pause the game
	@Override
	protected void onPause() {
		super.onPause();
		tableCardSurface.pause();
	}

	//resume the game where left off
	@Override
	protected void onResume() {
		super.onResume();
		tableCardSurface.resume();
	}
}
