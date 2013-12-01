package com.egyptianratscrew.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;

public class RegisterActivity extends Activity {
	private final static String TAG = "RegisterActivity";

	private EditText txtFirstName;
	private EditText txtLastName;
	private EditText txtUserName;
	private EditText txtEmailAddress;
	private EditText txtPassword;
	private EditText txtConfirmPassword;

	private RatscrewDatabase db;

	private List<User> userList;

	// creating the register content view
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

		fillData();

		db = new RatscrewDatabase(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// TODO Remove this method when the app is finished!!! Only used in testing
	public void fillData() {
		txtFirstName.setText("Edward");
		txtLastName.setText("McNealy");
		txtUserName.setText("edwardmcn");
		txtEmailAddress.setText("edwardmcn@gmail.com");
		txtPassword.setText("password");
		txtConfirmPassword.setText("password");
	}

	/**
	 * This is the method for the onClick event of the Submit button from the Registration page of the app.<br>
	 * This takes all the text from the views and checks if they are empty, then checks if the user name is already in
	 * use, and then checks if the entered passwords match. A new user is then created and added to the database.
	 * 
	 * @param v
	 */
	public void SubmitUser(View v) {
		String firstName = txtFirstName.getText().toString().trim();
		String lastName = txtLastName.getText().toString().trim();
		String userName = txtUserName.getText().toString().trim();
		String email = txtEmailAddress.getText().toString().trim();
		String password = txtPassword.getText().toString().trim();
		String confirmPassword = txtConfirmPassword.getText().toString().trim();

		if (firstName.equals("")) {
			txtFirstName.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter a first name...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (lastName.equals("")) {
			txtLastName.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter a last name...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (userName.equals("")) {
			txtUserName.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter a user name...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (db.userNameExists(userName)) {
			txtUserName.requestFocus();
			Toast toast = Toast.makeText(this, "That user name is already in use...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (email.equals("")) {
			txtEmailAddress.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter an email address...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (password.equals("")) {
			txtPassword.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter a password...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (confirmPassword.equals("")) {
			txtConfirmPassword.requestFocus();
			Toast toast = Toast.makeText(this, "You need to enter a confirmation password...", Toast.LENGTH_SHORT);
			toast.show();
		} else if (!password.equals(confirmPassword)) {
			txtPassword.requestFocus();
			Toast toast = Toast.makeText(this, "Your passwords do not match...", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			User user = new User(firstName, lastName, userName, email, password);
			try {
				db.insertUser(user);
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			}
			Toast toast = Toast.makeText(this, "The user " + user + " has been added!", Toast.LENGTH_SHORT);
			toast.show();
			resetFields();
		}
		// Intent resultIntent = new Intent();
		// resultIntent.putExtra("NewUser", user);
		// setResult(Activity.RESULT_OK, resultIntent);
		// finish();
	}

	public void resetFields() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtUserName.setText("");
		txtEmailAddress.setText("");
		txtPassword.setText("");
		txtConfirmPassword.setText("");
	}

	public void ResetFields(View v) {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtUserName.setText("");
		txtEmailAddress.setText("");
		txtPassword.setText("");
		txtConfirmPassword.setText("");
	}

}
