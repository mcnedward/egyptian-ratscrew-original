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
	private static final int DB_VERSION = 3;

	/** User Table **/
	private static String DATABASE_TABLE_USER = "user";
	private static String USER_ID = "_id";
	private static String FIRST_NAME = "FirstName";
	private static String LAST_NAME = "LastName";
	private static String USERNAME = "UserName";
	private static String EMAIL = "Email";
	private static String PASSWORD = "Password";
	private static String NUMBER_OF_WINS = "NumberOfWins";
	private static String NUMBER_OF_LOSSES = "NumberOfLosses";
	private static String HIGHEST_WINNING_STREAK = "HighestWinningStreak";
	private static String HIGHEST_LOSING_STREAK = "HighestLosingStreak";
	private static String CURRENT_WINNING_STREAK = "CurrentWinningStreak";
	private static String CURRENT_LOSING_STREAK = "CurrentLosingStreak";
	private static String TOTAL_GAMES = "TotalGames";

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
						NUMBER_OF_LOSSES, HIGHEST_WINNING_STREAK, HIGHEST_LOSING_STREAK, CURRENT_WINNING_STREAK,
						CURRENT_LOSING_STREAK, TOTAL_GAMES);
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
