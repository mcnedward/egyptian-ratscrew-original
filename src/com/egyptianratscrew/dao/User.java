package com.egyptianratscrew.dao;

import java.io.Serializable;

public class User implements Serializable, IUser  {

	private int userId;
	private String userName;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private int numberOfWins;
	private int numberOfLosses;
	private int highestWinningStreak;
	private int highestLosingStreak;
	private int currentWinningStreak;
	private int currentLosingStreak;
	private int numberOfTies;
	private int totalGames;
	private int highScore;

	public User(int userId, String userName, String password, String email, String firstName, String lastName,
			int numberOfWins, int numberOfLosses, int highestWinningStreak, int highestLosingStreak,
			int currentWinningStreak, int currentLosingStreak, int numberOfTies,
			int totalGames, int highScore) {

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
		this.numberOfTies = numberOfTies;
		this.totalGames = totalGames;
		this.highScore = highScore;
	}

	public User() {
		// Empty Constructor
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}

	public int getNumberOfLosses() {
		return numberOfLosses;
	}

	public void setNumberOfLosses(int numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}

	public int getHighestWinningStreak() {
		return highestWinningStreak;
	}

	public void setHighestWinningStreak(int highestWinningStreak) {
		this.highestWinningStreak = highestWinningStreak;
	}

	public int getHighestLosingStreak() {
		return highestLosingStreak;
	}

	public void setHighestLosingStreak(int highestLosingStreak) {
		this.highestLosingStreak = highestLosingStreak;
	}

	public int getNumberOfTies() {
		return numberOfTies;
	}

	public void setNumberOfTies(int numberOfTies) {
		this.numberOfTies = numberOfTies;
	}

	public int getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(int totalGames) {
		this.totalGames = totalGames;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
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

}
