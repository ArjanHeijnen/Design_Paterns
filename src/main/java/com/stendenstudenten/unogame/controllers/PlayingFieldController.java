package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.Game;
import com.stendenstudenten.unogame.card.Card;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;

public class PlayingFieldController {
    Game game;

    @FXML
    public void initialize(){
        System.out.println("test");
        game = new Game();
        game.addPlayer("player");
        game.addPlayer("CPU1");
        game.addPlayer("CPU2");
        game.addPlayer("CPU3");
        try {
            game.startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
