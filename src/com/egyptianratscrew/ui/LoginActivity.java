package com.egyptianratscrew.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;

public class LoginActivity extends Activity {

	private EditText edtUserName;
	private EditText edtPassword;
	private RatscrewDatabase rdb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		edtUserName = (EditText)findViewById(R.id.edtUserName);
		edtPassword = (EditText)findViewById(R.id.edtPassword);
		rdb = new RatscrewDatabase(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void LoginClicked(View v){
		if (rdb.userExists(edtUserName.getText().toString(), RatscrewDatabase.USER_NAME_FIELD)){
			User user = (User)rdb.selectUserByUserName(edtUserName.getText().toString());
			if (user.getPassword() == edtPassword.getText().toString()){
				Intent resultIntent = new Intent();
				resultIntent.putExtra("ExistingUser", user);
				setResult(Activity.RESULT_OK,resultIntent);
				finish();
			}
			else{
				Toast.makeText(this, "Invalid Password.", Toast.LENGTH_LONG);
			}
		}
		else {
			Toast.makeText(this, "Invalid Username.", Toast.LENGTH_LONG);
		}
	}

}
