package com.egyptianratscrew.dao;

import java.io.Serializable;

public class User implements Serializable, IUser {

	private int userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private String password;
	private int numberOfWins = 0;
	private int numberOfLosses = 0;
	private int highestWinningStreak = 0;
	private int highestLosingStreak = 0;
	private int currentWinningStreak = 0;
	private int currentLosingStreak = 0;
	private int totalGames = 0;

	/**
	 * This constructor is used when selecting a user from the database.<br>
	 * It is needed to quickly set all the parameters in one place.
	 * 
	 * @param firstName
	 *            The first name of the user.
	 * @param lastName
	 *            The last name of the user.
	 * @param userName
	 *            The user name.
	 * @param email
	 *            The email address of the user.
	 * @param password
	 *            The password of the user.
	 * @param numberOfWins
	 *            The number of times this user has won.
	 * @param numberOfLosses
	 *            The number of times this user has lost.
	 * @param highestWinningStreak
	 *            The highest winning streak for this user.
	 * @param highestLosingStreak
	 *            The highest losing streak for this user.
	 * @param numberOfTies
	 *            The amount of times this user has tied.
	 * @param totalGames
	 *            The total number of games played by this user.
	 * @param highScore
	 *            The highest score this user has earned.
	 */
	public User(int userId, String firstName, String lastName, String userName, String email, String password,
			int numberOfWins, int numberOfLosses, int highestWinningStreak, int highestLosingStreak, int currentWinningStreak, int currentLosingStreak,int totalGames) {

		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.numberOfWins = numberOfWins;
		this.numberOfLosses = numberOfLosses;
		this.highestWinningStreak = highestWinningStreak;
		this.highestLosingStreak = highestLosingStreak;
		this.currentWinningStreak = currentWinningStreak;
		this.currentLosingStreak = currentLosingStreak;
		this.totalGames = totalGames;
	}

	/**
	 * This constructor is used when creating a new user from Registration.
	 * 
	 * @param firstName
	 *            The first name of the user.
	 * @param lastName
	 *            The last name of the user.
	 * @param userName
	 *            The user name.
	 * @param email
	 *            The email address of the user.
	 * @param password
	 *            The password of the user.
	 */
	public User(String firstName, String lastName, String userName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public User() {
		// Empty Constructor
	}

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public int getNumberOfWins() {
		return numberOfWins;
	}

	@Override
	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}

	@Override
	public int getNumberOfLosses() {
		return numberOfLosses;
	}

	@Override
	public void setNumberOfLosses(int numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}

	@Override
	public int getHighestWinningStreak() {
		return highestWinningStreak;
	}

	@Override
	public void setHighestWinningStreak(int highestWinningStreak) {
		this.highestWinningStreak = highestWinningStreak;
	}

	@Override
	public int getHighestLosingStreak() {
		return highestLosingStreak;
	}

	@Override
	public void setHighestLosingStreak(int highestLosingStreak) {
		this.highestLosingStreak = highestLosingStreak;
	}

	@Override
	public int getCurrentWinningStreak() {
		// TODO Auto-generated method stub
		return this.currentWinningStreak;
	}

	@Override
	public void setCurrentWinningStreak(int currentWinningStreak) {
		// TODO Auto-generated method stub
		this.currentWinningStreak = currentWinningStreak;
	}

	@Override
	public int getCurrentLosingStreak() {
		// TODO Auto-generated method stub
		return this.currentLosingStreak;
	}

	@Override
	public void setCurrentLosingStreak(int currentLosingStreak) {
		// TODO Auto-generated method stub
		this.currentLosingStreak = currentLosingStreak;
	}

	@Override
	public int getTotalGames() {
		return totalGames;
	}

	@Override
	public void setTotalGames(int totalGames) {
		this.totalGames = totalGames;
	}

	@Override
	public String toString() {
		return userName;

	}

}
