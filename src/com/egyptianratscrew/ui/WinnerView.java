package com.egyptianratscrew.ui;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.View;

import com.egyptianratscrew.dao.IUser;

/**
 * Setting the screen when the game is won
 * @author Julie
 *
 */
public class WinnerView extends View {

	// setting the variables
	private Movie mMovie;
	private long mMovieStart;
	private String username;


	public WinnerView(Context context, InputStream is, IUser winner) {
		// if winnerview is called then play the animated cards
		super(context);
		setFocusable(true);
		if (winner == null)
			username = "Player";
		else
			username = winner.getUserName();
		
		//is = context.getResources().openRawResource(R.drawable.playing_cards_animation);
		//mMovie = Movie.decodeStream(is);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		BackupDraw(canvas);
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
		paint.setColor(Color.RED);
		canvas.drawText("Congratulations, " + username, canvas.getWidth()/2,canvas.getHeight()/2, paint);
		canvas.drawText("Winner!", canvas.getWidth()/2 ,(canvas.getHeight()/2)+ paint.ascent(), paint);
	}

}
