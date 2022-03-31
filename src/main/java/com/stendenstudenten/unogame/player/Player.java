package com.stendenstudenten.unogame.player;

import com.stendenstudenten.unogame.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> cardsInHand = new ArrayList<>();
    private String playerName;

    public Player(String playerName) {
        setPlayerName(playerName);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }


    public void addCardToHand(Card card) {
        cardsInHand.add(card);
    }
}
