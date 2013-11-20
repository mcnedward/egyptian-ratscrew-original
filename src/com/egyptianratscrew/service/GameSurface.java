package com.egyptianratscrew.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.egyptianratscrew.R;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameSurface";

	private final Context context;
	private GameThread2 gameThread2 = null;
	private Game game;

	public GameSurface(Context context, Game game) {
		super(context);
		this.context = context;
		this.game = game;

		getHolder().addCallback(this);
		setFocusable(true);
		gameThread2 = new GameThread2(context, this, game);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// new GameThread(context, this, game).execute();
		Canvas canvas = null;
		try {
			canvas = holder.lockCanvas(null);
			synchronized (this) {
				canvas.drawColor(context.getResources().getColor(R.color.green));
				int cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
				final Bitmap b = BitmapFactory.decodeResource(context.getResources(), cardId);
				final int bitX = (this.getWidth() - b.getWidth()) / 2;
				final int bitY = (this.getHeight() - b.getHeight()) / 2;
				canvas.drawBitmap(b, bitX, bitY, null);

				this.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int action = event.getAction();
						int x = (int) event.getX();
						int y = (int) event.getY();

						switch (action) {
						case MotionEvent.ACTION_DOWN:
							if (x >= bitX && x < (bitX + b.getWidth()) && y >= bitY && y < (bitY + b.getHeight())) {
								Log.i(TAG, "Card Deck Touched!!!");
								gameThread2.setRunning(true);
								gameThread2.start();
							}
						}
						return false;
					}

				});
			}
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
