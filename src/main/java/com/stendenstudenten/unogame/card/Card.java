package com.stendenstudenten.unogame.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private final String color;
    private final int symbol;
    private final List<CardEffect> cardEffects;

    private Card(CardBuilder builder) {
        this.color = builder.color;
        this.symbol = builder.symbol;
        this.cardEffects = Collections.unmodifiableList(builder.cardEffects);
    }
    public int getSymbol() {
        return symbol;
    }
    public String getColor() {
        return color;
    }

    public boolean matches(Card card){
        return this.symbol == card.symbol || this.color.equals(card.color);
    }

    public static class CardBuilder {
        private String color;
        private int symbol;
        private List<CardEffect> cardEffects = new ArrayList<>();

        public CardBuilder setColor(String color){
            this.color = color;
            return this;
        }

        public CardBuilder setSymbol(int symbol){
            this.symbol = symbol;
            return this;
        }

        public CardBuilder addSkipTurnEffect(int numberOfTurns){
            //todo add card effect
            return this;
        }

        public CardBuilder addDrawCardEffect(int numberOfCards){
            //todo add card effect
            return this;
        }

        public CardBuilder addReverseDirectionEffect(){
            //todo add card effect
            return this;
        }

        public CardBuilder addPickCardColorEffect(){
            //todo add card effect
            return this;
        }

        public CardBuilder clearEffects(){
            cardEffects = null;
            return this;
        }

        public Card build(){
            return new Card(this);
        }

    }
}
