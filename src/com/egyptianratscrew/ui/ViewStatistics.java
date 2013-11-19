package com.egyptianratscrew.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.User;

/**
 * Activity for viewing the statistics for the currently logged in user
 * 
 * Created 10/17/2013
 * 
 * @author Edward
 * 
 */

public class ViewStatistics extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);

		// A user should be passed to this activity when it is inflated in MainActivity
		// That way we don't have to create one here
		User user = new User();
		//setting the username, email, firstName, and LastName
		user.setUserName("whatsmyusername");
		user.setEmail("edmcn123@gmail.com");
		user.setFirstName("Edward");
		user.setLastName("McNealy");

		//setting the number of wins, losses, ties, games played, and high score
		user.setNumberOfWins(10);
		user.setNumberOfLosses(2);
		user.setNumberOfTies(1);
		user.setTotalGames(13);
		user.setHighScore(50);

		//calling the method displayUserData with the user information
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
	public void displayUserData(User user) {
		
		//setting up the text views
		TextView txtDisplayUserName = (TextView) findViewById(R.id.txtDisplayUserName);
		TextView txtDisplayEmail = (TextView) findViewById(R.id.txtDisplayEmail);
		TextView txtDisplayName = (TextView) findViewById(R.id.txtDisplayName);

		TextView txtDisplayNumberOfWins = (TextView) findViewById(R.id.txtDisplayNumberOfWins);
		TextView txtDisplayNumberOfLosses = (TextView) findViewById(R.id.txtDisplayNumberOfLosses);
		TextView txtDisplayNumberOfTies = (TextView) findViewById(R.id.txtDisplayNumberOfTies);
		TextView txtDisplayTotalGames = (TextView) findViewById(R.id.txtDisplayTotalGames);
		TextView txtDisplayHighScore = (TextView) findViewById(R.id.txtDisplayHighScore);

		//setting the text for username, email, and firstname and lastname through get methods
		txtDisplayUserName.setText(user.getUserName());
		txtDisplayEmail.setText(user.getEmail());
		txtDisplayName.setText(user.getFirstName() + " " + user.getLastName());

		//setting the text for wins, losses, tiesm and games, and high score through get methods
		txtDisplayNumberOfWins.setText(String.valueOf(user.getNumberOfWins()));
		txtDisplayNumberOfLosses.setText(String.valueOf(user.getNumberOfLosses()));
		txtDisplayNumberOfTies.setText(String.valueOf(user.getNumberOfTies()));
		txtDisplayTotalGames.setText(String.valueOf(user.getTotalGames()));
		txtDisplayHighScore.setText(String.valueOf(user.getHighScore()));
	}

}
