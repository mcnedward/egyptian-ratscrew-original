package com.egyptianratscrew.dto;

import java.util.List;

public class HumanPlayer implements IPlayer {
	private String playerName;
	private List<Card2> hand;
	private boolean turn;
	private int tillFace;
	private int playerID;
	
	public HumanPlayer(String name, int id) {
		playerName = name;
		playerID = id;
	}

	@Override
	public void addCard(Card2 c) {
		// TODO Auto-generated method stub
		hand.add(c);
	}

	@Override
	public Card2 playCard() {
		// TODO Auto-generated method stub
		Card2 retCard = hand.get(0);
		hand.remove(0);
		return retCard;
	}

	@Override
	public boolean needsToPlayFace() {
		// TODO Auto-generated method stub
		if (getTillFace() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int getTillFace() {
		// TODO Auto-generated method stub
		return tillFace;
	}

	@Override
	public void setTillFace(int i) {
		// TODO Auto-generated method stub
		tillFace = i;
	}

	@Override
	public boolean myTurn() {
		// TODO Auto-generated method stub
		return turn;
	}

	@Override
	public void setMyTurn(boolean b) {
		// TODO Auto-generated method stub
		turn = b;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return playerName;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public boolean hasAllCards() {
		// TODO Auto-generated method stub
		if (hand.size() == 52) {
			return true;
		}
		else {
			return false;
		}
	}
}
