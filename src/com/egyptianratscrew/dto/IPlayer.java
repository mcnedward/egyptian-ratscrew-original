package com.egyptianratscrew.dto;

import java.util.List;

import com.egyptianratscrew.dao.IUser;

public interface IPlayer {
	//declaring variables 
	public String getName();

	public void addCard(Card c);

	public Card playCard();

	public boolean needsToPlayFace();

	public int getTillFace();

	public void setTillFace(int i);

	public boolean myTurn();

	public void setMyTurn(boolean b);

	public int getID();

	public boolean hasAllCards();

	public List<Card> getHand();

	/**
	 * Use this to get the top card from the player's hand.
	 * 
	 * @return The top card of the player's hand.
	 */
	public Card getTopCard();

	public void setCardCoor(float x, float y);
	
	public IUser getUser();
}
