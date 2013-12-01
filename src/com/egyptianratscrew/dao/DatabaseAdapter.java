package com.egyptianratscrew.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class that is used to create and upgrade the Egyptian Ratscrew Database.
 * All tables for the database need to be defined and created here.
 * 
 * @author Edward
 * 
 */
public class DatabaseAdapter extends SQLiteOpenHelper {
	private static String TAG = "DatabaseAdapter";

	/** Database Name **/
	private static String DB_NAME = "ratscrew.db";
	/** Database Version **/
	private static final int DB_VERSION = 1;

	/** User Table **/
	private static String DATABASE_TABLE_USER = "user";
	private static String USER_ID = "_id";
	private static String USERNAME = "userName";
	private static String PASSWORD = "password";
	private static String EMAIL = "email";
	private static String FIRST_NAME = "first_name";
	private static String LAST_NAME = "last_name";
	private static String NUMBER_OF_WINS = "number_of_wins";
	private static String NUMBER_OF_LOSSES = "number_of_losses";
	private static String HIGHEST_WINNING_STREAK = "highest_winning_streak";
	private static String HIGHEST_LOSING_STREAK = "highest_losing_streak";
	private static String CURRENT_WINNING_STREAK = "current_winning_streak";
	private static String CURRENT_LOSING_STREAK = "current_losing_streak";
	private static String NUMBER_OF_TIES = "number_of_ties";
	private static String TOTAL_GAMES = "total_games";
	private static String HIGH_SCORE = "high_score";

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 *            - The context of the activity that is creating the database
	 */
	public DatabaseAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createUserTable = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
						DATABASE_TABLE_USER, USER_ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, NUMBER_OF_WINS,
						NUMBER_OF_LOSSES, HIGHEST_WINNING_STREAK, HIGHEST_LOSING_STREAK, CURRENT_WINNING_STREAK, CURRENT_LOSING_STREAK, NUMBER_OF_TIES, TOTAL_GAMES,
						HIGH_SCORE);
		Log.i(TAG, String.format("%s", createUserTable));
		db.execSQL(createUserTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Droping database tables");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USER);

		onCreate(db);
	}

}
