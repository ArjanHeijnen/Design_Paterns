package com.stendenstudenten.unogame.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private final String color;
    private final int symbol;
    private final List<CardEffect> cardEffects;
    private final boolean alwaysMatches;

    private Card(CardBuilder builder) {
        this.color = builder.color;
        this.symbol = builder.symbol;
        this.cardEffects = Collections.unmodifiableList(builder.cardEffects);
        this.alwaysMatches = builder.alwaysMatches;
    }

    public int getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }
    public List<CardEffect> getCardEffects() {
        return cardEffects;
    }

    public boolean matches(Card card) {
        return this.symbol == card.symbol || this.color.equals(card.color) || alwaysMatches || card.alwaysMatches;
    }

    public boolean alwaysMatches(){
        return this.alwaysMatches;
    }

    public static class CardBuilder {
        private String color;
        private int symbol;
        private List<CardEffect> cardEffects = new ArrayList<>();
        private boolean alwaysMatches = false;

        public CardBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public CardBuilder setSymbol(int symbol) {
            this.symbol = symbol;
            return this;
        }

        public CardBuilder setAlwaysMatches(boolean alwaysMatches){
            this.alwaysMatches = alwaysMatches;
            return this;
        }

        public CardBuilder addSkipTurnEffect() {
            cardEffects.add(new SkipTurnCardEffect());
            return this;
        }

        public CardBuilder addDrawCardEffect() {
            cardEffects.add(new DrawCardCardEffect());
            return this;
        }

        public CardBuilder addReverseDirectionEffect() {
            cardEffects.add(new TurnDirectionCardEffect());
            return this;
        }

        public CardBuilder addPickCardColorEffect() {
            cardEffects.add(new PickColorCardEffect());
            return this;
        }

        public CardBuilder clearEffects(){
            cardEffects = new ArrayList<>();
            return this;
        }

        public Card build() {
            Card card = new Card(this);
            cardEffects = new ArrayList<>(cardEffects);
            return card;
        }

    }
}
