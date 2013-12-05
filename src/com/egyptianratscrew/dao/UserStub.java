package com.egyptianratscrew.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserStub implements IUser {
//declaring variables
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
	private int totalGames;

	public UserStub() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
		int date = cal.getTime().getDate();

		int id = date;
		this.setUserId(id);
		this.setUserName("UserStub_" + Integer.toString(id));
		this.setFirstName("User");
		this.setLastName("Stub");
		this.setPassword("");
		this.setEmail("");
		this.setNumberOfLosses(0);
		this.setNumberOfWins(0);
		this.setHighestLosingStreak(0);
		this.setHighestWinningStreak(0);
		this.setCurrentLosingStreak(0);
		this.setCurrentWinningStreak(0);
		this.setTotalGames(0);
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
		return this.password;
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
		return this.numberOfWins;
	}

	@Override
	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}

	@Override
	public int getNumberOfLosses() {
		return this.numberOfLosses;
	}

	@Override
	public void setNumberOfLosses(int numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}

	@Override
	public int getHighestWinningStreak() {
		return this.highestWinningStreak;
	}

	@Override
	public void setHighestWinningStreak(int highestWinningStreak) {
		this.highestWinningStreak = highestWinningStreak;
	}

	@Override
	public int getHighestLosingStreak() {
		return this.highestLosingStreak;
	}

	@Override
	public void setHighestLosingStreak(int highestLosingStreak) {
		this.highestLosingStreak = highestLosingStreak;
	}

	@Override
	public int getCurrentWinningStreak() {
		return this.currentWinningStreak;
	}

	@Override
	public void setCurrentWinningStreak(int currentWinningStreak) {
		this.currentWinningStreak = currentWinningStreak;
	}

	@Override
	public int getCurrentLosingStreak() {
		return this.currentLosingStreak;
	}

	@Override
	public void setCurrentLosingStreak(int currentLosingStreak) {
		this.currentLosingStreak = currentLosingStreak;
	}

	@Override
	public int getTotalGames() {
		return this.totalGames;
	}

	@Override
	public void setTotalGames(int totalGames) {
		this.totalGames = totalGames;
	}
}
