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

/**
 * Setting the screen when the game is won
 * @author Julie
 *
 */
public class WinnerView extends View {

	// setting the variables
	private Movie mMovie;
	private long mMovieStart;


	public WinnerView(Context context, InputStream is) {
		// if winnerview is called then play the animated cards
		super(context);
		setFocusable(true);

		//is = context.getResources().openRawResource(R.drawable.playing_cards_animation);
		mMovie = Movie.decodeStream(is);
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
		paint.setColor(Color.RED);
		paint.setTextSize(50);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("Winner!", 10,10, paint);
	}

}
