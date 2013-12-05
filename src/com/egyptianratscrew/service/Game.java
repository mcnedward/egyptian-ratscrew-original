package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dao.IGameFinishedListener;
import com.egyptianratscrew.dao.IUser;
import com.egyptianratscrew.dao.RatscrewDatabase;
import com.egyptianratscrew.dao.User;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.HumanPlayer;
import com.egyptianratscrew.dto.IPlayer;

/**
 * <p>
 * Game Class
 * <p>
 * This class creates an instance of the Egyptian Ratscrew game. All game logic is handled here, and the drawGame method
 * is created here. By calling drawGame and passing it a canvas, the card game will be started and run.
 * 
 * @author AJ, Edward
 * 
 */
public class Game {
	private static final String TAG = "Game2";	// Tag for logging errors

	private static final int DELAY_INTERVAL = 1000;	// Delay for computer to play a card or slap
	private static final long TIME_BETWEEN_TURNS = 1500;
	private static boolean GAME_STARTED = false;	// Tells the Surface View thread that the game should be ran
	private static boolean SLAPPED = false;			// Allows the middle deck to be slapped

	private Context context;		// Context of the activity to run the game on
	private RatscrewDatabase db;	// Database for updating a winner
	private List<IGameFinishedListener> listeners;
	private IUser user;				// The user for player 1

	public IPlayer player1;			// Interface for player 1
	public IPlayer player2;			// Interface for player 2
	public List<Card> theStack;		// The middle stack of cards that have been played
	private CardDeck cd;			// Card deck object for creating the list of cards
	private List<Card> cardDeck;	// List of cards for the card deck
	private Bitmap cardBack;		// The card back that will be used if the user took a picture

	/**
	 * Game Constructor, creates a new instance of the game class
	 * sets up players, creates deck, shuffles and deals cards
	 * 
	 * @param onePlayerGame
	 * @param names
	 */
	public Game(boolean onePlayerGame, IUser u, int difficulty, Bitmap cardBack, Context con) {
		this.context = con;
		this.user = u;
		db = new RatscrewDatabase(context);

		listeners = new ArrayList<IGameFinishedListener>();

		this.cardBack = cardBack;

		// Check if a user is logged in and make player 1 that user if true
		// Create a new blank user if there is no user logged in
		if (user != null) {
			player1 = new HumanPlayer(user);
		} else {
			player1 = new HumanPlayer(new User("Player", "1", "Player 1", "player1@gmail.com", "password"));
		}
		player2 = new HumanPlayer(null);
	}

	/**
	 * Used to deal cards from the deck to each player.
	 */
	public void dealCards() {
		player1.setHand(new ArrayList<Card>());
		player2.setHand(new ArrayList<Card>());
		player1.setMyTurn(true);
		player2.setMyTurn(false);
		player1.setTillFace(0);
		player2.setTillFace(0);

		theStack = new ArrayList<Card>();
		// Create the deck of cards
		cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
		shuffleCards(cardDeck);

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
		GAME_STARTED = true;
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
	public void setCardListeners(final SurfaceView surfaceView) {
		// Set the touch listener for the game surface
		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				if (action == MotionEvent.ACTION_UP) {

					if (GAME_STARTED) {
						float bottomX = surfaceView.getWidth() - 10;
						float bottomY = surfaceView.getBottom() - 20;
						Card player1Card = new Card(context);
						player1Card.setX(bottomX - player1Card.getWidth());
						player1Card.setY(bottomY - player1Card.getHeight());
						if (eventX >= player1Card.getX() && eventX < (player1Card.getX() + player1Card.getWidth())
								&& eventY >= player1Card.getY()
								&& eventY < (player1Card.getY() + player1Card.getHeight())) {
							if (player1.myTurn()) {
								if (!player1.getHand().isEmpty()) {
									throwCard(player1);
									return false;
								}
							}
						}
						if (!theStack.isEmpty()) {
							Card middleCard = theStack.get(theStack.size() - 1);
							if (eventX >= middleCard.getX() && eventX < (middleCard.getX() + middleCard.getWidth())
									&& eventY >= middleCard.getY()
									&& eventY < (middleCard.getY() + middleCard.getHeight())) {
								if (slappable()) {
									Log.i(TAG, "Adding cards to player 1");
									slapStack(player1);
									return false;
								}
							}
						}
					} else {
						Card cardDeck = new Card(context);
						cardDeck.setX((surfaceView.getWidth() - cardDeck.getWidth()) / 2);
						cardDeck.setY((surfaceView.getHeight() - cardDeck.getHeight()) / 2);
						if (eventX >= cardDeck.getX() && eventX < (cardDeck.getX() + cardDeck.getWidth())
								&& eventY >= cardDeck.getY() && eventY < (cardDeck.getY() + cardDeck.getHeight())) {
							dealCards();
							return false;
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
		Card cardToThrow = player.getTopCard();	// Get the top card from the player's hand

		// First check if the player has already played a face card by getting their till face value. If it is greater
		// than 0, then they have played a face card. So add the card to throw to the middle stack and remove it from
		// the player's hand.
		if (player.getTillFace() > 0) {
			theStack.add(cardToThrow);
			player.getHand().remove(cardToThrow);
			// Then check if the card that was thrown is a face card. If it is, then that player's turn is over. If it
			// is not a face card, then reduce the player's till face value by one.
			if (cardToThrow.tillFaceValue > 0) {
				player.setTillFace(0);
				player.setMyTurn(false);
				getOtherPlayer(player).setMyTurn(true);
			} else {
				player.setTillFace(player.getTillFace() - 1);
				// If they run out of chances to play a face card, then the middle deck goes to the other player.
				if (player.getTillFace() == 0) {
					if (!SLAPPED)
						slapStack(getOtherPlayer(player));
				}
			}
		}
		// If the player has not already played a face card, then check if the card thrown is a face card. If it is, add
		// it to the middle deck and set the player's till face value. Then return (void) so that either a touch can be
		// handled for player 1, or a new turn task can be scheduled for player 2.
		else if (cardToThrow.tillFaceValue > 0) {
			theStack.add(cardToThrow);
			player.getHand().remove(cardToThrow);
			player.setMyTurn(false);
			IPlayer playerB = getOtherPlayer(player);
			playerB.setMyTurn(true);
			playerB.setTillFace(cardToThrow.tillFaceValue);
		}
		// If the card thrown is not a face card, then simply add it to the middle deck and switch player turns.
		else {
			theStack.add(cardToThrow);
			player.getHand().remove(cardToThrow);
			player.setMyTurn(false);
			getOtherPlayer(player).setMyTurn(true);
		}
		if (player2.myTurn()) {
			// Start a new timer for the computer's turn
			// TIME_BETWEEN_TURNS???
			Timer player2TurnTask = new Timer();
			player2TurnTask.schedule(new Player2TurnTask(), DELAY_INTERVAL);
		}
	}

	/**
	 * This method will slap the middle stack and add all of the cards there to the player who slapped it.
	 * 
	 * @param player
	 *            The player who slapped the middle deck.
	 */
	public void slapStack(IPlayer player) {
		SLAPPED = true;
		for (int i = 0; i < theStack.size(); i++) {
			Card c = theStack.get(i);
			c.resetCardBitmap();
			player.addCard(c);
		}
		theStack = new ArrayList<Card>();
		// Reset the number of chances to play a letter card if a player has those chances
		player.setTillFace(0);
		player.setMyTurn(true);
		IPlayer playerB = getOtherPlayer(player);
		playerB.setTillFace(0);
		playerB.setMyTurn(false);

		SLAPPED = false;

		if (player2.myTurn()) {
			// Start a new timer for the computer's turn
			// TIME_BETWEEN_TURNS???
			Timer player2TurnTask = new Timer();
			player2TurnTask.schedule(new Player2TurnTask(), DELAY_INTERVAL);
		}
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
				if (!player2.getHand().isEmpty()) {
					/**
					 * Get the last card in the computer's hand and hide it, then set the turn to player 1 and
					 * set the computer's turn off
					 */
					throwCard(player2);
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
			if (slappable()) {
				if (!SLAPPED) {
					Log.i(TAG, "Adding cards to player 2");
					slapStack(player2);
				}
			}
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

	/***
	 * Checks to see if the passed in player has all 52 cards and calls declare winner
	 * 
	 * @param player
	 */
	public void checkWinner() {
		if (player1.getHand().isEmpty() && player1.myTurn()) {
			if (!slappable()) {
				declareWinner(player2);
				return;
			}
		}
		if (player2.getHand().isEmpty() && player2.myTurn()) {
			if (!slappable()) {
				declareWinner(getOtherPlayer(player1));
				return;
			}
		}
		if (player1.getHand().size() == cardDeck.size()) {
			declareWinner(player1);
			return;
		}
		if (player2.getHand().size() == cardDeck.size()) {
			declareWinner(player2);
			return;
		}
	}

	public void declareWinner(final IPlayer player) {

		GAME_STARTED = false;
		GameFinished(this);

	}

	protected void GameFinished(Game game) {
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

	public boolean isStarted() {
		return GAME_STARTED;
	}

	public void setStarted(boolean start) {
		GAME_STARTED = start;
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

	/**
	 * This method is used to draw all the components of the game. The card locations will first be set, then the
	 * playCard method will be called to check if there are any cards that need to be removed. After all of those
	 * methods have ran, the onDraw method for each card in both player's hands and the middle stack will be called so
	 * that the cards can be drawn on the canvas.
	 * 
	 * @param canvas
	 *            The canvas that the cards will be drawn on.
	 */
	@SuppressLint("WrongCall")
	public void drawGame(Canvas canvas) {
		float topX = 10;
		float topY = 10;
		float bottomX = canvas.getWidth() - 10;
		float bottomY = canvas.getHeight() - 20;

		Card player1Card = new Card(context);
		player1Card.setX(bottomX - player1Card.getWidth());
		player1Card.setY(bottomY - player1Card.getHeight());
		Card player2Card = new Card(context);
		player2Card.setX(topX);
		player2Card.setY(topY);
		if (cardBack != null) {
			player1Card.setCardBitmap(cardBack);
			player2Card.setCardBitmap(cardBack);
		}

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		paint.setTextAlign(Align.CENTER);

		canvas.drawColor(context.getResources().getColor(R.color.green));	// Set the background color of the canvas

		if (isStarted()) {
			checkWinner();
			if (!player1.getHand().isEmpty()) {
				// game.player1.getTopCard().onDraw(canvas);
				player1Card.onDraw(canvas);
				canvas.drawText("Card Count: " + player1.getHand().size(),
						(canvas.getWidth() - player1Card.getWidth()) / 2, player1Card.getY() + player1Card.getHeight()
								- 10, paint);
			}
			if (!player2.getHand().isEmpty()) {
				// game.player2.getTopCard().onDraw(canvas);
				player2Card.onDraw(canvas);
				canvas.drawText("Card Count: " + player2.getHand().size(),
						(canvas.getWidth() + player2Card.getWidth()) / 2, topY - paint.ascent(), paint);
			}
			if (!theStack.isEmpty()) {
				int degree = 0;
				for (int i = 0; i < theStack.size(); i++) {
					Card c = theStack.get(i);
					c.rotateCard(degree);
					c.setRotate(false);
					c.setX((canvas.getWidth() - c.getWidth()) / 2);
					c.setY((canvas.getHeight() - c.getHeight()) / 2);
					degree += 45;
					c.onDraw(canvas);
				}
			}
			if (player1.myTurn()) {
				canvas.drawText(player1.getName() + "'s turn!!!", (canvas.getWidth() / 2), (canvas.getHeight() / 2)
						+ player1Card.getHeight(), paint);
				if (player1.getTillFace() > 0)
					canvas.drawText("Chances to play card: " + player1.getTillFace(), (canvas.getWidth() / 2),
							(canvas.getHeight() / 2) + player1Card.getHeight() + 50, paint);
			}
			if (player2.myTurn()) {
				canvas.drawText(player2.getName() + "'s turn!!!", (canvas.getWidth() / 2), (canvas.getHeight() / 2)
						- player2Card.getHeight(), paint);
				if (player2.getTillFace() > 0)
					canvas.drawText("Chances to play card: " + player2.getTillFace(), (canvas.getWidth() / 2),
							(canvas.getHeight() / 2) - player2Card.getHeight() - 50, paint);
			}
			if (slappable()) {
				paint.setTextAlign(Align.LEFT);
				canvas.drawText("SLAP!!!", 10, canvas.getHeight() / 2, paint);
				paint.setTextAlign(Align.CENTER);

				// Start a new timer for the computer's turn
				// TODO TIME_BETWEEN_TURNS???
				Timer player2SlapTask = new Timer();
				player2SlapTask.schedule(new Player2SlapTask(), DELAY_INTERVAL);
			}
		} else {
			Card cardDeck = new Card(context);
			float x = (canvas.getWidth() - cardDeck.getWidth()) / 2;
			float y = (canvas.getHeight() - cardDeck.getHeight()) / 2;
			cardDeck.setX(x);
			cardDeck.setY(y);
			cardDeck.onDraw(canvas);
			canvas.drawText("TAP THE DECK TO START!!!", (canvas.getWidth() / 2),
					(canvas.getHeight() / 2) + cardDeck.getHeight(), paint);
		}
	}
}
