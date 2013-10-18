package com.egyptianratscrew.service;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.ComputerPlayer;
import com.egyptianratscrew.dto.HumanPlayer;
import com.egyptianratscrew.dto.IPlayer;

/**
 * Game Class
 * 
 * @author AJ
 * 
 */
public class Game extends Activity {

	private static final String COMPUTER_PLAYER_NAME = "Android";
	private IPlayer player1;
	private IPlayer player2;
	private List<Card> theStack;

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

	public Game() {

	}

	/**
	 * Game Constructor, creates a new instance of the game class
	 * sets up players, creates deck, shuffles and deals cards
	 * 
	 * @param onePlayerGame
	 * @param names
	 */
	public Game(boolean onePlayerGame, String[] names) {
		// set player variables
		player1 = new HumanPlayer(names[0], 0);

		if (onePlayerGame) {
			player2 = new ComputerPlayer(COMPUTER_PLAYER_NAME, 1);
		} else {
			player2 = new HumanPlayer(names[1], 1);
		}

		// create deck
		shuffleCards(cardDeck);

		// shuffle cards
	}

	// deal cards
	public void dealCards() {
		tableCardSurface = new GameSurface(this);
		table.addView(tableCardSurface);
		cd = new CardDeck(this);
		cardDeck = cd.cardDeck;

	}

	/**
	 * Play Card Method
	 * will get called each time either player plays a card, only needs a player ID
	 * 
	 * @param playerID
	 * @return
	 */
	public boolean playCard(int playerID) {
		IPlayer p = getPlayerFromID(playerID);
		IPlayer p2 = new HumanPlayer("empty", -1);
		theStack.add(p.playCard());

		// need to update graphics here

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

	/**
	 * Saves temp players to the local variables player1 and player2
	 * 
	 * @param p
	 * @param p2
	 */
	private void savePlayers(IPlayer p, IPlayer p2) {
		if (p.getID() == player1.getID()) {
			player1 = p;
			player2 = p2;
		} else {
			player1 = p2;
			player2 = p;
		}
	}

	/**
	 * returns IPlayer the coresponds with playerID
	 * 
	 * @param playerID
	 * @return
	 */
	private IPlayer getPlayerFromID(int playerID) {
		if (player1.getID() == playerID) {
			return player1;
		} else {
			return player2;
		}
	}

	/**
	 * returns the opposite player
	 * 
	 * @param p
	 * @return
	 */
	private IPlayer getOtherPlayer(IPlayer p) {
		if (p.getName().equals(player1.getName())) {
			return player2;
		} else {
			return player1;
		}
	}

	/**
	 * returns true if the passed in card is a face or ace
	 * 
	 * @param c
	 * @return
	 */
	private boolean isFace(Card c) {
		if (c.cardType.equalsIgnoreCase("jack") || c.cardType.equalsIgnoreCase("queen")
				|| c.cardType.equalsIgnoreCase("king") || c.cardType.equalsIgnoreCase("ace")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * gets called with each attempt to slap the stack
	 * also declares winner happens here
	 * 
	 * @param playerID
	 */
	public void slapStack(int playerID) {
		IPlayer p = getPlayerFromID(playerID);

		if (slappable()) {
			for (Card c : theStack) {
				p.addCard(c);
			}
			theStack.removeAll(theStack);

			// need to update graphics here

			savePlayers(p, getOtherPlayer(p));
			if (p.hasAllCards()) {
				DeclareWinner(p);
			}
		}
	}

	/**
	 * returns true if the stack is slappable
	 * 
	 * @return
	 */
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

	/**
	 * do stuff with the winning player
	 * 
	 * @param p
	 */
	private void DeclareWinner(IPlayer p) {
		// do stuff with winner
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
