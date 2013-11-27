package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

import com.egyptianratscrew.dao.User;
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
	public Game(boolean onePlayerGame, int difficulty, User[] users, Context con) {
		context = con;
		// set player variables
		if (users.length > 0)
			player1 = new HumanPlayer(users[0], 0);

		if (!onePlayerGame && users.length > 1) {
			player2 = new HumanPlayer(users[1], 1);
		} else {
			player2 = new ComputerPlayer(CreateComputer(),1);
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

		player1.setCardCoor((bottomX - player1.getTopCard().getWidth()), (bottomY - player1.getTopCard().getHeight()));
		if (player1.myTurn()) {
			setCardListener(player1.getTopCard(), surfaceView);
		}
		player2.setCardCoor(topX, topY);
		for (Card c : theStack) {
			c.rotateCard(degree);
			c.setRotate(false);
			c.setX((surfaceView.getWidth() - c.getWidth()) / 2);
			c.setY((surfaceView.getHeight() - c.getHeight()) / 2);
			degree += 20;
			setCardListener(c, surfaceView);
		}
	}

	/**
	 * Used to play a card from either the player's or the computer's hand.
	 */
	public void displayCards() {
		/**
		 * Check the player's hands for any cards that are hidden, which should be only the top cards if they were
		 * played. If there are any hidden cards, remove them from the iterator and add that card to the middle stack.
		 */
		if (player1.getTopCard().isHidden()) {
			theStack.add(player1.getTopCard());
			player1.getHand().remove(player1.getTopCard());
			playCard(player1);
		}
		if (player2.getTopCard().isHidden()) {
			theStack.add(player2.getTopCard());
			player2.getHand().remove(player2.getTopCard());
			playCard(player2);
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
	public void setCardListener(final Card card, SurfaceView surfaceView) {
		final float playerCardX = (surfaceView.getWidth() - 10) - card.getWidth();
		final float playerCardY = surfaceView.getBottom() - 20;
		final float middleCardX = (surfaceView.getWidth() - card.getWidth()) / 2;
		final float middleCardY = (surfaceView.getHeight() - card.getHeight()) / 2;

		// Set the touch listener for the game surface
		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				if (action == MotionEvent.ACTION_UP) {
					if (eventX >= playerCardX && eventX < (playerCardX + card.getWidth())
							&& eventY >= playerCardY - card.getHeight()
							&& eventY < (playerCardY - card.getHeight() + card.getHeight())) {
						if (player1.myTurn()) {
							player1.getTopCard().setHiddden(true);
							player1.setMyTurn(false);
							player2.setMyTurn(true);
						}
					}
					if (eventX >= middleCardX && eventX < (middleCardX + card.getWidth()) && eventY >= middleCardY
							&& eventY < (middleCardY + card.getHeight())) {
						if (slappable()) {
							Log.i(TAG, "Adding cards to player 1");
							List<Card> cardsToAdd = new ArrayList<Card>();
							for (ListIterator<Card> iterator = theStack.listIterator(); iterator.hasNext();) {
								Card c = iterator.next();
								c.resetCardBitmap();
								c.setHiddden(false);
								cardsToAdd.add(c);
							}
							theStack = new ArrayList<Card>();
							player1.getHand().addAll(0, cardsToAdd);
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
	public boolean playCard(IPlayer p) {
		IPlayer p2 = new HumanPlayer(null, -1);
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
			// if (p.hasAllCards()) {
			// DeclareWinner(p);
			// }
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
		} else if (theStack.size() > 2) {
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
	
	private User CreateComputer(){
		User u = new User();
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
