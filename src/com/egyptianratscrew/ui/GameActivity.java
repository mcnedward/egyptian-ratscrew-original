package com.egyptianratscrew.ui;


import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.egyptianratscrew.R;
import com.egyptianratscrew.service.Game;
import com.egyptianratscrew.service.GameSurface;

public class GameActivity extends Activity {

	private RelativeLayout table;
	private GameSurface tableCardSurface;
	private Game game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		game = new Game(true, 3, new String[] {"Player1"}, this);
		
		
		table = (RelativeLayout) findViewById(R.id.Table);
		tableCardSurface = new GameSurface(this, game);
		table.addView(tableCardSurface);
		
		
		
	}
}
