package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;

/**
 * This page is the Main Menu for the Egyptian Ratscrew game
 * Contains methods for:
 * - Viewing rules activity
 * - Starting a new game against AI
 * - Starting a new game against a human
 * 
 * Created 9/12/13
 * 
 * @author Edward
 * 
 */

public class MainActivity extends Activity {

	private static final int NEW_USER_REQUEST = 2;

	private static final int FB_REQUEST = 1;
	
	private RatscrewDatabase rdb;
	private User loggedInUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rdb = new RatscrewDatabase(this);
		loggedInUser = null;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//starting activity of stats
	public void viewStatistics(View view) {
		Intent stats = new Intent(this, ViewStatistics.class);
		startActivity(stats);
	}

	//starting activity of fbLogin
	public void fbLogin(View view) {
		Intent fblogin = new Intent(this, fbLoginActivity.class);
		startActivityForResult(fblogin,FB_REQUEST);
	}

	
	//starting activity of user
	public void startNewUser(View view) {
		Intent user = new Intent(this, RegisterActivity.class);
		startActivityForResult(user,NEW_USER_REQUEST);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  
		  if(resultCode == RESULT_OK) {
				// RESULT_OK means that everything processed successfully.
				
				if (requestCode == FB_REQUEST) {
					String firstName = data.getStringExtra("USER_NAME");
					if (rdb.userExists(firstName))
					{
						loggedInUser = rdb.selectUserByName(firstName);
					}
					else
					{
						User user = new User();
						user.setFirstName(firstName);
						user.setEmail(null);
						user.setHighestLosingStreak(0);
						user.setHighestWinningStreak(0);
						user.setHighScore(0);
						user.setLastName("");
						user.setNumberOfLosses(0);
						user.setNumberOfTies(0);
						user.setNumberOfWins(0);
						user.setPassword("");
						user.setTotalGames(0);
						user.setUserId(rdb.getNewUserID());
						user.setUserName(firstName + user.getUserId());
						
						loggedInUser = user;
						rdb.insertUser(user);
					}
				}
				else if (requestCode == NEW_USER_REQUEST){
					User user = (User) data.getSerializableExtra("NewUser");
					loggedInUser = user;
					rdb.insertUser(user);
				}
		  }
	}
	
}
