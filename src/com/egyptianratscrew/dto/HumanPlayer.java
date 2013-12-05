package com.egyptianratscrew.dto;

import java.util.ArrayList;
import java.util.List;

import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.User;

public class HumanPlayer implements IPlayer {
	// declare variable and values
	private static final String TAG = "HumanPlayer";

	private String playerName;
	private List<Card> hand;
	private boolean turn;
	private int tillFace;
	private int playerID;
	private IUser user;

	/**
	 * 
	 * @param user getting the user information
	 */
	public HumanPlayer(IUser user) {
		// if the user exists set to the user of the game
		if (user != null) {
			this.user = user;
			// if the user not there then set to 0 for the ID
		} else {
			this.user = new User("Egyptian", "Ratscrew", "Android", null, null);
			this.user.setUserId(0);
		}

		// getting the username and the userid with the hand
		playerName = this.user.getUserName();
		playerID = this.user.getUserId();
		hand = new ArrayList<Card>();
	}

	/**
	 * add the card to the hand
	 */
	@Override
	public void addCard(Card c) {
		hand.add(c);
	}

	/**
	 * remove the card from the hand and return the hand size less than one
	 */
	@Override
	public Card playCard() {
		Card retCard = hand.get(hand.size() - 1);
		hand.remove(hand.size() - 1);
		return retCard;
	}

	// must play a face return true if jack, queen, king, and ace
	@Override
	public boolean needsToPlayFace() {
		if (tillFace > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getTillFace() {
		return tillFace;
	}

	// setting the tillFace
	@Override
	public void setTillFace(int tillFace) {
		this.tillFace = tillFace;
	}

	// return true if myturn
	@Override
	public boolean myTurn() {
		return turn;
	}

	// setting the setMyTurn
	@Override
	public void setMyTurn(boolean b) {
		turn = b;
	}

	@Override
	public String getName() {

		return playerName;
	}

	@Override
	public int getID() {

		return playerID;
	}

	@Override
	public boolean hasAllCards() {
		if (hand.size() == 11) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Card> getHand() {
		return hand;
	}

	@Override
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	@Override
	public Card getTopCard() {
		return hand.get(hand.size() - 1);
	}

	@Override
	public void setCardCoor(float x, float y) {
		
		Card card = getTopCard();
		if (card != null) {
			//set the coordinates of the cards
			card.setX(x);
			card.setY(y);
		}
	}

	@Override
	public IUser getUser() {
		return user;
	}
}
