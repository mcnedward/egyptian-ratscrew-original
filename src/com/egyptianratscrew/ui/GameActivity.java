package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IGameFinishedListener;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.IPlayer;
import com.egyptianratscrew.service.Game;
import com.egyptianratscrew.service.GameSurface;

public class GameActivity extends Activity implements IGameFinishedListener {

	// declaring variables
	private RelativeLayout table;
	private GameSurface tableCardSurface;
	RatscrewDatabase db;
	private Game game;
	private Context context;
	private Bitmap cardBack;
	private IUser user;
	private boolean realdeck;

	//two player not implemented
	private Boolean onePlayer = true;

	// creating the content view of game_layout
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		realdeck = (boolean) this.getIntent().getBooleanExtra("RealDeck", true);
		
		// using the database
		db = new RatscrewDatabase(this);

		cardBack = (Bitmap) this.getIntent().getParcelableExtra("CardBack");
		// Check if there was a user intent passed to this activity
		user = null;
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey("User")) {
				user = (User) extras.getSerializable("User");

				// TODO: Do something with the value of isNew.
			}
			if (extras.containsKey("test")) {
				onePlayer = false;
			}
		}
		StartGame();

	}

	private void StartGame() {
		context = this;
		game = new Game(onePlayer, user, 3, realdeck, cardBack, this);
		game.registerGameFinishedListener(this);

		// setting the relative layout of table
		table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this, game);
		// adding the tableCardSurface
		table.addView(tableCardSurface);
	}

	@Override
	public void onGameFinished(final Game game) {
		// runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		//
		// }
		// });
		updateStatistics(game);
		Intent winnerIntent = new Intent(this, WinnerActivity.class);
		winnerIntent.putExtra("Player", game.player1);
		startActivity(winnerIntent);
	}

	/**
	 * 
	 * @param game
	 *            to update the Statistic of the game
	 */
	private void updateStatistics(Game game) {
		// player 1 wins the game and set the winner to the winner and loser to player 2
		if (game.player1.hasAllCards()
				|| (game.player1.getHand().size() + game.theStack.size()) == CardDeck.DeckSize()) {
			db.userWins(game.player1.getUser());
		}
		// player 2 winner and player 1 loser
		else if (game.player2.hasAllCards()
				|| (game.player2.getHand().size() + game.theStack.size()) == CardDeck.DeckSize()) {
			db.userLoses(game.player2.getUser());
		}
		// game not finished
		else {
			Toast t = Toast.makeText(this, "Error, Game Not Finished.", Toast.LENGTH_LONG);
			t.show();
			// handle error
			StartGame();
		}
		// setting information about the winner
		// winner.setTotalGames(winner.getTotalGames() + 1);
		// winner.setNumberOfWins(winner.getNumberOfWins() + 1);
		// winner.setCurrentWinningStreak(winner.getCurrentWinningStreak() + 1);
		// winner.setCurrentLosingStreak(0);
		// if (winner.getHighestWinningStreak() < winner.getCurrentWinningStreak()) {
		// winner.setHighestWinningStreak(winner.getCurrentWinningStreak());
		// }
		// // setting information about the loser
		// loser.setTotalGames(loser.getTotalGames() + 1);
		// loser.setNumberOfLosses(loser.getNumberOfLosses() + 1);
		// loser.setCurrentLosingStreak(loser.getCurrentLosingStreak() + 1);
		// loser.setCurrentWinningStreak(0);
		// if (loser.getHighestLosingStreak() < loser.getCurrentLosingStreak()) {
		// loser.setHighestLosingStreak(loser.getCurrentLosingStreak());
		// }
		//
		// // updating information given the game results
		// if (winner.getUserId() != 0) {
		// db.updateUser(winner);
		// }
		// if (loser.getUserId() != 0) {
		// db.updateUser(loser);
		// }

	}

	// pause the game
	@Override
	protected void onPause() {
		super.onPause();
		tableCardSurface.pause();
	}

	// resume the game where left off
	@Override
	protected void onResume() {
		super.onResume();
		tableCardSurface.resume();
	}
}
