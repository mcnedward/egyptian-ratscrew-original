package com.egyptianratscrew.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IUser;
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

	//setting the request and variables
	private static final int LOGIN_REQUEST = 3;

	private static final int NEW_USER_REQUEST = 2;

	private static final int FB_REQUEST = 1;

	private Context context;
	private RatscrewDatabase rdb;
	private IUser loggedInUser;
	public static IUser user = null;

	
	//creating the screens
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;
		rdb = new RatscrewDatabase(this);
		loggedInUser = null;

		user = rdb.getUserByUserName("edwardmcn");	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (user != null) {
			hideButtons(true);
		}
	}

	// starting activity of fbLogin
	public void fbLogin(View view) {
		Intent fblogin = new Intent(this, fbLoginActivity.class);
		startActivityForResult(fblogin, FB_REQUEST);
	}

	//starting activity with credentials
	public void LoginWithCredentials(View v) {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, LOGIN_REQUEST);
	}

	// starting activity of user
	public void startNewUser(View view) {
		Intent user = new Intent(this, RegisterActivity.class);
		startActivityForResult(user, NEW_USER_REQUEST);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			// RESULT_OK means that everything processed successfully.

			
			if (requestCode == FB_REQUEST) {
				String firstName = data.getStringExtra("USER_NAME");
				if (rdb.userExists(firstName, RatscrewDatabase.FIRST_NAME_FIELD)) {
					loggedInUser = rdb.getUserByName(firstName);
					LoginSuccess();
				} else {
					IUser user = new User();
					user.setFirstName(firstName);
					user.setLastName("");
					user.setUserId(rdb.getNewUserID());
					user.setUserName(firstName + user.getUserId());

					loggedInUser = user;
					rdb.insertUser(user);
					LoginSuccess();
				}
			} else if (requestCode == NEW_USER_REQUEST) {
				IUser user = (IUser) data.getSerializableExtra("NewUser");
				loggedInUser = user;
				rdb.insertUser(user);
				LoginSuccess();
			} else if (requestCode == LOGIN_REQUEST) {
				IUser user = (IUser) data.getSerializableExtra("ExistingUser");
				loggedInUser = user;
				LoginSuccess();
			}
		}
	}

	private void LoginSuccess() {
		Intent loginIntent = new Intent(this, PlayGameActivity.class);
		loginIntent.putExtra("User", loggedInUser);
		startActivity(loginIntent);
	}

	public void StartGame(View v) {
		Intent gameIntent = new Intent(this, GameActivity.class);
		// UserArrayWrapper wrapper = new UserArrayWrapper(new IUser[] { new UserStub() });
		// gameIntent.putExtra("Users", wrapper);
		startActivity(gameIntent);
	}

	public void Logout(View v) {
		AlertDialog.Builder confirm = new AlertDialog.Builder(this);
		AlertDialog confirmDialog;
		confirm.setTitle("Are you sure you want to logout?");
		confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { // Delete the artist if user confirms
				user = null;
				hideButtons(false);
				Toast toast = Toast.makeText(context, "You have logged out.", Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
		});
		confirm.setNegativeButton("No", new OnClickListener() { // Do nothing if user cancels
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		confirmDialog = confirm.create(); // Show the confirm dialog
		confirmDialog.show();
	}

	public void viewStatistics(View v) {
		Intent gameIntent = new Intent(this, ViewStatistics.class);
		startActivity(gameIntent);
	}
	
	public void viewRules(View v){
		Intent ruleIntent = new Intent(this, ViewRules.class);
		startActivity(ruleIntent);
	}
	
	public void viewDevelopers(View v){
		Intent developerIntent = new Intent(this, ViewDevelopers.class);
		startActivity(developerIntent);
	}
	
	public void CardBackSelectActivity(View v){
		Intent cardIntent = new Intent(this, CardBackSelectActivity.class);
		startActivity(cardIntent);
	}
	

	

	public void hideButtons(boolean hide) {
		Button btnUserRegister = (Button) findViewById(R.id.btnUserRegister);
		Button btnFBLogin = (Button) findViewById(R.id.btnFBLogin);
		Button btnExisting = (Button) findViewById(R.id.btnExisting);
		Button btnLogout = (Button) findViewById(R.id.btnLogout);
		Button btnStatistics = (Button) findViewById(R.id.btnStatistics);
		Button btnRule = (Button) findViewById(R.id.btnRule);
		Button btnPicture = (Button) findViewById(R.id.btnPicture);
		Button btnDeveloper = (Button) findViewById(R.id.btnDeveloper);
	

		TextView txtUserInfo = (TextView) findViewById(R.id.txtUserInfo);

		if (hide) {
			btnUserRegister.setVisibility(View.GONE);
			btnFBLogin.setVisibility(View.GONE);
			btnExisting.setVisibility(View.GONE);
			btnLogout.setVisibility(View.VISIBLE);
			btnStatistics.setVisibility(View.VISIBLE);
			btnRule.setVisibility(View.VISIBLE);
			btnDeveloper.setVisibility(View.VISIBLE);
			btnPicture.setVisibility(View.VISIBLE);
			txtUserInfo.setVisibility(View.VISIBLE);
			txtUserInfo.setText("Logged in as " + user);
		} else {
			btnUserRegister.setVisibility(View.VISIBLE);
			btnFBLogin.setVisibility(View.VISIBLE);
			btnExisting.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.GONE);
			btnStatistics.setVisibility(View.GONE);
			btnRule.setVisibility(View.GONE);
			btnDeveloper.setVisibility(View.GONE);
			btnPicture.setVisibility(View.GONE);
			txtUserInfo.setVisibility(View.GONE);
			
			
		}
	}
}
