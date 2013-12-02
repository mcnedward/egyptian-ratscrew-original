package com.egyptianratscrew.dto;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.User;

public class HumanPlayer implements IPlayer {
	private static final String TAG = "HumanPlayer";

	private String playerName;
	private List<Card> hand;
	private boolean turn;
	private int tillFace;
	private int playerID;
	private IUser user;

	public HumanPlayer(IUser user) {
		if (user != null) {
			this.user = user;
		} else {
			this.user = new User("Egyptian", "Ratscrew", "Android", null, null);
			this.user.setUserId(0);
		}
		playerName = this.user.getUserName();
		playerID = this.user.getUserId();
		hand = new ArrayList<Card>();
	}

	@Override
	public void addCard(Card c) {
		hand.add(c);
	}

	@Override
	public Card playCard() {
		Card retCard = hand.get(hand.size() - 1);
		hand.remove(hand.size() - 1);
		return retCard;
	}

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

	@Override
	public void setTillFace(int tillFace) {
		this.tillFace = tillFace;
	}

	@Override
	public boolean myTurn() {
		return turn;
	}

	@Override
	public void setMyTurn(boolean b) {
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
		if (hand.size() == 51) {
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
	public Card getTopCard() {
		try {
			return hand.get(hand.size() - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.i(TAG, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void setCardCoor(float x, float y) {
		Card card = getTopCard();
		card.setX(x);
		card.setY(y);
	}

	@Override
	public IUser getUser() {
		// TODO Auto-generated method stub
		return null;
	}
}
