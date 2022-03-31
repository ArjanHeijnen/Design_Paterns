package com.stendenstudenten.unogame.card;

import com.stendenstudenten.unogame.Game;

public class PickColorCardEffect implements CardEffect {

    @Override
    public void execute(Game game) {
        System.out.println("Executing pick color effect!");
        game.changeLastPlayedCardColor();
    }
}
