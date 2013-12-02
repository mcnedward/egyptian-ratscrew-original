package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;
import com.egyptianratscrew.dao.UserArrayWrapper;
import com.egyptianratscrew.dao.UserStub;

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

	private static final int LOGIN_REQUEST = 3;

	private static final int NEW_USER_REQUEST = 2;

	private static final int FB_REQUEST = 1;
	
	private RatscrewDatabase rdb;
	private IUser loggedInUser;
	
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

	//starting activity of fbLogin
	public void fbLogin(View view) {
		Intent fblogin = new Intent(this, fbLoginActivity.class);
		startActivityForResult(fblogin,FB_REQUEST);
	}

	public void LoginWithCredentials(View v){
		Intent loginIntent = new Intent(this,LoginActivity.class);
		startActivityForResult(loginIntent,LOGIN_REQUEST);
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
					if (rdb.userExists(firstName, RatscrewDatabase.FIRST_NAME_FIELD))
					{
						loggedInUser = rdb.selectUserByName(firstName);
						LoginSuccess();
					}
					else
					{
						IUser user = new UserStub();
						user.setFirstName(firstName);
						user.setLastName("");
						user.setUserId(rdb.getNewUserID());
						user.setUserName(firstName + user.getUserId());
						
						loggedInUser = user;
						rdb.insertUser(user);
						LoginSuccess();
					}
				}
				else if (requestCode == NEW_USER_REQUEST){
					IUser user = (IUser) data.getSerializableExtra("NewUser");
					loggedInUser = user;
					rdb.insertUser(user);
					LoginSuccess();
				}
				else if (requestCode == LOGIN_REQUEST){
					IUser user = (IUser) data.getSerializableExtra("ExistingUser");
					loggedInUser = user;
					LoginSuccess();
				}
		  }
	}
	
	private void LoginSuccess(){
		Intent loginIntent = new Intent(this,PlayGameActivity.class);
		loginIntent.putExtra("User", loggedInUser);
		startActivity(loginIntent);
	}
	
	public void StartGame(View v)
	{
		Intent gameIntent = new Intent(this, GameActivity.class);
		UserArrayWrapper wrapper = new UserArrayWrapper(new IUser[] {new UserStub()});
		gameIntent.putExtra("Users", wrapper);
		startActivity(gameIntent);
	}
}
