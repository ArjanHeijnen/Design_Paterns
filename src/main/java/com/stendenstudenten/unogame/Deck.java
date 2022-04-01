package com.stendenstudenten.unogame;

import com.stendenstudenten.unogame.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    //special card symbols: 10 wild, 11 wild +4, 12 skip, 13 reverse, 14 +2
    //1 zero card per color
    //2 of every other number per color
    //2 of skip, reverse and +2 cards per color
    //4 wild cards
    //4 wild +4 cards
    public Deck() {
        Card.CardBuilder builder = new Card.CardBuilder();
        for (int c = 0; c < 4; c++) {
            String color;
            switch (c) {
                case 0 -> color = "#d72600";
                case 1 -> color = "#0956bf";
                case 2 -> color = "#379711";
                case 3 -> color = "#ecd407";
                default -> throw new RuntimeException();
            }

            builder.setColor(color).clearEffects();
            for (int n = 0; n <= 9; n++) {
                builder.setSymbol(n);
                cards.add(builder.build());
                if(n > 0){
                    //add second card
                    cards.add(builder.build());
                }
            }
            //add colored action cards
            builder.setSymbol(12).addSkipTurnEffect();
            cards.add(builder.build());
            cards.add(builder.build());
            builder.clearEffects().setSymbol(13).addReverseDirectionEffect();
            cards.add(builder.build());
            cards.add(builder.build());
            //todo make this draw 2 cards somehow
            builder.clearEffects().setSymbol(14).addDrawCardEffect();
            cards.add(builder.build());
            cards.add(builder.build());

        }
        //add wild cards
        builder.setAlwaysMatches(true);
        builder.clearEffects().setSymbol(10).setColor("#000000").addPickCardColorEffect();
        cards.add(builder.build());
        cards.add(builder.build());
        cards.add(builder.build());
        cards.add(builder.build());
        //todo make this draw 4 cards somehow
        builder.setSymbol(11).addDrawCardEffect();
        cards.add(builder.build());
        cards.add(builder.build());
        cards.add(builder.build());
        cards.add(builder.build());
    }

    public Card getRandom() {
        if (cards.size() > 0) {
            int i = random.nextInt(cards.size());
            Card pickedCard = cards.get(i);
            cards.remove(i);
            return pickedCard;
        } else {
            return null;
        }
    }

    public int getCardsLeft() {
        return cards.size();
    }

    public void placeCard(Card card) {
        cards.add(card);
    }

    public Card getTopMost() {
        return cards.get(cards.size() - 1);
    }

    public void clear() {
        cards.clear();
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
