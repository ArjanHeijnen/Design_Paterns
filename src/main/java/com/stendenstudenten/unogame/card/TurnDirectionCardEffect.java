package com.stendenstudenten.unogame.card;

import com.stendenstudenten.unogame.Game;

public class TurnDirectionCardEffect implements CardEffect {
     @Override
    public void execute(Game game) {
        game.reverseTurnDirection();
    }
}
