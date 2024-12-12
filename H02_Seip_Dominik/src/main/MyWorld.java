package main;

import fopbot.World;

public class MyWorld extends World {
	private static TetrisGame GAME_INSTANCE;
	
	public static TetrisGame getTetrisGame() {
		return GAME_INSTANCE;
	}
	
	public static void setTetrisGame(TetrisGame _tetrisGame) {
		GAME_INSTANCE = _tetrisGame;
	}
	
}

