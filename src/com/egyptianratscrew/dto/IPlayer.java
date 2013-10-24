package com.egyptianratscrew.dto;

import java.util.List;

public interface IPlayer {
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
}
