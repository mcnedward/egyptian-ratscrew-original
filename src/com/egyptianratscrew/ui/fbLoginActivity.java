package com.egyptianratscrew.ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.easy.facebook.android.apicall.GraphApi;
import com.easy.facebook.android.error.EasyFacebookError;
import com.easy.facebook.android.facebook.FBLoginManager;
import com.easy.facebook.android.facebook.Facebook;
import com.easy.facebook.android.facebook.LoginListener;
import com.egyptianratscrew.R;

public class fbLoginActivity extends Activity implements LoginListener {
	 /** Called when the activity is first created. */

	 private FBLoginManager fbLoginManager;

	 public final String EGYPTIANRATSCREWAPP_ID = "585680418165248";

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  //setContentView(R.layout.fbLogin);
	  connectToFacebook();
	 }

	 public void connectToFacebook(){

	  //read about Facebook Permissions here:
	  //http://developers.facebook.com/docs/reference/api/permissions/
	  String permissions[] = {
	    "user_about_me",
	    "user_activities",
	    "user_birthday",
	    "user_checkins",
	    "user_education_history",
	    "user_events",
	    "user_groups",
	    "user_hometown",
	    "user_interests",
	    "user_likes",
	    "user_location",
	    "user_notes",
	    "user_online_presence",
	    "user_photo_video_tags",
	    "user_photos",
	    "user_relationships",
	    "user_relationship_details",
	    "user_religion_politics",
	    "user_status",
	    "user_videos",
	    "user_website",
	    "user_work_history",
	    "email",

	    "read_friendlists",
	    "read_insights",
	    "read_mailbox",
	    "read_requests",
	    "read_stream",
	    "xmpp_login",
	    "ads_management",
	    "create_event",
	    "manage_friendlists",
	    "manage_notifications",
	    "offline_access",
	    "publish_checkins",
	    "publish_stream",
	    "rsvp_event",
	    "sms",
	    //"publish_actions",

	    "manage_pages"

	  };

	  fbLoginManager = new FBLoginManager(this,
	    R.layout.fblogin, 
	    EGYPTIANRATSCREWAPP_ID, 
	    permissions);

	  if(fbLoginManager.existsSavedFacebook()){
	   fbLoginManager.loadFacebook();
	  }
	  else{
	   fbLoginManager.login();
	  }
	 }

	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
	  fbLoginManager.loginSuccess(data);
	 }

	 public void loginSuccess(Facebook facebook) {
	  GraphApi graphApi = new GraphApi(facebook);

	  com.easy.facebook.android.data.User user = new com.easy.facebook.android.data.User();

	  try{
	   user = graphApi.getMyAccountInfo();

	   //update your status if logged in
	   graphApi.setStatus("Hello, world!");
	  } catch(EasyFacebookError e){
	   Log.d("TAG: ", e.toString());
	  }

	  fbLoginManager.displayToast("Hey, " + user.getFirst_name() + "! Login success!");
	 }

	 public void logoutSuccess() {
	  fbLoginManager.displayToast("Logout Success!");
	 }

	 public void loginFail() {
	  fbLoginManager.displayToast("Login Epic Failed!");
	 }
	}