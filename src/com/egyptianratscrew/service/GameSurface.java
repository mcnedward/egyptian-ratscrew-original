package com.egyptianratscrew.service;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameSurface";

	private final Context context;

	public GameSurface(Context context) {
		super(context);
		this.context = context;
		getHolder().addCallback(this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new GameThread(context, this).execute();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
