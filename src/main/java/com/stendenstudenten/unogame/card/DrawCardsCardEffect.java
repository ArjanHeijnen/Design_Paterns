package com.stendenstudenten.unogame.card;

import com.stendenstudenten.unogame.Game;

public class DrawCardsCardEffect implements CardEffect {

    @Override
    public void execute(Game game) {
        game.draw2CardsEffect();
    }
}
