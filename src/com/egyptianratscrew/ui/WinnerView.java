package com.egyptianratscrew.ui;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.egyptianratscrew.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.view.View;

public class WinnerView extends View {

	private Movie mMovie;
    private long mMovieStart;
    
    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }
    
    public WinnerView(Context context) {
        super(context);
        setFocusable(true);
        
        java.io.InputStream is;
        
        is = context.getResources().openRawResource(R.drawable.playing_cards_animation);
        if (true) {
            mMovie = Movie.decodeStream(is);
        } else {
            byte[] array = streamToBytes(is);
            mMovie = Movie.decodeByteArray(array, 0, array.length);
        }
    }
    
    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x00000000);            
        
        Paint p = new Paint();
        p.setAntiAlias(true);
        
        
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {   // first time
            mMovieStart = now;
        }
        if (mMovie != null) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int relTime = (int)((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(),
                        getHeight() - mMovie.height());
            invalidate();
        }
    }

}
