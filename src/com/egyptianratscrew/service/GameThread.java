package com.egyptianratscrew.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

public class GameThread extends AsyncTask<Void, Integer, Void> {
	private static String TAG = "GameThread";

	private final Context context;
	private final GameSurface gameSurface;
	private final SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private boolean run = false;
	private List<Card> cardDeck = new ArrayList<Card>();

	public GameThread(Context context, GameSurface gameSurface) {
		this.context = context;
		this.gameSurface = gameSurface;
		this.surfaceHolder = gameSurface.getHolder();
		CardDeck cd = new CardDeck(context);
		cardDeck = cd.cardDeck;
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
		int surfaceWidth = gameSurface.getWidth();	// The width of the game surface (card table)
		float bitX = 10;	// The x-coordinate for the top set of cards
		float bitY = 10;	// The y-coordinate for the top set of cards
		float bitY2 = gameSurface.getBottom() - 20;	// The y-coordinate for the bottom set of cards

		// Lock the canvas for drawing on
		canvas = surfaceHolder.lockCanvas();		// The canvas for the game surface
		canvas.drawColor(context.getResources().getColor(R.color.green));
		int x = 1;

		for (Card card : cardDeck) {
			int cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
			final Bitmap cardBitmap = BitmapFactory.decodeResource(context.getResources(), cardId);
			// Draw the top card
			canvas.drawBitmap(cardBitmap, bitX, bitY, null);
			// Draw the bottom card
			canvas.drawBitmap(cardBitmap, bitX, bitY2 - cardBitmap.getHeight(), null);
			playCard(card, (int) bitX, (int) bitY);

			bitX += 5;	// Increase the x-coordinate for the cards
			bitY += 0.5;
			bitY2 -= 0.5;
			// Check if the card is going past the edge of the game surface; Reset the coordinates if the cards if true
			if ((card.getCardBitmap().getWidth() + bitX) >= surfaceWidth) {
				bitX = 10;
				bitY += 40;
				bitY2 -= 40;
			}
			x++;
		}
		// Unlock and post the canvas
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	public void playCard(Card card, final int bitX, final int bitY) {
		final Bitmap b = card.getCardBitmap();
		// Create a new GestureDetector to handle flings on the card table
		final GestureDetector gestureDetector = new GestureDetector(context, new MyGestureDetector());
		OnTouchListener gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		gameSurface.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = MotionEventCompat.getActionMasked(event);
				int eventX = (int) event.getX();
				int eventY = (int) event.getY();

				if (gestureDetector.onTouchEvent(event)) {
					Log.i(TAG, "Swiped!!!");
				} else if (action == MotionEvent.ACTION_UP) {
					if (eventX >= bitX && eventX < (bitX + b.getWidth()) && eventY >= bitY
							&& eventY < (bitY + b.getHeight())) {
						Log.i(TAG, "Touched!!!");
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

	public void setRunning(boolean run) {
		this.run = run;
		Log.d(TAG, "Starting Card Thread...");
	}

	@Override
	protected void onPreExecute() {

	}

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

	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				// Not a fling
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
					return false;
				}
				if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i(TAG, "CARD SPACE SWIPED DOWN");
					return true;
				} else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i(TAG, "CARD SPACE SWIPED UP");
					return true;
				}
			} catch (Exception e) {

			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
