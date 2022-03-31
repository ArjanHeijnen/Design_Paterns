package com.stendenstudenten.unogame.card;

import com.stendenstudenten.unogame.Game;

public class SkipTurnCardEffect implements CardEffect {

    @Override
    public void execute(Game game) {
        System.out.println("Executing skip turn effect!");
        game.nextTurn();
    }
}
