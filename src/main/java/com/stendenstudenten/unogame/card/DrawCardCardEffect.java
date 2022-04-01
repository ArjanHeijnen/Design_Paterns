package com.stendenstudenten.unogame.card;

import com.stendenstudenten.unogame.Game;

public class DrawCardCardEffect implements CardEffect {

    @Override
    public void execute(Game game) {
        System.out.println("Executing draw card effect!");
        game.addForceDraw();
    }
}
