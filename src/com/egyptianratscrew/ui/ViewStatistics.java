package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IUser;

/**
 * Activity for viewing the statistics for the currently logged in user
 * 
 * Created 10/17/2013
 * 
 * @author Edward
 * 
 */

public class ViewStatistics extends Activity {
	private final static String TAG = "ViewStatistics";

	private IUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);

		user = MainActivity.user;
		displayUserData(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Displays the statistics for a user.
	 * 
	 * @param user
	 *            - The user who's data you want to view
	 */
	public void displayUserData(IUser user) {

		// setting up the text views
		TextView txtDisplayUserName = (TextView) findViewById(R.id.txtDisplayUserName);
		TextView txtDisplayEmail = (TextView) findViewById(R.id.txtDisplayEmail);
		TextView txtDisplayName = (TextView) findViewById(R.id.txtDisplayName);

		TextView txtDisplayNumberOfWins = (TextView) findViewById(R.id.txtDisplayNumberOfWins);
		TextView txtDisplayNumberOfLosses = (TextView) findViewById(R.id.txtDisplayNumberOfLosses);
		TextView txtDisplayNumberOfTies = (TextView) findViewById(R.id.txtDisplayNumberOfTies);
		TextView txtDisplayTotalGames = (TextView) findViewById(R.id.txtDisplayTotalGames);
		TextView txtDisplayHighScore = (TextView) findViewById(R.id.txtDisplayHighScore);
		TextView txtDisplayHighestWinningStreak = (TextView) findViewById(R.id.txtDisplayHighestWinningStreak);
		TextView txtDisplayHighestLosingStreak = (TextView) findViewById(R.id.txtDisplayHighestLosingStreak);

		// setting the text for user name, email, and first name and last name through get methods
		txtDisplayUserName.setText(user.getUserName());
		txtDisplayEmail.setText(user.getEmail());
		txtDisplayName.setText(user.getFirstName() + " " + user.getLastName());

		// setting the text for wins, losses, ties, and games, and high score through get methods
		txtDisplayNumberOfWins.setText(String.valueOf(user.getNumberOfWins()));
		txtDisplayNumberOfLosses.setText(String.valueOf(user.getNumberOfLosses()));
		txtDisplayHighestWinningStreak.setText(String.valueOf(user.getHighestWinningStreak()));
		txtDisplayHighestLosingStreak.setText(String.valueOf(user.getHighestLosingStreak()));
		txtDisplayTotalGames.setText(String.valueOf(user.getTotalGames()));
	}

}
