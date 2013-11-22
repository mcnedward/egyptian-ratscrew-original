package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

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
public class Game {
	private static final String TAG = "Game";

	private static final String COMPUTER_PLAYER_NAME = "Android";
	private static final int DELAY_INTERVAL = 1000;
	private static final long TIME_BETWEEN_TURNS = 1500;
	public IPlayer player1;
	public IPlayer player2;
	public List<Card> theStack;

	// public Map<Integer, Card> middleStack;

	private CardDeck cd;
	private List<Card> cardDeck;
	private Context context;

	private long compSlapDelay;

	/**
	 * Game Constructor, creates a new instance of the game class
	 * sets up players, creates deck, shuffles and deals cards
	 * 
	 * @param onePlayerGame
	 * @param names
	 */
	public Game(boolean onePlayerGame, int difficulty, String[] names, Context con) {
		context = con;
		// set player variables
		if (names.length > 0)
			player1 = new HumanPlayer(names[0], 0);

		if (!onePlayerGame && names.length > 1) {
			player2 = new HumanPlayer(names[1], 1);
		} else {
			player2 = new ComputerPlayer(COMPUTER_PLAYER_NAME, 1);
		}

		compSlapDelay = difficulty * DELAY_INTERVAL;

		player1.setMyTurn(true);
		player2.setMyTurn(false);
		player1.setTillFace(0);
		player2.setTillFace(0);

		theStack = new ArrayList<Card>();

		// middleStack = new ConcurrentHashMap<Integer, Card>();

		// create deck

		cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
		// shuffleCards(cardDeck);
		// dealCards();

		// update graphics

	}

	/**
	 * Used to deal cards from the deck to each player.
	 */
	public void dealCards() {
		int i = 0;
		int x = 0;
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
	 * This method is used to set the coordinates of each card in the deck, whether it is in a player's hand, or in the
	 * middle stack.
	 * 
	 * @param surfaceView
	 *            The surface view that the cards will be displayed on. Needed in order to get the width and height of
	 *            the card table.
	 */
	public void setCardLocations(SurfaceView surfaceView) {
		float topX = 10;
		float topY = 10;
		float bottomX = surfaceView.getWidth() - 10;
		float bottomY = surfaceView.getBottom() - 20;
		int degree = 0;

		for (Card c : player2.getHand()) {
			c.setX(topX);
			c.setY(topY);
			topX += 14;
			topY += 0.5;
		}
		for (Card c : player1.getHand()) {
			c.setX(bottomX - c.getWidth());
			c.setY(bottomY - c.getHeight());
			if (player1.myTurn()) {
				setCardListener((bottomX - c.getWidth()), bottomY, c, surfaceView);
			}
			bottomX -= 14;
			bottomY -= 0.5;
		}
		for (Card c : theStack) {
			c.rotateCard(degree);
			c.setRotate(false);
			c.setX((surfaceView.getWidth() - c.getWidth()) / 2);
			c.setY((surfaceView.getHeight() - c.getHeight()) / 2);
			degree += 20;
		}
	}

	/**
	 * Used to play a card from either the player's or the computer's hand.
	 */
	public void playCard2() {
		/**
		 * Check the player's hands for any cards that are hidden, which should be only the top cards if they were
		 * played. If there are any hidden cards, remove them from the iterator and add that card to the middle stack.
		 */
		for (Iterator<Card> iterator = player1.getHand().iterator(); iterator.hasNext();) {
			Card card = iterator.next();
			if (card.isHidden()) {
				iterator.remove();
				theStack.add(card);
			}
		}
		for (Iterator<Card> iterator2 = player2.getHand().iterator(); iterator2.hasNext();) {
			Card card = iterator2.next();
			if (card.isHidden()) {
				iterator2.remove();
				theStack.add(card);
			}
		}

		/**
		 * Check if if if the computer's turn. If it is, start a new task that will play a card for the computer after
		 * the set delay.
		 */
		if (player2.myTurn()) {
			try {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
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

				}, DELAY_INTERVAL);	// TODO This delay interval will be determined by the difficulty later
			} catch (Exception e) {
				Log.i(TAG, e.getMessage(), e);
			}
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
	public void setCardListener(final float x, final float y, final Card card, SurfaceView surfaceView) {
		// Set the touch listener for the game surface
		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				if (action == MotionEvent.ACTION_UP) {
					if (eventX >= x && eventX < (x + card.getWidth()) && eventY >= y - card.getHeight()
							&& eventY < (y - card.getHeight() + card.getHeight())) {
						if (player1.myTurn()) {
							card.setHiddden(true);
							player1.setMyTurn(false);
							player2.setMyTurn(true);
						}
					}
				}
				return true;
			}
		});
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
		// int x = middleStack.size();
		int y = p.getHand().size();
		// middleStack.put(x, p.playCard(y));

		// need to update graphics here

		// if player needs to play a face and didnt
		// decrement turns left to play face
		if (p.needsToPlayFace() && !isFace(theStack.get(theStack.size() - 1))) {
			// if (p.needsToPlayFace() && !isFace(middleStack.get(middleStack.size() - 1))) {
			p.setTillFace(p.getTillFace() - 1);
		}
		// if player needs to play a face and did
		// switch players turn
		// set other player to need a face
		else if (p.needsToPlayFace() && isFace(theStack.get(theStack.size() - 1))) {
			// else if (p.needsToPlayFace() && isFace(middleStack.get(middleStack.size() - 1))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);
			p2.setTillFace(theStack.get(theStack.size() - 1).tillFaceValue);
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
			p2.setTillFace(theStack.get(theStack.size() - 1).tillFaceValue);
			// p2.setTillFace(middleStack.get(middleStack.size() - 1).tillFaceValue);
		}
		// if player does not need to play a face and doesnt
		// switch players turn
		else if (!p.needsToPlayFace() && theStack.size() > 0 && !isFace(theStack.get(theStack.size() - 1))) {
			// else if (!p.needsToPlayFace() && !isFace(middleStack.get(middleStack.size() - 1))) {
			p.setMyTurn(false);
			p2 = getOtherPlayer(p);
			p2.setMyTurn(true);

		}

		savePlayers(p, p2);

		// if the stack is slappable, start timer to slap stack
		if (slappable()) {
			Timer compSlapTimer = new Timer();
			compSlapTimer.schedule(new CompSlapTask(), compSlapDelay);
		}

		// if it is the computers turn (player 2) start timer to play card
		if (player2.myTurn()) {
			Timer compTurnTimer = new Timer();
			compTurnTimer.schedule(new CompTurnTask(), TIME_BETWEEN_TURNS);
		}

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
		if (theStack.size() > 1
				&& theStack.get(theStack.size() - 1).cardType.equals(theStack.get(theStack.size() - 2).cardType)) {
			retBool = true;
		}
		// if first and third card are the same (sandwich)
		else if (theStack.size() > 2
				&& theStack.get(theStack.size() - 1).cardType.equals(theStack.get(theStack.size() - 3).cardType)) {
			retBool = true;
		}
		// if top two cards add up to ten
		else if (theStack.size() > 1
				&& theStack.get(theStack.size() - 1).cardValue + theStack.get(theStack.size() - 2).cardValue == 10) {
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
			player2.getHand().get(player2.getHand().size() - 1).setHiddden(true);
			player2.setMyTurn(false);
		}
	}

}
