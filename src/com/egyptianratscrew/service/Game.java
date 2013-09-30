package com.egyptianratscrew.service;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.Card2;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.ComputerPlayer;
import com.egyptianratscrew.dto.HumanPlayer;
import com.egyptianratscrew.dto.IPlayer;

public class Game extends Activity {

	private IPlayer player1;
	private IPlayer player2;
	private List<Card2> theStack;
	private CardDeck cd;
	private List<Card> cardDeck;

	private RelativeLayout table;
	private GameSurface tableCardSurface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this);
		table.addView(tableCardSurface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Game() {

	}

	public Game(boolean onePlayerGame, String[] names) {
		// set player variables
		player1 = new HumanPlayer(names[0], 0);

		if (onePlayerGame) {
			player2 = new ComputerPlayer("Android");
		} else {
			player2 = new HumanPlayer(names[1], 1);
		}

		// create deck

		shuffleCards(cardDeck);

		// deal cards

	}

	public void dealCards() {
		// table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this);
		table.addView(tableCardSurface);

		cd = new CardDeck(this);
		cardDeck = cd.cardDeck;
	}

	public boolean playCard(int playerID) {
		IPlayer p = getPlayerFromID(playerID);
		IPlayer p2 = new HumanPlayer("empty", -1);
		theStack.add(p.playCard());
		// if player needs to play a face and didnt
		// decrement turns left to play face
		if (p.needsToPlayFace() && !isFace(theStack.get(theStack.size()))) {
			p.setTillFace(p.getTillFace() - 1);
		}
		// if player needs to play a face and did
		// switch players turn
		// set other player to need a face
		else if (p.needsToPlayFace() && isFace(theStack.get(theStack.size()))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);
			p2.setTillFace(theStack.get(theStack.size()).tillFaceValue);

		}
		// if player does not need to play a face and does
		// switch players turn
		// set other player to need a face
		else if (!p.needsToPlayFace() && isFace(theStack.get(theStack.size()))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);
			p2.setTillFace(theStack.get(theStack.size()).tillFaceValue);

		}
		// if player does not need to play a face and doesnt
		// switch players turn
		else if (!p.needsToPlayFace() && !isFace(theStack.get(theStack.size()))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);

		}

		savePlayers(p, p2);

		return true;
	}

	private void savePlayers(IPlayer p, IPlayer p2) {
		if (p.getID() == player1.getID()) {
			player1 = p;
			player2 = p2;
		} else {
			player1 = p2;
			player2 = p;
		}
	}

	private IPlayer getPlayerFromID(int playerID) {
		if (player1.getID() == playerID) {
			return player1;
		} else {
			return player2;
		}
	}

	private IPlayer getOtherPlayer(IPlayer p) {
		if (p.getName().equals(player1.getName())) {
			return player2;
		} else {
			return player1;
		}
	}

	private boolean isFace(Card2 c) {
		if (c.cardType.equalsIgnoreCase("jack") || c.cardType.equalsIgnoreCase("queen")
				|| c.cardType.equalsIgnoreCase("king") || c.cardType.equalsIgnoreCase("ace")) {
			return true;
		} else {
			return false;
		}

	}

	public void slapStack(int playerID) {
		IPlayer p = getPlayerFromID(playerID);

		if (slappable()) {
			for (Card2 c : theStack) {
				p.addCard(c);
			}
			theStack.removeAll(theStack);
			savePlayers(p, getOtherPlayer(p));
		}
	}

	public boolean slappable() {

		boolean retBool = false;

		// if top two cards are the same type
		if (theStack.get(theStack.size()).cardType.equals(theStack.get(theStack.size() - 1).cardType)) {
			retBool = true;
		}
		// if first and third card are the same (sandwich)
		else if (theStack.get(theStack.size()).cardType.equals(theStack.get(theStack.size() - 2).cardType)) {
			retBool = true;
		}
		// if top two cards add up to ten
		else if (theStack.get(theStack.size()).cardValue + theStack.get(theStack.size() - 1).cardValue == 10) {
			retBool = true;
		} else {

		}

		return retBool;
	}

	// Method that will shuffle the cards based on the Fisher-Yates Array Shuffle
	public void shuffleCards(List<Card> cardDeck) {
		int arrayLength = cardDeck.size();
		Random random = new Random();
		while (arrayLength > 1) {
			int nextRandom = random.nextInt(arrayLength--);
			Card card = cardDeck.get(nextRandom);
			cardDeck.set(nextRandom, cardDeck.get(arrayLength));
			cardDeck.set(arrayLength, card);
		}
	}

}
