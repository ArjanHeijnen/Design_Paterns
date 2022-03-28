package com.stendenstudenten.unogame;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Game game = new Game();
        game.addPlayer("player1");
        game.addPlayer("player2");
        game.addPlayer("player3");
        game.addPlayer("player4");
        game.startGame();
    }
}
