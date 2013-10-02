package com.egyptianratscrew.dto;

public interface IPlayer {
	public String getName();
	public void addCard(Card2 c);
	public Card2 playCard();
	public boolean needsToPlayFace();
	public int getTillFace();
	public void setTillFace(int i);
	public boolean myTurn();
	public void setMyTurn(boolean b);
	public int getID();
}
