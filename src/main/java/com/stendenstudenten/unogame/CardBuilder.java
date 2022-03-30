package com.stendenstudenten.unogame;

import com.stendenstudenten.unogame.card.Card;

import java.util.Random;

public class CardBuilder {
    public CardBuilder() {

    }

    public Card BuildCard() {
        Random rand1 = new Random();
        int cardColourNumber = rand1.nextInt((4 - 1) + 1) + 1;
        String cardColour = switch (cardColourNumber) {
            case 1 -> "red";
            case 2 -> "blue";
            case 3 -> "green";
            case 4 -> "yellow";
            default -> "error card colour";
        };
        Random rand2 = new Random();
        int cardNumber = rand2.nextInt((10 - 1) + 1) + 1;
        Card returnCard = new Card(cardColour, cardNumber);
        return returnCard;
    }
}
