package com.stendenstudenten.unogame.player;

import com.stendenstudenten.unogame.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> cardsInHand = new ArrayList<>();
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
    public String getCardsInHandString() {
        StringBuilder cardsAsString = new StringBuilder();
        cardsAsString.append("[");
        for (Card card : cardsInHand) {
            cardsAsString.append(card.getColour());
            cardsAsString.append(card.getNumber());
            cardsAsString.append(",");
        }
        cardsAsString.append("]");
        return cardsAsString.toString();
    }
    public Card playCard(int cardNum) {
        return cardsInHand.get(cardNum);
    }
    public void removeCardFromHand(int cardNum) {
        cardsInHand.remove(cardNum);
    }

    public void addCardToHand(Card card) {
        cardsInHand.add(card);
    }
}
