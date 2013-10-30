package com.egyptianratscrew.ui;

import android.app.Activity;
import android.os.Bundle;

import com.egyptianratscrew.service.Game;
import com.egyptianratscrew.service.GameSurface;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Game game = new Game(true, 1, new String[] { "Ed", "Computer" }, this);
		GameSurface gameSurface = new GameSurface(this, game);
		setContentView(gameSurface);
	}
}
