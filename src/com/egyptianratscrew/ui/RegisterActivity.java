package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;

public class RegisterActivity extends Activity{
	
	private TextView txtFirst;
	private TextView txtLast;
	private TextView txtPassword;
	private TextView txtConfirmPassword;
	private TextView txtUserName;
	private TextView txtEmailAddress;
	
	//creatingthe register contentview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		txtFirst = (TextView) findViewById(R.id.txtFirst);
		txtLast = (TextView) findViewById(R.id.txtLast);
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		txtPassword = (TextView) findViewById(R.id.txtPassword);
		txtConfirmPassword = (TextView) findViewById(R.id.txtConfirmPassword);
		txtEmailAddress = (TextView) findViewById(R.id.txtEmailAddress);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void SubmitUser(View v){
		if (txtPassword.getText() == txtConfirmPassword.getText()){
			RatscrewDatabase rdb = new RatscrewDatabase(this);
			
			User user = new User();
			user.setFirstName(txtFirst.getText().toString());
			user.setLastName(txtLast.getText().toString());
			user.setUserName(txtUserName.getText().toString());
			user.setPassword(txtPassword.getText().toString());
			user.setEmail(txtEmailAddress.getText().toString());
			user.setUserId(rdb.getNewUserID());
			
			Intent resultIntent = new Intent();
			resultIntent.putExtra("NewUser", user);
			setResult(Activity.RESULT_OK,resultIntent);
			finish();
		}
		else{
			Toast.makeText(this, "Invalid Password.", Toast.LENGTH_LONG);
		}
	}
	
	public void ResetFields(View v) {
		txtFirst.setText("");
		txtLast.setText("");
		txtUserName.setText("");
		txtPassword.setText("");
		txtConfirmPassword.setText("");
		txtEmailAddress.setText("");
	}

}

	
