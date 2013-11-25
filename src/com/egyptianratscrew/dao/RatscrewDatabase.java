package com.egyptianratscrew.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class RatscrewDatabase {
	public static final int USER_NAME_FIELD = 2;

	public static final int FIRST_NAME_FIELD = 1;

	private static String TAG = "MusicDatabase";

	private DatabaseAdapter dba;
	private SQLiteDatabase db;

	/** User Variables **/
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
	private static String NUMBER_OF_TIES = "number_of_ties";
	private static String TOTAL_GAMES = "total_games";
	private static String HIGH_SCORE = "high_score";

	public RatscrewDatabase(Context context) {
		dba = new DatabaseAdapter(context);
		db = dba.getWritableDatabase();
	}

	/********** INSERT QUERIES **********/

	@SuppressWarnings("deprecation")
	public void insertUser(User user) {
		if (userExists(user.getUserId())) {
			return;
		} else {
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(db, DATABASE_TABLE_USER);

			// Get the Artist information
			final String userName = user.getUserName();
			final String password = user.getPassword();
			final String email = user.getEmail();
			final String firstName = user.getFirstName();
			final String lastName = user.getLastName();
			final int numberOfWins = user.getNumberOfWins();
			final int numberOfLosses = user.getNumberOfLosses();
			final int highestWinningStreak = user.getHighestWinningStreak();
			final int highestLosingStreak = user.getHighestLosingStreak();
			final int numberOfTies = user.getNumberOfTies();
			final int totalGames = user.getTotalGames();
			final int highScore = user.getHighScore();

			// Get the numeric indexes for each of the columns to update
			final int userNameColumn = ih.getColumnIndex(USERNAME);
			final int passwordColumn = ih.getColumnIndex(PASSWORD);
			final int emailColumn = ih.getColumnIndex(EMAIL);
			final int firstNameColumn = ih.getColumnIndex(FIRST_NAME);
			final int lastNameColumn = ih.getColumnIndex(LAST_NAME);
			final int numberOfWinsColumn = ih.getColumnIndex(NUMBER_OF_WINS);
			final int numberOfLossesColumn = ih.getColumnIndex(NUMBER_OF_LOSSES);
			final int highestWinningStreakColumn = ih.getColumnIndex(HIGHEST_WINNING_STREAK);
			final int highestLosingStreakColumn = ih.getColumnIndex(HIGHEST_LOSING_STREAK);
			final int numberOfTiesColumn = ih.getColumnIndex(NUMBER_OF_TIES);
			final int totalGamesColumn = ih.getColumnIndex(TOTAL_GAMES);
			final int highScoreColumn = ih.getColumnIndex(HIGH_SCORE);

			@SuppressWarnings("unused")
			int x = 0;

			try {
				// Get the InsertHelper ready to insert a single row
				ih.prepareForInsert();

				// Add the data for each column
				ih.bind(userNameColumn, userName);
				ih.bind(passwordColumn, password);
				ih.bind(emailColumn, email);
				ih.bind(firstNameColumn, firstName);
				ih.bind(lastNameColumn, lastName);
				ih.bind(numberOfWinsColumn, numberOfWins);
				ih.bind(numberOfLossesColumn, numberOfLosses);
				ih.bind(highestWinningStreakColumn, highestWinningStreak);
				ih.bind(highestLosingStreakColumn, highestLosingStreak);
				ih.bind(numberOfTiesColumn, numberOfTies);
				ih.bind(totalGamesColumn, totalGames);
				ih.bind(highScoreColumn, highScore);

				// Insert the row into the database.
				ih.execute();
				x++;
			} finally {
				ih.close();
			}
		}
	}

	/********** SELECT QUERIES **********/

	public User selectUserById(int userId) {
		User user = null;
		Cursor c = db.rawQuery("SELECT * FROM user WHERE userId = ?", new String[] { String.valueOf(userId) });
		try {
			while (c.moveToNext()) {
				String userName = c.getString(c.getColumnIndexOrThrow(USERNAME));
				String password = c.getString(c.getColumnIndexOrThrow(PASSWORD));
				String email = c.getString(c.getColumnIndexOrThrow(EMAIL));
				String firstName = c.getString(c.getColumnIndexOrThrow(FIRST_NAME));
				String lastName = c.getString(c.getColumnIndexOrThrow(LAST_NAME));
				int numberOfWins = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_WINS));
				int numberOfLosses = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_LOSSES));
				int highestWinningStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_WINNING_STREAK));
				int highestLosingStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_LOSING_STREAK));
				int numberOfTies = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_TIES));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));
				int highScore = c.getInt(c.getColumnIndexOrThrow(HIGH_SCORE));

				user = new User(userId, userName, password, email, firstName, lastName, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, numberOfTies, totalGames, highScore);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
	}
	
	public User selectUserByName(String userFirstName) {
		User user = null;
		Cursor c = db.rawQuery("SELECT * FROM user WHERE FIRST_NAME = ?", new String[] { userFirstName });
		try {
			while (c.moveToNext()) {
				int userId = c.getInt(c.getColumnIndexOrThrow(USER_ID));
				String userName = c.getString(c.getColumnIndexOrThrow(USERNAME));
				String password = c.getString(c.getColumnIndexOrThrow(PASSWORD));
				String email = c.getString(c.getColumnIndexOrThrow(EMAIL));
				String firstName = c.getString(c.getColumnIndexOrThrow(FIRST_NAME));
				String lastName = c.getString(c.getColumnIndexOrThrow(LAST_NAME));
				int numberOfWins = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_WINS));
				int numberOfLosses = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_LOSSES));
				int highestWinningStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_WINNING_STREAK));
				int highestLosingStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_LOSING_STREAK));
				int numberOfTies = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_TIES));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));
				int highScore = c.getInt(c.getColumnIndexOrThrow(HIGH_SCORE));

				user = new User(userId, userName, password, email, firstName, lastName, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, numberOfTies, totalGames, highScore);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
	}
	
	public User selectUserByUserName(String aUserName) {
		User user = null;
		Cursor c = db.rawQuery("SELECT * FROM user WHERE USER_NAME = ?", new String[] { aUserName });
		try {
			while (c.moveToNext()) {
				int userId = c.getInt(c.getColumnIndexOrThrow(USER_ID));
				String userName = c.getString(c.getColumnIndexOrThrow(USERNAME));
				String password = c.getString(c.getColumnIndexOrThrow(PASSWORD));
				String email = c.getString(c.getColumnIndexOrThrow(EMAIL));
				String firstName = c.getString(c.getColumnIndexOrThrow(FIRST_NAME));
				String lastName = c.getString(c.getColumnIndexOrThrow(LAST_NAME));
				int numberOfWins = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_WINS));
				int numberOfLosses = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_LOSSES));
				int highestWinningStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_WINNING_STREAK));
				int highestLosingStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_LOSING_STREAK));
				int numberOfTies = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_TIES));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));
				int highScore = c.getInt(c.getColumnIndexOrThrow(HIGH_SCORE));

				user = new User(userId, userName, password, email, firstName, lastName, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, numberOfTies, totalGames, highScore);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
	}

	/********** OBJECT EXISTS QUERIES **********/

	public boolean userExists(int userId) {
		Cursor c = db.rawQuery("SELECT * FROM user WHERE USER_ID = ?", new String[] { String.valueOf(userId) });
		if (c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	
	public boolean userExists(String userName, int field) {
		Cursor c = null;
		if (field == FIRST_NAME_FIELD){
			c = db.rawQuery("SELECT * FROM user WHERE FIRST_NAME = ?", new String[] { userName });
		}
		else if (field == USER_NAME_FIELD){
			c = db.rawQuery("SELECT * FROM user WHERE USER_NAME = ?", new String[] { userName });
		}
		else {
			Log.d("TAG: ", "Field Invalid.");
			return false;
		}
		
		if (c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	
	public int getNewUserID()
	{
		int retInt = 1;
		Cursor c = db.rawQuery("SELECT * FROM user", null);
		try {
			List<Integer> userIDs = new ArrayList<Integer>();
			
			while (c.moveToNext()) {
				userIDs.add(c.getInt(c.getColumnIndexOrThrow(USER_ID)));
			}
			
			while (true)
			{
				if (userIDs.contains(retInt))
				{
					retInt++;
				}
				else
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			Log.d("TAG: ", e.toString());
		}
		
		return retInt;
	}

}
