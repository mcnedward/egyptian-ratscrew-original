package com.egyptianratscrew.dto;

import java.util.ArrayList;
import java.util.List;

import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.UserStub;

public class PlayerStub implements IPlayer {
	// declaring variables
	private String playerName;
	private List<Card> hand;
	private boolean turn;
	private int tillFace;
	private int playerID;
	private IUser user;

	public PlayerStub(int id) {
		// setting the user to the UserStub information
		user = new UserStub();
		// getting the username of the player
		playerName = user.getUserName();
		// the id setting to the playerID
		playerID = id;
		// hand is a new ArrayList
		hand = new ArrayList<Card>();
	}

	@Override
	public void addCard(Card c) {
		// add the card to the hand
		hand.add(c);
	}

	@Override
	public Card playCard() {
		// playing a card and taking the the stack to one less card
		Card retCard = hand.get(hand.size() - 1);
		hand.remove(hand.size() - 1);
		return retCard;
	}

	@Override
	public boolean needsToPlayFace() {
		// must play a face card
		if (getTillFace() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getTillFace() {
		// getting the tillFace value
		return tillFace;
	}

	@Override
	public void setTillFace(int i) {
		// setting the TillFace
		tillFace = i;
	}

	@Override
	public boolean myTurn() {
		// return turn if it is players turn
		return turn;
	}

	@Override
	public void setMyTurn(boolean b) {
		// return turn to set to myturn
		turn = b;
	}

	@Override
	public String getName() {
		// returning the name of the player
		return playerName;
	}

	@Override
	public int getID() {
		// returning the playerID
		return playerID;
	}

	@Override
	public boolean hasAllCards() {
		// once the player has 52 cards in their hand they gain all cards
		if (hand.size() == 51) {
			return true;
			// if not then the game continues
		} else {
			return false;
		}
	}

	@Override
	public List<Card> getHand() {
		return hand;
	}

	@Override
	public Card getTopCard() {
		return hand.get(hand.size() - 1);
	}

	@Override
	public void setCardCoor(float x, float y) {
		Card card = getTopCard();
		card.setX(x);
		card.setY(y);
	}

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public void setHand(ArrayList<Card> list) {
		// TODO Auto-generated method stub

	}
}
