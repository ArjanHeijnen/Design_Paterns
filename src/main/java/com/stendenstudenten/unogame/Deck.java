package com.stendenstudenten.unogame;

import com.stendenstudenten.unogame.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    public Deck(){
        Card.CardBuilder builder = new Card.CardBuilder();
        for(int c = 0; c < 4; c++){
            String color;
            switch (c){
                case 0 -> color = "#d72600";
                case 1 -> color = "#0956bf";
                case 2 -> color = "#379711";
                case 3 -> color = "#ecd407";
                default -> throw new RuntimeException();
            }
            builder.setColor(color);
            for(int n = 1; n <= 9; n++){
                builder.setSymbol(n);
                cards.add(builder.build());
            }
        }
    }

    public Card getRandom(){
        if(cards.size() > 0){
            int i =  random.nextInt(cards.size());
            Card pickedCard = cards.get(i);
            cards.remove(i);
            return pickedCard;
        }else{
            return null;
        }
    }

    public int getCardsLeft(){
        return cards.size();
    }

    public void placeCard(Card card){
        cards.add(card);
    }

    public Card getTopMost(){
        return cards.get(cards.size() - 1);
    }

    public void clear(){
        cards.clear();
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
