package com.egyptianratscrew.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HumanPlayer implements IPlayer {
	private String playerName;
	private List<Card> hand;
	private boolean turn;
	private int tillFace;
	private int playerID;

	private Map<Integer, Card> playerHand;

	public HumanPlayer(String name, int id) {
		playerName = name;
		playerID = id;
		hand = new ArrayList<Card>();
	}

	@Override
	public void addCard(Card c) {
		// TODO Auto-generated method stub
		hand.add(c);
	}

	@Override
	public Card playCard() {
		// TODO Auto-generated method stub
		Card retCard = hand.get(hand.size() - 1);
		hand.remove(hand.size() - 1);
		return retCard;
	}

	@Override
	public boolean needsToPlayFace() {
		// TODO Auto-generated method stub
		if (getTillFace() > 0) {
			return true;
		} else {
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
		} else {
			return false;
		}
	}

	@Override
	public List<Card> getHand() {
		return hand;
	}
}
