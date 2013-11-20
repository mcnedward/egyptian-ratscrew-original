package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.CardDeck;
import com.egyptianratscrew.dto.IPlayer;

/**
 * 
 * Source for some code: http://https417.blogspot.com/2012/08/threading-surfaceview-in-android.html
 * 
 * @author Edward
 * 
 */
public class GameThread2 extends Thread {
	private static String TAG = "GameThread";	// Tag for logging

	private final Context context;				// Context of the current activity
	private final GameSurface gameSurface;		// Custom GameSurface object for the canvas
	private SurfaceHolder surfaceHolder;		// Surface holder for the canvas
	private Canvas canvas;						// Canvas for the UI the thread is running
	volatile Thread thread;
	private boolean run = false;				// Boolean for determining if the thread is running

	/** Game Variables **/
	private Game game;
	private IPlayer p1;
	private IPlayer p2;
	private List<Card> theStack;

	private CardDeck cd;						// CardDeck object
	private List<Card> cardDeck;				// List of cards for the card deck for each new game
	private List<Card> player1;					// List of cards for player 1
	private List<Card> player2;					// List of cards for player 2
	private List<Card> middleDeck;				// List of cards for the middle of the game table
	private Bitmap faceDownCard;				// Bitmap image of the face down card

	private Object lock = new Object();			// Create a lock that will be used for synchronization of displaying cards

	// Coordinates for the top and bottom player's cards
	private float topX;
	private float topY;
	private float bottomX;
	private float bottomY;

	/**
	 * Creates a new GameThread. All variables are initialized here, and the face down card bitmap is set here to be
	 * used throughout the thread.
	 * 
	 * @param context
	 *            - The context of the activity that is running the thread
	 * @param gameSurface
	 *            - The game surface that the UI of the thread is using
	 */
	public GameThread2(Context context, GameSurface gameSurface, Game game) {
		// Set the context, GameSurface, and SurfaceHolder
		this.context = context;
		this.gameSurface = gameSurface;
		this.surfaceHolder = gameSurface.getHolder();
		this.game = game;

		canvas = new Canvas();

		// Initialize all variables taken from Game class
		p1 = game.player1;
		p2 = game.player2;
		theStack = game.theStack;

		// Initialize a new card deck
		cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
		shuffleCards(cardDeck);
		// Initialize the player cards and the middle card deck
		player1 = new ArrayList<Card>();
		player2 = new ArrayList<Card>();
		middleDeck = new ArrayList<Card>();

		int cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
		faceDownCard = BitmapFactory.decodeResource(context.getResources(), cardId);
	}

	public void setCardDeck(List<Card> cardDeck) {
		this.cardDeck = cardDeck;
	}

	/**
	 * Used to set the card deck image in the middle of the game surface. Also sets the onTouchListener to deal cards to
	 * the top and bottom players on tap
	 * 
	 * @param canvas
	 *            - The canvas to draw the card deck on
	 */
	public void displayCardDeck() {
		int cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
		final Bitmap b = BitmapFactory.decodeResource(context.getResources(), cardId);
		final int bitX = (gameSurface.getWidth() - b.getWidth()) / 2;
		final int bitY = (gameSurface.getHeight() - b.getHeight()) / 2;
		canvas.drawBitmap(b, bitX, bitY, null);

		gameSurface.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				int x = (int) event.getX();
				int y = (int) event.getY();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (x >= bitX && x < (bitX + b.getWidth()) && y >= bitY && y < (bitY + b.getHeight())) {
						Log.i(TAG, "Card Deck Touched!!!");
						dealCards();
					}
				}
				return false;
			}

		});
	}

	/**
	 * Deal cards to the top and bottom players
	 */
	public void dealCards() {
		game.dealCards();
		// int x = 0;
		// for (Card card : cardDeck) {
		// if ((x & 1) == 0) {
		// player1.add(card);
		// } else {
		// player2.add(card);
		// }
		// x++;
		// }
	}

	/**
	 * Used to display all cards in each player's hand, and the cards in the middle of the table.
	 */
	public void displayCards() {
		Log.i(TAG, "DIIIISPLAY");
		canvas.drawColor(context.getResources().getColor(R.color.green));

		// Set the variables for each card coordinates
		topX = 10;
		topY = 10;
		bottomX = gameSurface.getWidth() - faceDownCard.getWidth() - 10;
		bottomY = gameSurface.getBottom() - 20;
		float middleX = (gameSurface.getWidth() - faceDownCard.getWidth()) / 2;
		float middleY = (gameSurface.getHeight() - faceDownCard.getHeight()) / 2;

		// Synchronize the lock here so the card deck arrays will be locked while constantly being drawn by the run loop
		synchronized (lock) {
			for (Card card : p1.getHand()) {
				canvas.drawBitmap(card.getCardBitmap(), topX, topY, null);
				topX += 5;
				topY += 0.5;
			}
			// Display the cards in player 2's hand
			for (Card card : p2.getHand()) {
				canvas.drawBitmap(card.getCardBitmap(), bottomX, bottomY - faceDownCard.getHeight(), null);
				bottomX -= 5;
				bottomY -= 0.5;
			}
			// Display the cards in the middle of the table
			if (theStack != null) {
				Iterator<Card> middleDeckIterator = middleDeck.iterator();
				// Display the first card centered
				if (middleDeckIterator.hasNext()) {
					canvas.drawBitmap(middleDeckIterator.next().getCardBitmap(), middleX, middleY, null);
					// Rotate every other card
					int degree = 20;
					while (middleDeckIterator.hasNext()) {
						Bitmap b = rotateBitmap(middleDeckIterator.next().getCardBitmap(), degree);
						canvas.drawBitmap(b, ((gameSurface.getWidth() - b.getWidth()) / 2),
								((gameSurface.getHeight() - b.getHeight()) / 2), null);
						degree += 20;
					}
				}
			}
			lock.notify();	// Notify the thread of any changes to the lock
		}
		setCardListeners();
	}

	/**
	 * This sets an onTouchListener for when the game surface is touched. If the touch is within the card bitmap of the
	 * top or bottom player's hand, then the touch event will be handled by either a custom gesture for flinging, or an
	 * when a touch is released (action up).
	 */
	public void setCardListeners() {
		// Create a new GestureDetector to handle flings on the card table
		/*
		 * final GestureDetector gestureDetector = new GestureDetector(context, new MyGestureDetector());
		 * OnTouchListener gestureListener = new View.OnTouchListener() {
		 * 
		 * @Override
		 * public boolean onTouch(View v, MotionEvent event) {
		 * return gestureDetector.onTouchEvent(event);
		 * }
		 * };
		 */
		// TODO Is this sync needed?
		// synchronized (surfaceHolder) {
		// Set the touch listener for the game surface
		gameSurface.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				/*
				 * // Touch was a fling
				 * if (gestureDetector.onTouchEvent(event)) {
				 * if (eventX >= topX && eventX < (topX + faceDownCard.getWidth()) && eventY >= topY
				 * && eventY < (topY + faceDownCard.getHeight())) {
				 * Log.i(TAG, "Swiped!!!");
				 * if (!player1.isEmpty()) {
				 * Card card = player1.get(player1.size() - 1);
				 * player1.remove(card);
				 * middleDeck.add(card);
				 * displayCards();
				 * }
				 * } else if (eventX >= bottomX && eventX < (bottomX + faceDownCard.getWidth())
				 * && eventY >= bottomY - faceDownCard.getHeight()
				 * && eventY < (bottomY - faceDownCard.getHeight() + faceDownCard.getHeight())) {
				 * Log.i(TAG, "Swiped!!!");
				 * if (!player2.isEmpty()) {
				 * Card card = player2.get(player2.size() - 1);
				 * player2.remove(card);
				 * middleDeck.add(card);
				 * displayCards();
				 * }
				 * }
				 * }
				 * // Touch was tap and release
				 * else
				 */if (action == MotionEvent.ACTION_UP) {
					if (eventX >= topX && eventX < (topX + faceDownCard.getWidth()) && eventY >= topY
							&& eventY < (topY + faceDownCard.getHeight())) {
						Log.i(TAG, "Touched!!!");

						// Unlock the lock so that the cards can be removed and added without throwing
						// ConcurrentModificationException
						synchronized (lock) {
							try {
								lock.wait();
								if (!player1.isEmpty()) {
									Card card = player1.get(player1.size() - 1);
									Log.i(TAG, "Player 1 played a card!!!");
									player1.remove(card);
									middleDeck.add(card);
								}
								lock.notify();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else if (eventX >= bottomX && eventX < (bottomX + faceDownCard.getWidth())
							&& eventY >= bottomY - faceDownCard.getHeight()
							&& eventY < (bottomY - faceDownCard.getHeight() + faceDownCard.getHeight())) {
						Log.i(TAG, "Touched!!!");

						synchronized (lock) {
							try {
								lock.wait();
								if (!player2.isEmpty()) {
									Card card = player2.get(player2.size() - 1);
									player2.remove(card);
									middleDeck.add(card);
								}
								lock.notify();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return true;
			}
		});
		// surfaceHolder.notify();
		// }
	}

	/**
	 * Method that will shuffle the cards based on the Fisher-Yates Array Shuffle
	 * 
	 * @param cardDeck
	 *            - The card deck to shuffle
	 */
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
	 * Used to rotate a bitmap image.
	 * 
	 * @param b
	 *            - The bitmap image to rotate
	 * @param degree
	 *            - The number of degrees to rotate the image
	 * @return - The newly rotated bitmap
	 */
	public Bitmap rotateBitmap(Bitmap b, float degree) {
		float scaleWidth = ((float) 100) / faceDownCard.getWidth();
		float scaleHeight = ((float) 150) / faceDownCard.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.postRotate(degree);
		b = Bitmap.createBitmap(b, 0, 0, faceDownCard.getWidth(), faceDownCard.getHeight(), matrix, true);
		return b;
	}

	/**
	 * This will set up the game surface. Sets the background color to green and displays the card deck image in the
	 * middle of the surface.
	 */
	public void initializeTable() {
		canvas = surfaceHolder.lockCanvas(null);
		canvas.drawColor(context.getResources().getColor(R.color.green));
		displayCardDeck();
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	/**
	 * Kills the thread
	 */
	public void terminateSurface() {
		this.thread.interrupt();
	}

	public void restartSurface() {
		// Start a new thread if it is terminated
		if (thread.getState() == Thread.State.TERMINATED) {
			thread = new Thread(this);
			thread.start();
		} else {
			synchronized (this) {
				this.notify();	// Notify the existing thread
			}
		}
	}

	public void setRunning(boolean run) {
		this.run = run;
		Log.d(TAG, "Starting Card Thread...");
	}

	@Override
	public void run() {
		dealCards();
		while (run) {
			try {
				canvas = surfaceHolder.lockCanvas(null);
				if (canvas == null) {
					surfaceHolder = gameSurface.getHolder();
				} else {
					synchronized (surfaceHolder) {
						displayCards();
					}
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}

	}

	/**
	 * Class for a custom Gesture Detector. Detects whether a Motion Event was a fling or not, and handles the event
	 * accordingly
	 * 
	 * @author Edward
	 * 
	 */
	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 50;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				// Not a fling
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
					return false;
				}
				// Swipe Down
				if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i(TAG, "CARD SPACE SWIPED DOWN");
					return true;
				}
				// Swipe Up
				else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i(TAG, "CARD SPACE SWIPED UP");
					return true;
				}
			} catch (Exception e) {

			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
