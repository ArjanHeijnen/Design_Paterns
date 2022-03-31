package com.stendenstudenten.unogame.card;

public class Card {
    private String color;
    private final int symbol;
    private final CardEffect cardEffect;

    private Card(CardBuilder builder) {
        this.color = builder.color;
        this.symbol = builder.symbol;
        this.cardEffect = builder.cardEffect;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }
    public CardEffect getCardEffect() {
        return cardEffect;
    }

    public boolean matches(Card card) {
        return this.symbol == card.symbol || this.color.equals(card.color);
    }

    public static class CardBuilder {
        private String color;
        private int symbol;
        private CardEffect cardEffect;

        public CardBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public CardBuilder setSymbol(int symbol) {
            this.symbol = symbol;
            return this;
        }

        public CardBuilder addSkipTurnEffect(String color) {
            this.color = color;
            cardEffect = new SkipTurnCardEffect();
            setSymbol(10);
            return this;
        }

        public CardBuilder addDrawCardEffect(String color) {
            this.color = color;
            cardEffect = new DrawCardsCardEffect();
            setSymbol(11);
            return this;
        }

        public CardBuilder addReverseDirectionEffect(String color) {
            this.color = color;
            cardEffect = new TurnDirectionCardEffect();
            setSymbol(12);
            return this;
        }

        public CardBuilder addPickCardColorEffect(String color) {
            this.color = color;
            cardEffect = new PickColorCardEffect();
            setSymbol(13);
            return this;
        }

        public Card build() {
            return new Card(this);
        }

    }
}
