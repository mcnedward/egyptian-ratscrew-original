package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

import com.egyptianratscrew.R;
import com.egyptianratscrew.dto.Card;
import com.egyptianratscrew.dto.CardDeck;

/**
 * A thread class that is used to run the Egyptian Ratscrew game.
 * 
 * @author Edward
 * 
 */
public class GameThread extends AsyncTask<Void, Integer, Void> {
	private static String TAG = "GameThread";	// Tag for logging

	private final Context context;				// Context of the current activity
	private final GameSurface gameSurface;		// Custom GameSurface object for the canvas
	private final SurfaceHolder surfaceHolder;	// Surface holder for the canvas
	private Canvas canvas;						// Canvas for the UI the thread is running
	private boolean run = false;				// Boolean for determining if the thread is running
	private CardDeck cd;						// CardDeck object
	private List<Card> cardDeck;				// List of cards for the card deck for each new game
	private List<Card> player1;					// List of cards for player 1
	private List<Card> player2;					// List of cards for player 2
	private List<Card> middleDeck;				// List of cards for the middle of the game table
	private Bitmap faceDownCard;				// Bitmap image of the face down card

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
	public GameThread(Context context, GameSurface gameSurface) {
		this.context = context;
		this.gameSurface = gameSurface;
		this.surfaceHolder = gameSurface.getHolder();
		cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
		shuffleCards(cardDeck);
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
	public void displayCardDeck(Canvas canvas) {
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
						publishProgress();
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
		int x = 0;
		for (Card card : cardDeck) {
			if ((x & 1) == 0) {
				player1.add(card);
			} else {
				player2.add(card);
			}
			x++;
		}

		displayCards();
	}

	/**
	 * Used to display all cards in each player's hand, and the cards in the middle of the table.
	 */
	public void displayCards() {
		// Lock the canvas for drawing on
		canvas = surfaceHolder.lockCanvas();		// The canvas for the game surface
		canvas.drawColor(context.getResources().getColor(R.color.green));

		// Set the variables for each card coordinates
		topX = 10;
		topY = 10;
		bottomX = 10;
		bottomY = gameSurface.getBottom() - 20;
		float middleX = (gameSurface.getWidth() - faceDownCard.getWidth()) / 2;
		float middleY = (gameSurface.getHeight() - faceDownCard.getHeight()) / 2;

		// Display the cards in player 1's hand
		for (Card card : player1) {
			canvas.drawBitmap(card.getCardBitmap(), topX, topY, null);
			topY += 0.5;
		}
		// Display the cards in player 2's hand
		for (Card card : player2) {
			canvas.drawBitmap(card.getCardBitmap(), bottomX, bottomY - faceDownCard.getHeight(), null);
			bottomX += 5;
			bottomY -= 0.5;
		}

		// Display the cards in the middle of the table
		int x = 0;
		if (middleDeck != null) {
			for (Card card : middleDeck) {
				// Rotate every other card
				if ((x & 1) == 0) {
					canvas.drawBitmap(card.getCardBitmap(), middleX, middleY, null);
				} else {
					Bitmap b = rotateBitmap(card.getCardBitmap(), 90);
					canvas.drawBitmap(b, ((gameSurface.getWidth() - b.getWidth()) / 2),
							((gameSurface.getHeight() - b.getHeight()) / 2), null);
				}
				x++;
			}
		}

		surfaceHolder.unlockCanvasAndPost(canvas);	// Unlock and post the canvas
		setCardListeners();							// Reset the card listeners to the new coordinates
	}

	/**
	 * This sets an onTouchListener for when the game surface is touched. If the touch is within the card bitmap of the
	 * top or bottom player's hand, then the touch event will be handled by either a custom gesture for flinging, or an
	 * when a touch is released (action up).
	 */
	public void setCardListeners() {
		// Create a new GestureDetector to handle flings on the card table
		final GestureDetector gestureDetector = new GestureDetector(context, new MyGestureDetector());
		OnTouchListener gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		// Set the touch listener for the game surface
		gameSurface.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);	// Get the action for the motion
				int eventX = (int) event.getX();	// Get the x-coordinates for the action
				int eventY = (int) event.getY();	// Get the y-coordinates for the action

				// Touch was a fling
				if (gestureDetector.onTouchEvent(event)) {
					if (eventX >= topX && eventX < (topX + faceDownCard.getWidth()) && eventY >= topY
							&& eventY < (topY + faceDownCard.getHeight())) {
						Log.i(TAG, "Swiped!!!");
						if (!player1.isEmpty()) {
							Card card = player1.get(player1.size() - 1);
							player1.remove(card);
							middleDeck.add(card);
							displayCards();
						}
					} else if (eventX >= bottomX && eventX < (bottomX + faceDownCard.getWidth())
							&& eventY >= bottomY - faceDownCard.getHeight()
							&& eventY < (bottomY - faceDownCard.getHeight() + faceDownCard.getHeight())) {
						Log.i(TAG, "Swiped!!!");
						if (!player2.isEmpty()) {
							Card card = player2.get(player2.size() - 1);
							player2.remove(card);
							middleDeck.add(card);
							displayCards();
						}
					}
				}
				// Touch was tap and release
				else if (action == MotionEvent.ACTION_UP) {
					if (eventX >= topX && eventX < (topX + faceDownCard.getWidth()) && eventY >= topY
							&& eventY < (topY + faceDownCard.getHeight())) {
						Log.i(TAG, "Touched!!!");
						if (!player1.isEmpty()) {
							Card card = player1.get(player1.size() - 1);
							player1.remove(card);
							middleDeck.add(card);
							displayCards();
						}
					} else if (eventX >= bottomX && eventX < (bottomX + faceDownCard.getWidth())
							&& eventY >= bottomY - faceDownCard.getHeight()
							&& eventY < (bottomY - faceDownCard.getHeight() + faceDownCard.getHeight())) {
						Log.i(TAG, "Touched!!!");
						if (!player2.isEmpty()) {
							Card card = player2.get(player2.size() - 1);
							player2.remove(card);
							middleDeck.add(card);
							displayCards();
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * This will set up the game surface. Sets the background color to green and displays the card deck image in the
	 * middle of the surface.
	 */
	public void initializeTable() {
		canvas.drawColor(context.getResources().getColor(R.color.green));
		displayCardDeck(canvas);
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
		matrix.postRotate(90);
		b = Bitmap.createBitmap(b, 0, 0, faceDownCard.getWidth(), faceDownCard.getHeight(), matrix, true);
		return b;
	}

	public void setRunning(boolean run) {
		this.run = run;
		Log.d(TAG, "Starting Card Thread...");
	}

	@Override
	protected void onPreExecute() {

	}

	/**
	 * Create a new canvas and set up the card table for the first time
	 */
	@Override
	protected Void doInBackground(Void... params) {
		canvas = new Canvas();
		canvas = surfaceHolder.lockCanvas();
		initializeTable();
		displayCardDeck(canvas);
		surfaceHolder.unlockCanvasAndPost(canvas);
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		dealCards();
	}

	protected void onPostExecute() {
		Log.i(TAG, "Canvas Drawn");
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
