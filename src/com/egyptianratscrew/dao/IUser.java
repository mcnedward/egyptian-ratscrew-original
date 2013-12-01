package com.egyptianratscrew.dao;

import java.io.Serializable;

public interface IUser extends Serializable{
	public int getUserId();

	public void setUserId(int userId);

	public String getUserName();

	public void setUserName(String userName);

	public String getPassword();

	public void setPassword(String password);

	public String getEmail();

	public void setEmail(String email);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public int getNumberOfWins();

	public void setNumberOfWins(int numberOfWins);

	public int getNumberOfLosses();

	public void setNumberOfLosses(int numberOfLosses);

	public int getHighestWinningStreak();

	public void setHighestWinningStreak(int highestWinningStreak);

	public int getHighestLosingStreak();

	public void setHighestLosingStreak(int highestLosingStreak);

	public int getNumberOfTies();

	public void setNumberOfTies(int numberOfTies);

	public int getTotalGames();

	public void setTotalGames(int totalGames);

	public int getHighScore();

	public void setHighScore(int highScore);
	
	public int getCurrentWinningStreak();
	
	public void setCurrentWinningStreak(int currentWinningStreak);
	
	public int getCurrentLosingStreak();
	
	public void setCurrentLosingStreak(int currentLosingStreak);

}
