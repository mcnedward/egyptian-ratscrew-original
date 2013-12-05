package com.egyptianratscrew.service;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is a custom surface view that is used as the card table. This view implements Runnable to constantly redraw the
 * canvas and everything that needs to be displayed on the canvas.
 * 
 * @author Edward McNealy
 * 
 */
public class GameSurface extends SurfaceView implements Runnable {
	private static final String TAG = "GameSurface";

	private final Context context;
	private SurfaceHolder holder;
	private Thread thread;
	private boolean GAME_RUNNING = false;
	private Canvas canvas = null;

	private Game2 game;

	public GameSurface(Context context, Game2 game) {
		super(context);
		this.context = context;
		this.game = game;
		holder = getHolder();
		setFocusable(true);
	}

	@Override
	public void run() {
		while (GAME_RUNNING) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			try {
				canvas = holder.lockCanvas(null);
				synchronized (this) {
					game.setCardListeners(this);
					game.drawGame(canvas);	// Called constantly while the game is running
				}
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	public void pause() {
		GAME_RUNNING = false;
		game.setStarted(false);
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void resume() {
		GAME_RUNNING = true;
		thread = new Thread(this);
		thread.start();
	}

	// TODO All this code is probably not needed anymore...
	// @Override
	// public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	//
	// }
	//
	// @Override
	// public void surfaceCreated(SurfaceHolder holder) {
	// // new GameThread(context, this, game).execute();
	// }
	//
	// @Override
	// public void surfaceDestroyed(SurfaceHolder holder) {
	//
	// }
	//
	// /**
	// * Used to rotate a bitmap image.
	// *
	// * @param b
	// * - The bitmap image to rotate.
	// * @param degree
	// * - The number of degrees to rotate the image.
	// * @return - The newly rotated bitmap.
	// */
	// public Bitmap rotateBitmap(Bitmap b, float degree) {
	// float scaleWidth = ((float) 100) / faceDownCard.getWidth();
	// float scaleHeight = ((float) 150) / faceDownCard.getHeight();
	// Matrix matrix = new Matrix();
	// matrix.postScale(scaleWidth, scaleHeight);
	// matrix.postRotate(degree);
	// b = Bitmap.createBitmap(b, 0, 0, faceDownCard.getWidth(), faceDownCard.getHeight(), matrix, true);
	// return b;
	// }
	// /**
	// * Used to display all cards in each player's hand, and the cards in the middle of the table.
	// */
	// public void displayCards(Canvas canvas) {
	// canvas.drawColor(context.getResources().getColor(R.color.green));
	// if (!GAME_STARTED) {
	// int cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
	// final Bitmap b = BitmapFactory.decodeResource(context.getResources(), cardId);
	// final int bitX = (this.getWidth() - b.getWidth()) / 2;
	// final int bitY = (this.getHeight() - b.getHeight()) / 2;
	// canvas.drawBitmap(b, bitX, bitY, null);
	//
	// this.setOnTouchListener(new OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// int action = event.getAction();
	// int x = (int) event.getX();
	// int y = (int) event.getY();
	//
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	// if (x >= bitX && x < (bitX + b.getWidth()) && y >= bitY && y < (bitY + b.getHeight())) {
	// Log.i(TAG, "Card Deck Touched!!!");
	// GAME_STARTED = true;
	// game.dealCards();
	// }
	// }
	// return false;
	// }
	//
	// });
	// } else {
	// // Set the variables for each card coordinates
	// topX = 10;
	// topY = 10;
	// bottomX = this.getWidth() - faceDownCard.getWidth() - 10;
	// bottomY = this.getBottom() - 20;
	// float middleX = (this.getWidth() - faceDownCard.getWidth()) / 2;
	// float middleY = (this.getHeight() - faceDownCard.getHeight()) / 2;
	//
	// Iterator<Card> player2Iterator = game.player2.getHand().iterator();
	// while (player2Iterator.hasNext()) {
	// // Card card = game.player2.getPlayerHand().get(player2Iterator.next());
	// canvas.drawBitmap(player2Iterator.next().getCardBitmap(), topX, topY, null);
	// topX += 14;
	// topY += 0.5;
	// }
	// // Display the cards in player 2's hand
	// Iterator<Card> player1Iterator = game.player1.getHand().iterator();
	// while (player1Iterator.hasNext()) {
	// // int x = player1Iterator.next();
	// // Card card = game.player1.getPlayerHand().get(x);
	// canvas.drawBitmap(player1Iterator.next().getCardBitmap(), bottomX, bottomY - faceDownCard.getHeight(),
	// null);
	// bottomX -= 14;
	// bottomY -= 0.5;
	// }
	// // Display the cards in the middle of the table
	// if (game.theStack != null) {
	// Iterator<Card> middleDeckIterator = game.theStack.iterator();
	// // Display the first card centered
	// if (middleDeckIterator.hasNext()) {
	// Card card = middleDeckIterator.next();
	// canvas.drawBitmap(card.getCardBitmap(), middleX, middleY, null);
	// // Rotate every other card
	// int degree = 20;
	// while (middleDeckIterator.hasNext()) {
	// Card nextCard = middleDeckIterator.next();
	// Bitmap b = rotateBitmap(nextCard.getCardBitmap(), degree);
	// canvas.drawBitmap(b, ((this.getWidth() - b.getWidth()) / 2),
	// ((this.getHeight() - b.getHeight()) / 2), null);
	// degree += 20;
	// }
	// }
	// }
	// setCardListeners(); // Reset the card listeners to the new coordinates
	// }
	// }
	//
	// /**
	// * This sets an onTouchListener for when the game surface is touched. If the touch is within the card bitmap of
	// the
	// * top or bottom player's hand, then the touch event will be handled by either a custom gesture for flinging, or
	// an
	// * when a touch is released (action up).
	// */
	// public void setCardListeners() {
	// // Set the touch listener for the game surface
	// this.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// final int action = MotionEventCompat.getActionMasked(event); // Get the action for the motion
	// int eventX = (int) event.getX(); // Get the x-coordinates for the action
	// int eventY = (int) event.getY(); // Get the y-coordinates for the action
	//
	// // if (eventX >= topX && eventX < (topX + faceDownCard.getWidth()) && eventY >= topY
	// // && eventY < (topY + faceDownCard.getHeight()))
	// if (action == MotionEvent.ACTION_UP) {
	// if (eventX >= bottomX && eventX < (bottomX + faceDownCard.getWidth())
	// && eventY >= bottomY - faceDownCard.getHeight()
	// && eventY < (bottomY - faceDownCard.getHeight() + faceDownCard.getHeight())) {
	// Log.i(TAG, "Touched!!!");
	//
	// if (!game.player1.getHand().isEmpty()) {
	// if (game.playCard(game.player1.getID())) {
	// }
	// }
	// }
	// }
	// return true;
	// }
	// });
	// }
}
