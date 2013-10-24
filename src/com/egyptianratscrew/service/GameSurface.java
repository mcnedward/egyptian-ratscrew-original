package com.egyptianratscrew.service;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameSurface";

	private final Context context;
	private Game game;

	public GameSurface(Context context, Game game) {
		super(context);
		this.context = context;
		this.game = game;
		
		getHolder().addCallback(this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new GameThread(context, this, game).execute();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
