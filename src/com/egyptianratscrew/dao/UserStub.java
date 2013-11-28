package com.egyptianratscrew.dao;

import java.util.Date;

public class UserStub implements IUser{

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
	private int numberOfTies;
	private int totalGames;
	private int highScore;
	
	
	public UserStub(){
		Date d = new Date();
		int id = Integer.parseInt(String.format("%d%d%d%d%d%d", d.getMonth(), d.getDay(), d.getYear(),d.getHours(),d.getMinutes(),d.getSeconds()));
		this.setUserId(id);
		this.setUserName("UserStub_"+ Integer.toString(id));
		this.setFirstName("User");
		this.setLastName("Stub");
		this.setPassword("");
		this.setEmail("");
		this.setNumberOfLosses(0);
		this.setNumberOfTies(0);
		this.setNumberOfWins(0);
		this.setHighestLosingStreak(0);
		this.setHighestWinningStreak(0);
		this.setHighScore(0);
		this.setTotalGames(0);
	}

	@Override
	public int getUserId() {
		// TODO Auto-generated method stub
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		// TODO Auto-generated method stub
		this.userId = userId;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		// TODO Auto-generated method stub
		this.userName = userName; 
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password = password;
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		this.email = email;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		// TODO Auto-generated method stub
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		// TODO Auto-generated method stub
		this.lastName = lastName;
	}

	@Override
	public int getNumberOfWins() {
		// TODO Auto-generated method stub
		return this.numberOfWins;
	}

	@Override
	public void setNumberOfWins(int numberOfWins) {
		// TODO Auto-generated method stub
		this.numberOfWins = numberOfWins;
	}

	@Override
	public int getNumberOfLosses() {
		// TODO Auto-generated method stub
		return this.numberOfLosses;
	}

	@Override
	public void setNumberOfLosses(int numberOfLosses) {
		// TODO Auto-generated method stub
		this.numberOfLosses = numberOfLosses;
	}

	@Override
	public int getHighestWinningStreak() {
		// TODO Auto-generated method stub
		return this.highestWinningStreak;
	}

	@Override
	public void setHighestWinningStreak(int highestWinningStreak) {
		// TODO Auto-generated method stub
		this.highestWinningStreak = highestWinningStreak;
	}

	@Override
	public int getHighestLosingStreak() {
		// TODO Auto-generated method stub
		return this.highestLosingStreak;
	}

	@Override
	public void setHighestLosingStreak(int highestLosingStreak) {
		// TODO Auto-generated method stub
		this.highestLosingStreak = highestLosingStreak;
	}

	@Override
	public int getNumberOfTies() {
		// TODO Auto-generated method stub
		return this.numberOfTies;
	}

	@Override
	public void setNumberOfTies(int numberOfTies) {
		// TODO Auto-generated method stub
		this.numberOfTies = numberOfTies;
	}

	@Override
	public int getTotalGames() {
		// TODO Auto-generated method stub
		return this.totalGames;
	}

	@Override
	public void setTotalGames(int totalGames) {
		// TODO Auto-generated method stub
		this.totalGames = totalGames;
	}

	@Override
	public int getHighScore() {
		// TODO Auto-generated method stub
		return this.highScore;
	}

	@Override
	public void setHighScore(int highScore) {
		// TODO Auto-generated method stub
		this.highScore = highScore;
	}
}
