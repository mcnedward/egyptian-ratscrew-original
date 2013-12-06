package com.egyptianratscrew.ui;

import java.io.InputStream;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.View;

import com.egyptianratscrew.dto.IPlayer;

/**
 * Setting the screen when the game is won
 * @author Julie
 *
 */
public class WinnerView extends View {

	private static final String WINNER = "Winner!";
	private static final String LOSER = "Loser!";
	private static final String CONGRATULATIONS = "Congratulations, ";
	private static final String SORRY = "So sorry, ";
	private static final int DELAY_INTERVAL = 2000;
	// setting the variables
	private Movie mMovie;
	private long mMovieStart;
	private String username;
	private boolean Blink;
	private Canvas canvas;
	private Boolean won;


	public WinnerView(Context context, InputStream is, IPlayer player1) {
		// if winnerview is called then play the animated cards
		super(context);
		setFocusable(true);
		Blink = true;
		username = player1.getUser().getUserName();
		won = player1.isWinner();
//		Timer BlinkTimer = new Timer();
//		BlinkTimer.schedule(new BlinkTask(), DELAY_INTERVAL,DELAY_INTERVAL);
		
		//is = context.getResources().openRawResource(R.drawable.playing_cards_animation);
		//mMovie = Movie.decodeStream(is);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		BackupDraw(this.canvas);
		return;
		
//		canvas.drawColor(Color.TRANSPARENT);
//
//		Paint p = new Paint();
//		p.setAntiAlias(true);
//
//		long now = android.os.SystemClock.uptimeMillis();
//		if (mMovieStart == 0) {   // first time
//			mMovieStart = now;
//		}
//		if (mMovie != null) {
//			int dur = mMovie.duration();
//			if (dur == 0) {
//				dur = 1000;
//			}
//			int relTime = (int) ((now - mMovieStart) % dur);
//			mMovie.setTime(relTime);
//			mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
//			invalidate();
//		}
	}

	protected void BackupDraw(Canvas canvas){
		canvas.drawColor(Color.GREEN);
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("Game Over", canvas.getWidth()/2,10 -paint.ascent(), paint);
		paint.setTextSize(50);
		if (Blink)
			paint.setColor(Color.RED);
		else
			paint.setColor(Color.MAGENTA);
		
		String message;
		String message2;
		if (won){
			message = CONGRATULATIONS;
			message2 = WINNER;
		}
		else{
			message = SORRY;
			message2 = LOSER;
		}
		
		
		canvas.drawText(message + username, canvas.getWidth()/2,canvas.getHeight()/2, paint);
		canvas.drawText(message2, canvas.getWidth()/2 ,(canvas.getHeight()/2)+ paint.ascent(), paint);
	}

	class BlinkTask extends TimerTask {

		public BlinkTask() {
		}

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			if (Blink)
				Blink = false;
			else
				Blink = true;
			if (canvas != null)
				onDraw(canvas);
		}
	}
}
