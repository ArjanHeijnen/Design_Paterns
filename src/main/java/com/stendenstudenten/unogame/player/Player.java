package com.stendenstudenten.unogame.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<String> cardsInHand = new ArrayList<String>();
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

    public List<String> getCardsInHand() {
        return cardsInHand;
    }

    public String playCard(int cardNum) {
        String card = cardsInHand.get(cardNum);
        cardsInHand.remove(cardNum);
        return card;
    }

    public void addCardToHand(String card) {
        cardsInHand.add(card);
    }
}
