package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.egyptianratscrew.dao.IGameFinishedListener;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.User;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.HumanPlayer;
import com.egyptianratscrew.dto.IPlayer;

/**
 * Game Class
 * 
 * @author AJ
 * 
 */
public class Game {
	// declaring variables and constants
	private static final String TAG = "Game";

	private static final String COMPUTER_PLAYER_NAME = "Android";
	private static final int DELAY_INTERVAL = 1000;
	private static final long TIME_BETWEEN_TURNS = 1500;
	private long compSlapDelay;

	public IPlayer player1;
	public IPlayer player2;
	public List<Card> theStack;
	private List<IGameFinishedListener> listeners;

	private CardDeck cd;
	private List<Card> cardDeck;
	private Context context;
	private IUser user;

	private Card blankCard;
	public int numberOfChances = 0;

	/**
	 * Game Constructor, creates a new instance of the game class
	 * sets up players, creates deck, shuffles and deals cards
	 * 
	 * @param onePlayerGame
	 * @param names
	 */
	public Game(boolean onePlayerGame, int difficulty, Context con) {
		context = con;
		blankCard = new Card(context);

		listeners = new ArrayList<IGameFinishedListener>();

		Intent intent = ((Activity) context).getIntent();
		Bundle extras = intent.getExtras();

		// Check if a user is logged in and make player 1 that user if true
		// Create a new blank user if there is no user logged in
		if (extras != null) {
			if (extras.containsKey("User")) {
				user = (User) ((Activity) context).getIntent().getExtras().getSerializable("User");
				player1 = new HumanPlayer(user);
			}
		} else {
			player1 = new HumanPlayer(new User("Player", "1", "Player 1", "player1@gmail.com", "password"));
			player2 = new HumanPlayer(null);
		}

		compSlapDelay = difficulty * DELAY_INTERVAL;

		player1.setMyTurn(true);
		player2.setMyTurn(false);
		player1.setTillFace(0);
		player2.setTillFace(0);

		theStack = new ArrayList<Card>();

		// Create the deck of cards
		cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
		// shuffleCards(cardDeck);
		dealCards();

		// update graphics

	}

	/**
	 * Used to deal cards from the deck to each player.
	 */
	public void dealCards() {
		int i = 0;
		for (Card c : cardDeck)// for (int i = 0; i > cardDeck.size(); i++)
		{
			if ((i % 2) == 0) {
				player1.addCard(c);// cardDeck.get(i));
			} else {
				player2.addCard(c);// cardDeck.get(i));
			}
			i++;
		}
	}

	/**
	 * This sets the onTouchListener to the top card in player 1's hand. If it is player 1's turn and a touch is
	 * received, then that card is removed from the player's hand and added to the middle stack. It is then the
	 * computer's turn, and the timer to allow the computer to play a card will start in the playCard method.
	 * 
	 * @param x
	 *            The x-coordinate for checking whether the touch was inside the top player's hand.
	 * @param y
	 *            The y-coordinate for checking whether the touch was inside the top player's hand.
	 * @param card
	 *            The top card in the player's hand.
	 * @param surfaceView
	 *            The surface view that this touch needs to be registered on.
	 */
	public void setCardListeners(final SurfaceView surfaceView, final Card player1Card) {
		// Set the touch listener for the game surface
		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				if (action == MotionEvent.ACTION_UP) {
					if (!player1.getHand().isEmpty()) {
						if (eventX >= player1Card.getX() && eventX < (player1Card.getX() + player1Card.getWidth())
								&& eventY >= player1Card.getY()
								&& eventY < (player1Card.getY() + player1Card.getHeight())) {
							if (player1.myTurn()) {
								throwCard(player1);
								if (numberOfChances == 0) {
									player1.setMyTurn(false);
									player2.setMyTurn(true);

									// Start a new timer for the computer's turn
									// TIME_BETWEEN_TURNS???
									Timer player2TurnTask = new Timer();
									player2TurnTask.schedule(new Player2TurnTask(), DELAY_INTERVAL);
								}
							}
						}
					}
					if (!theStack.isEmpty()) {
						Card middleCard = theStack.get(theStack.size() - 1);
						if (eventX >= middleCard.getX() && eventX < (middleCard.getX() + middleCard.getWidth())
								&& eventY >= middleCard.getY() && eventY < (middleCard.getY() + middleCard.getHeight())) {
							if (slappable()) {
								Log.i(TAG, "Adding cards to player 1");
								slapStack(player1);
							}
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * This method is used to throw a card into the middle deck. The timer for the computer player to slap the middle
	 * deck is also set here.
	 * 
	 * @param player
	 *            The player who is throwing cards into the middle deck.
	 */
	public void throwCard(IPlayer player) {
		if (player.getHand().isEmpty()) {
			DeclareWinner(getOtherPlayer(player));
		} else {
			Card cardToThrow = player.getTopCard();	// Get the top card from the player's hand

			// Check if the player has played a letter card. If they have, add their card to the top pile and throw
			// another
			// card. Then decrement the counter for their number of chances to get a letter card until they play one.
			if (numberOfChances > 0) {
				theStack.add(cardToThrow);
				player.getHand().remove(cardToThrow);
				if (cardToThrow.tillFaceValue > 0) {
					numberOfChances = 0;
					return;
				} else {
					numberOfChances--;
					// If they run out of chances to play a letter card, give all the cards in the middle stack to the
					// other
					// player.
					if (numberOfChances == 0) {
						slapStack(getOtherPlayer(player));
					}
				}
			} else {
				theStack.add(cardToThrow);
				player.getHand().remove(cardToThrow);
				numberOfChances = cardToThrow.tillFaceValue;
			}

			// Check if the middle deck is slappable after every throw
			if (slappable()) {
				// Start a new timer for the computer's turn
				// TODO TIME_BETWEEN_TURNS???
				Timer player2SlapTask = new Timer();
				player2SlapTask.schedule(new Player2SlapTask(), DELAY_INTERVAL);
			}
		}
	}

	/**
	 * This method will slap the middle stack and add all of the cards there to the player who slapped it.
	 * 
	 * @param player
	 *            The player who slapped the middle deck.
	 */
	public void slapStack(IPlayer player) {
		List<Card> cardsToAdd = new ArrayList<Card>();
		for (int i = 0; i < theStack.size(); i++) {
			Card c = theStack.get(i);
			// TODO Remove this (next line only) in final version when cards in player hands are displayed as facedown
			c.resetCardBitmap();
			c.setHiddden(false);
			cardsToAdd.add(c);
		}
		theStack = new ArrayList<Card>();
		player.getHand().addAll(0, cardsToAdd);
		// Reset the number of chances to play a letter card if a player has those chances
		if (numberOfChances > 0)
			numberOfChances = 0;
	}

	class Player2TurnTask extends TimerTask {

		public Player2TurnTask() {

		}

		@Override
		public void run() {
			/**
			 * Check again if it is the computer's turn still, because the game is constantly being run and
			 * drawn in the GameSurface class. If this check is not here, the timer task will continue
			 * removing cards until the run method in GameSurface reaches this point again.
			 */
			if (player2.myTurn()) {
				/**
				 * Get the last card in the computer's hand and hide it, then set the turn to player 1 and
				 * set the computer's turn off
				 */
				throwCard(player2);
				if (numberOfChances == 0) {
					player2.setMyTurn(false);
					player1.setMyTurn(true);
				} else {
					Timer player2TurnTask = new Timer();
					player2TurnTask.schedule(new Player2TurnTask(), DELAY_INTERVAL);
				}
			}
		}
	}

	class Player2SlapTask extends TimerTask {

		public Player2SlapTask() {

		}

		// slap deck add the card to the correct player
		@Override
		public void run() {
			Log.i(TAG, "Adding cards to player 2");
			slapStack(player2);
		}
	}

	/**
	 * Play Card Method
	 * will get called each time either player plays a card, only needs a player ID
	 * 
	 * @param playerID
	 * @return
	 */
	public boolean playCard(IPlayer p) {
		IPlayer p2 = new HumanPlayer(null);
		Card middleTopCard = theStack.get(theStack.size() - 1);

		// need to update graphics here

		// if player needs to play a face and didnt
		// decrement turns left to play face
		if (p.needsToPlayFace() && !isFace(middleTopCard)) {
			// if (p.needsToPlayFace() && !isFace(middleStack.get(middleStack.size() - 1))) {
			p.setTillFace(p.getTillFace() - 1);
		}
		// if player needs to play a face and did
		// switch players turn
		// set other player to need a face
		else if (p.needsToPlayFace() && isFace(middleTopCard)) {
			// else if (p.needsToPlayFace() && isFace(middleStack.get(middleStack.size() - 1))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);
			p2.setTillFace(middleTopCard.tillFaceValue);
			// p2.setTillFace(middleStack.get(middleStack.size() - 1).tillFaceValue);
		}
		// if player does not need to play a face and does
		// switch players turn
		// set other player to need a face
		else if (!p.needsToPlayFace() && theStack.size() > 0 && isFace(theStack.get(theStack.size() - 1))) {
			// else if (!p.needsToPlayFace() && isFace(middleStack.get(middleStack.size() - 1))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);
			p2.setTillFace(middleTopCard.tillFaceValue);
			// p2.setTillFace(middleStack.get(middleStack.size() - 1).tillFaceValue);
		}
		// if player does not need to play a face and doesnt
		// switch players turn
		else if (!p.needsToPlayFace() && theStack.size() > 0 && !isFace(middleTopCard)) {
			// else if (!p.needsToPlayFace() && !isFace(middleStack.get(middleStack.size() - 1))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);

		}

		if (p2.getID() != -1)
			savePlayers(p, p2);

		// if the stack is slappable, start timer to slap stack
		if (slappable()) {
			Timer compSlapTimer = new Timer();
			compSlapTimer.schedule(new CompSlapTask(), compSlapDelay);
		}

		// if it is the computers turn (player 2) start timer to play card
		if (player2.myTurn()) {
			Timer compTurnTimer = new Timer();
			compTurnTimer.schedule(new CompTurnTask(), DELAY_INTERVAL);	// TODO TIME_BETWEEN_TURNS???
		}

		// For testing game over scenarios
		// DeclareWinner(player1);
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
	 * returns IPlayer the corresponds with playerID
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
	 * @param player
	 * @return
	 */
	private IPlayer getOtherPlayer(IPlayer player) {
		if (player.getName().equals(player1.getName())) {
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
			List<Card> cardsToAdd = new ArrayList<Card>();
			for (ListIterator<Card> iterator = theStack.listIterator(); iterator.hasNext();) {
				Card c = iterator.next();
				c.resetCardBitmap();
				c.setHiddden(false);
				cardsToAdd.add(c);
			}

			theStack = new ArrayList<Card>();
			p.getHand().addAll(0, cardsToAdd);

			// need to update graphics here

			// savePlayers(p, getOtherPlayer(p));
			if (p.hasAllCards()) {
				DeclareWinner(p);
			}
		}
	}

	/***
	 * Checks to see if the passed in player has all 52 cards and calls declare winner
	 * 
	 * @param p
	 */
	private void checkWinner(IPlayer p) {
		if (p.hasAllCards()) {
			DeclareWinner(p);
		}
	}

	/**
	 * returns true if the stack is slappable
	 * 
	 * @return
	 */
	public boolean slappable() {
		boolean retBool = false;

		if (theStack.size() > 1) {
			Card topCard = theStack.get(theStack.size() - 1);
			Card secondCard = theStack.get(theStack.size() - 2);
			if (topCard.cardType.equals(secondCard.cardType)) {
				retBool = true;
			} else if (topCard.cardValue + secondCard.cardValue == 10) {
				retBool = true;
			}
		}
		if (theStack.size() > 2) {
			Card topCard = theStack.get(theStack.size() - 1);
			Card thirdCard = theStack.get(theStack.size() - 3);
			if (topCard.cardType.equals(thirdCard.cardType)) {
				retBool = true;
			}
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
		//GameFinished(this);
	}

	//
	protected void GameFinished(Game2 game) {
		// Object lock = new Object();
		for (IGameFinishedListener listener : listeners) {
			synchronized (this) {
				listener.onGameFinished(game);
			}
		}
	}

	public void registerGameFinishedListener(IGameFinishedListener listener) {
		listeners.add(listener);
	}

	// shuffle the cards and getting the cards to the players
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

	// create the computer with an id and name

	private IUser CreateComputer() {
		IUser u = new User();
		u.setUserId(0);
		u.setUserName(COMPUTER_PLAYER_NAME);
		return u;
	}

	class CompSlapTask extends TimerTask {

		public CompSlapTask() {

		}

		@Override
		public void run() {
			slapStack(player2.getID());
		}
	}

	class CompTurnTask extends TimerTask {

		public CompTurnTask() {

		}

		@Override
		public void run() {
			/**
			 * Check again if it is the computer's turn still, because the game is constantly being run and
			 * drawn in the GameSurface class. If this check is not here, the timer task will continue
			 * removing cards until the run method in GameSurface reaches this point again.
			 */
			if (player2.myTurn()) {
				/**
				 * Get the last card in the computer's hand and hide it, then set the turn to player 1 and
				 * set the computer's turn off
				 */
				player2.getHand().get(player2.getHand().size() - 1).setHiddden(true);
				player1.setMyTurn(true);
				player2.setMyTurn(false);
			}
		}
	}

}
