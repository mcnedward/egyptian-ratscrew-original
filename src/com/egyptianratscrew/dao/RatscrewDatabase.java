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
	private static String TAG = "RatscrewDatabase";

	private DatabaseAdapter dba;
	private SQLiteDatabase db;

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

	public RatscrewDatabase() {

	}

	public RatscrewDatabase(Context context) {
		dba = new DatabaseAdapter(context);
	}

	/**
	 * Open the database for the Ratscrew Database for reading only.
	 * 
	 * @return A read-only database.
	 * @throws android.database.SQLException
	 */
	public RatscrewDatabase openToRead() throws android.database.SQLException {
		db = dba.getReadableDatabase();
		return this;
	}

	/**
	 * Opens the database for the Ratscrew Database for writing.
	 * 
	 * @return A writable database.
	 * @throws android.database.SQLException
	 */
	public RatscrewDatabase open() throws android.database.SQLException {
		db = dba.getWritableDatabase();
		return this;
	}

	/**
	 * Close the open database object.
	 */
	public void close() {
		dba.close();
	}

	/********** INSERT QUERIES **********/

	/**
	 * This is used to add a new user to the database.
	 * 
	 * @param user
	 *            The user to add into the database.
	 */
	@SuppressWarnings("deprecation")
	public void insertUser(IUser user) {
		if (userExists(user.getUserId())) {
			return;
		} else {
			open();
			// Create a single InsertHelper to handle this set of insertions.
			InsertHelper ih = new InsertHelper(db, DATABASE_TABLE_USER);

			// Get the User information
			final String firstName = user.getFirstName();
			final String lastName = user.getLastName();
			final String userName = user.getUserName();
			final String email = user.getEmail();
			final String password = user.getPassword();
			final int numberOfWins = user.getNumberOfWins();
			final int numberOfLosses = user.getNumberOfLosses();
			final int highestWinningStreak = user.getHighestWinningStreak();
			final int highestLosingStreak = user.getHighestLosingStreak();
			final int currentWinningStreak = user.getCurrentWinningStreak();
			final int currentLosingStreak = user.getCurrentLosingStreak();
			final int totalGames = user.getTotalGames();

			// Get the numeric indexes for each of the columns to update
			final int firstNameColumn = ih.getColumnIndex(FIRST_NAME);
			final int lastNameColumn = ih.getColumnIndex(LAST_NAME);
			final int userNameColumn = ih.getColumnIndex(USERNAME);
			final int emailColumn = ih.getColumnIndex(EMAIL);
			final int passwordColumn = ih.getColumnIndex(PASSWORD);
			final int numberOfWinsColumn = ih.getColumnIndex(NUMBER_OF_WINS);
			final int numberOfLossesColumn = ih.getColumnIndex(NUMBER_OF_LOSSES);
			final int highestWinningStreakColumn = ih.getColumnIndex(HIGHEST_WINNING_STREAK);
			final int highestLosingStreakColumn = ih.getColumnIndex(HIGHEST_LOSING_STREAK);
			final int currentWinningStreakColumn = ih.getColumnIndex(CURRENT_WINNING_STREAK);
			final int currentLosingStreakColumn = ih.getColumnIndex(CURRENT_LOSING_STREAK);
			final int totalGamesColumn = ih.getColumnIndex(TOTAL_GAMES);

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
				ih.bind(currentWinningStreakColumn, currentWinningStreak);
				ih.bind(currentLosingStreakColumn, currentLosingStreak);
				ih.bind(totalGamesColumn, totalGames);

				// Insert the row into the database.
				ih.execute();
				x++;
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			} finally {
				ih.close();
				close();
			}
		}
	}

	/********** SELECT QUERIES **********/

	/**
	 * This is used to select a user from the database, based on a user id.
	 * 
	 * @param userId
	 *            The user id of the user you want to return from the database.
	 * @return The user from the database.
	 */
	public IUser getUserById(int userId) {
		User user = null;
		open();
		Cursor c = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USER + " WHERE " + USER_ID + " = ?",
				new String[] { String.valueOf(userId) });
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
				int currentWinningStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_WINNING_STREAK));
				int currentLosingStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_LOSING_STREAK));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));

				user = new User(userId, firstName, lastName, userName, email, password, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, currentWinningStreak, currentLosingStreak,
						totalGames);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				close();
			}
		}
	}

	public IUser getUserByName(String userFirstName) {
		IUser user = null;
		open();
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
				int currentWinningStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_WINNING_STREAK));
				int currentLosingStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_LOSING_STREAK));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));

				user = new User(userId, firstName, lastName, userName, email, password, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, currentWinningStreak, currentLosingStreak,
						totalGames);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				close();
			}
		}
	}

	/**
	 * This is used to select a certain user from the database based on a user name.
	 * 
	 * @param userName
	 *            The user name of the user you want to get from the database.
	 * @return The user from the database whose user name matches the user name parameter you entered.
	 */
	public IUser getUserByUserName(String userName) {
		IUser user = null;
		open();
		Cursor c = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USER + " WHERE " + USERNAME + " = ?",
				new String[] { userName });
		try {
			while (c.moveToNext()) {
				int userId = c.getInt(c.getColumnIndexOrThrow(USER_ID));
				String firstName = c.getString(c.getColumnIndexOrThrow(FIRST_NAME));
				String lastName = c.getString(c.getColumnIndexOrThrow(LAST_NAME));
				String email = c.getString(c.getColumnIndexOrThrow(EMAIL));
				String password = c.getString(c.getColumnIndexOrThrow(PASSWORD));
				int numberOfWins = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_WINS));
				int numberOfLosses = c.getInt(c.getColumnIndexOrThrow(NUMBER_OF_LOSSES));
				int highestWinningStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_WINNING_STREAK));
				int highestLosingStreak = c.getInt(c.getColumnIndexOrThrow(HIGHEST_LOSING_STREAK));
				int currentWinningStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_WINNING_STREAK));
				int currentLosingStreak = c.getInt(c.getColumnIndexOrThrow(CURRENT_LOSING_STREAK));
				int totalGames = c.getInt(c.getColumnIndexOrThrow(TOTAL_GAMES));

				user = new User(userId, firstName, lastName, userName, email, password, numberOfWins, numberOfLosses,
						highestWinningStreak, highestLosingStreak, currentWinningStreak, currentLosingStreak,
						totalGames);
			}
			if (user == null) {
				Log.i(TAG, "No user exists");
				return null;
			} else
				return user;
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				close();
			}
		}
		return user;
	}

	/********** UPDATE QUERIES **********/

	public void userWins(IUser user) {
		IUser u = getUserById(user.getUserId());
		if (u != null) {
			open();
			try {
				// Update the win stats
				db.execSQL("UPDATE " + DATABASE_TABLE_USER + " SET " + NUMBER_OF_WINS + " = " + NUMBER_OF_WINS
						+ " + 1, " + CURRENT_WINNING_STREAK + " = " + CURRENT_WINNING_STREAK + " + 1, " + TOTAL_GAMES
						+ " = " + TOTAL_GAMES + " + 1 WHERE " + USER_ID + " = ?",
						new String[] { String.valueOf(user.getUserId()) });
				// Reset the current losing streak
				db.execSQL("UPDATE " + DATABASE_TABLE_USER + " SET " + CURRENT_LOSING_STREAK + " = 0 WHERE " + USER_ID
						+ " = ?", new String[] { String.valueOf(user.getUserId()) });
				// Update the highest winning streak if it is less than or equal to the current winning streak
				if (user.getCurrentWinningStreak() >= user.getHighestWinningStreak()) {
					db.execSQL("UPDATE " + DATABASE_TABLE_USER + " SET " + HIGHEST_WINNING_STREAK + " = "
							+ CURRENT_WINNING_STREAK + " WHERE " + USER_ID + " = ?",
							new String[] { String.valueOf(user.getUserId()) });
				}
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			} finally {
				close();
			}
		}
	}

	public void userLoses(IUser user) {
		IUser u = getUserById(user.getUserId());
		if (u != null) {
			open();
			try {
				db.execSQL("UPDATE " + DATABASE_TABLE_USER + " SET " + NUMBER_OF_LOSSES + " = " + NUMBER_OF_LOSSES
						+ " + 1, " + CURRENT_LOSING_STREAK + " = " + CURRENT_LOSING_STREAK + " + 1, " + TOTAL_GAMES
						+ " = " + TOTAL_GAMES + " + 1 WHERE " + USER_ID + " = ?",
						new String[] { String.valueOf(user.getUserId()) });
				// Reset the current winning streak
				db.execSQL("UPDATE " + DATABASE_TABLE_USER + " SET " + CURRENT_WINNING_STREAK + " = 0 WHERE " + USER_ID
						+ " = ?", new String[] { String.valueOf(user.getUserId()) });
				// Update highest losing streak if it is less than or equal to the current losing streak
				if (user.getCurrentLosingStreak() >= user.getHighestLosingStreak()) {
					db.execSQL(
							"UPDATE " + DATABASE_TABLE_USER + " SET " + HIGHEST_LOSING_STREAK + " = "
									+ user.getCurrentLosingStreak() + " WHERE " + USER_ID + " = ?",
							new String[] { String.valueOf(user.getUserId()) });
				}
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			} finally {
				close();
			}
		}
	}

	public boolean updateUser(IUser user) {
		db.delete("user", "_id = " + user.getUserId(), null);
		insertUser(user);

		return true;
	}

	/********** OBJECT EXISTS QUERIES **********/

	/**
	 * Use this to check whether a user is already in the database.
	 * 
	 * @param userId
	 *            The user id of the user to check against.
	 * @return True if the user is already in the database, false if that user is not in the database.
	 */
	public boolean userExists(int userId) {
		open();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USER + " WHERE " + USER_ID + " = ?",
					new String[] { String.valueOf(userId) });
			c.moveToFirst();
			if (c.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return false;
	}

	/**
	 * Use this to check whether a user name is already in use by some one else.
	 * 
	 * @param userName
	 *            The user name that you want to check against.
	 * @return True if the user name already exists, false if the user name is not in use.
	 */
	public boolean userNameExists(String userName) {
		open();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USER + " WHERE " + USERNAME + " = ?",
					new String[] { String.valueOf(userName) });
			c.moveToFirst();
			if (c.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			close();
		}
		return false;
	}

	public boolean userExists(String userName, int field) {
		Cursor c = null;
		if (field == FIRST_NAME_FIELD) {
			c = db.rawQuery("SELECT * FROM user WHERE FIRST_NAME = ?", new String[] { userName });
		} else if (field == USER_NAME_FIELD) {
			c = db.rawQuery("SELECT * FROM user WHERE USER_NAME = ?", new String[] { userName });
		} else {
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

	public int getNewUserID() {
		int retInt = 1;
		Cursor c = db.rawQuery("SELECT * FROM user", null);
		try {
			List<Integer> userIDs = new ArrayList<Integer>();

			while (c.moveToNext()) {
				userIDs.add(c.getInt(c.getColumnIndexOrThrow(USER_ID)));
			}

			while (true) {
				if (userIDs.contains(retInt)) {
					retInt++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			Log.d("TAG: ", e.toString());
		}

		return retInt;
	}

}
