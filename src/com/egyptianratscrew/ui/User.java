package com.egyptianratscrew.ui;




import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.egyptianratscrew.R;

public class User extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		User user = new User();
		userInformation(user);
		
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflating the menu
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Login for the user
	 * 
	 * @param 
	 * Julie
	
	 */
	public void userInformation(User user) {
		
		
	}
	}
	


