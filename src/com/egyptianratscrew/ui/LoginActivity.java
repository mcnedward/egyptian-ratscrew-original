package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.RatscrewDatabase;

public class LoginActivity extends Activity {

	// setting variables
	private EditText edtUserName;
	private EditText edtPassword;
	private RatscrewDatabase rdb;

	// creating the screen of activity_login
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtUserName.requestFocus();
		rdb = new RatscrewDatabase(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * the login button is click
	 * 
	 * @param v
	 */
	public void LoginClicked(View v) {
		// declaring the username and password
		String userName = edtUserName.getText().toString().trim();
		String password = edtPassword.getText().toString().trim();

		IUser user = rdb.getUserByUserName(userName);

		// if the username is blank to display a message
		if (userName.equals("")) {
			Toast toast = Toast.makeText(this, "You need to enter a user name...", Toast.LENGTH_SHORT);
			toast.show();
			// if the password is blank to display a message
		} else if (password.equals("")) {
			Toast toast = Toast.makeText(this, "You need to enter a password...", Toast.LENGTH_SHORT);
			toast.show();
			// if the username is incorrect to display a message
		} else if (user == null) {
			Toast toast = Toast.makeText(this, "That user does not exist or the passwords do not match...",
					Toast.LENGTH_SHORT);
			toast.show();
			// if the password is incorrect to display a message
		} else if (!user.getPassword().equals(password)) {
			Toast toast = Toast.makeText(this, "That user does not exist or the passwords do not match...",
					Toast.LENGTH_SHORT);
			toast.show();
			// the username and password exist
		} else {
			Toast toast = Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT);
			toast.show();
			Intent resultIntent = new Intent();
			resultIntent.putExtra("ExistingUser", user);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}

	}

	/**
	 * if reset clicked then clear everything to start over
	 * 
	 * @param v
	 */
	public void ClearClicked(View v) {
		edtUserName.setText("");
		edtPassword.setText("");
	}

}
