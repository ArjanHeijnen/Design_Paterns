package com.stendenstudenten.unogame;


import com.stendenstudenten.unogame.card.Card;
import com.stendenstudenten.unogame.card.CardEffect;
import com.stendenstudenten.unogame.controllers.GameViewController;
import com.stendenstudenten.unogame.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    GameViewController gameViewController;
    private int activePlayerIndex = 0;
    private final List<Player> players = new ArrayList<Player>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;
    private final Deck discardDeck;
    private final Deck drawDeck;
    private final Random random = new Random();
    private boolean playedCardThisTurn = false;

    public Game(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
        drawDeck = new Deck();
        discardDeck = new Deck();
        setLastPlayedCard(drawDeck.getRandom());
    }

    public void startGame() throws IOException {
        shareStartCards(7);
        gameViewController.setStatusText("It's your turn!");
    }

    private void doCPUMoves() {
        if (activePlayerIndex != 0) {
            Player player = players.get(activePlayerIndex);
            int cardsDrawn = 0;
            List<Card> hand = player.getCardsInHand();
            Card cardToPlay = null;
            for (Card card : hand) {
                if (card.matches(discardDeck.getTopMost())) {
                    cardToPlay = card;
                    break;
                }
            }

            while (cardToPlay == null) {
                gameViewController.setNumberOfCPUCards(hand.size(), activePlayerIndex);
                Card drawnCard = drawCard(activePlayerIndex);
                cardsDrawn++;
                if (drawnCard == null) {
                    playedCardThisTurn = true;
                    return;
                }
                if (drawnCard.matches(discardDeck.getTopMost())) {
                    cardToPlay = drawnCard;
                }
            }

            setLastPlayedCard(cardToPlay);
            hand.remove(cardToPlay);
            if(cardsDrawn > 0){
                gameViewController.setStatusText(player.getPlayerName() + " played a card after drawing " + cardsDrawn + " cards.");
            }else{
                if(hand.size() == 1){
                    gameViewController.setStatusText("UNO! " + player.getPlayerName() + " has one card left!");
                }else{
                    gameViewController.setStatusText(player.getPlayerName() + " played a card.");
                }
            }

            gameViewController.setNumberOfCPUCards(hand.size(), activePlayerIndex);
            gameViewController.setDiscardPileCard(discardDeck.getTopMost());
            executeCardEffects(cardToPlay);
            if (hand.size() <= 0) {
                selectWinner(player.getPlayerName());
            } else {
                playedCardThisTurn = true;
            }
        }
    }

    public void tryPlayCard(int cardIndex) {
        if(activePlayerIndex != 0){
            gameViewController.setStatusText("It's not your turn yet!");
            return;
        }else if(playedCardThisTurn){
            gameViewController.setStatusText("You already played a card this turn!");
            return;
        }
            List<Card> playerHand = players.get(0).getCardsInHand();
            Card playedCard = playerHand.get(cardIndex);

            if (playedCard.matches(discardDeck.getTopMost())) {
                setLastPlayedCard(playedCard);
                playerHand.remove(cardIndex);
                executeCardEffects(playedCard);
                gameViewController.setPlayerCardViews(playerHand);

                if (playerHand.size() <= 0) {
                    selectWinner(players.get(0).getPlayerName());
                }
                    if(playerHand.size() == 1) {
                        gameViewController.setStatusText("UNO! You have one card left!");
                    }else {
                        gameViewController.setStatusText("You play a card.");
                    }
                    playedCardThisTurn = true;
            }else{
                gameViewController.setStatusText("You can't play that card.");
            }
    }

    private void executeCardEffects(Card card){
        for (CardEffect effect: card.getCardEffects()) {
            effect.execute(this);
        }
    }

    public void tryDrawCard(){
        if(activePlayerIndex != 0) {
            gameViewController.setStatusText("You can only draw a card on your own turn!");
        }else if(playedCardThisTurn){
            gameViewController.setStatusText("You can't draw after playing a card!");
        }else{
            drawCard(0);
        }
    }

    public Card drawCard(int playerIndex) {
        if (activePlayerIndex == playerIndex && gameStart) {
            gameViewController.setStatusText("You draw a card.");
            Player player = players.get(playerIndex);
            Card drawnCard = drawDeck.getRandom();
            if (drawnCard != null) {
                player.addCardToHand(drawnCard);
            } else {
                gameViewController.setDrawPileVisible(false);
            }
            if (playerIndex == 0) {
                gameViewController.setPlayerCardViews(player.getCardsInHand());
            } else {
                gameViewController.setNumberOfCPUCards(player.getCardsInHand().size(), playerIndex);
            }
            if (drawDeck.getCardsLeft() <= 0) {
                //reshuffle discard pile to draw pile
                reshufflePiles();
                if (drawDeck.getCards().size() <= 0) {
                    return null;
                }
            }
            return drawnCard;
        }
        return null;
    }

    public void changeLastPlayedCardColor(){
        String color;
        if(activePlayerIndex == 0){
             color = gameViewController.showCardColorPicker();
        }else{
            switch (random.nextInt(4)){
                case 0:
                    color = "#d72600";
                    break;
                case 1:
                    color = "#379711";
                    break;
                case 2:
                    color = "#0956bf";
                    break;
                case 3:
                    color = "#ecd407";
                    break;
                default:
                    throw new RuntimeException("this should never happen");
            }
            Card card = discardDeck.getTopMost();
            card.setColor(color);
            setLastPlayedCard(card);
        }
    }

    private void setLastPlayedCard(Card card) {
        discardDeck.placeCard(card);
        gameViewController.setDiscardPileCard(card);
    }

    public void nextTurn() {
        if (!playedCardThisTurn) {
            gameViewController.setStatusText("You still need to play a card!");
            return;
        }
        playedCardThisTurn = false;
        if (turnDirection == TurnDirection.CLOCKWISE) {
            if (activePlayerIndex >= (players.size() - 1)) {
                activePlayerIndex = 0;
            } else {
                activePlayerIndex++;
            }
        } else {
            if (activePlayerIndex <= 0) {
                activePlayerIndex = players.size() - 1;
            } else {
                activePlayerIndex--;
            }
        }
        if (activePlayerIndex != 0) {
            doCPUMoves();
        }else{
            gameViewController.setStatusText("It's your turn!");
        }
    }

    public void drawCardEffect() {
        int nextPlayer;
        if (turnDirection == TurnDirection.CLOCKWISE) {
            if (activePlayerIndex >= (players.size() - 1)) {
                nextPlayer = 0;
            } else {
                nextPlayer = activePlayerIndex + 1;
            }
        } else {
            if (activePlayerIndex <= 0) {
                nextPlayer = players.size() - 1;
            } else {
                nextPlayer = activePlayerIndex - 1;
            }
        }
        drawCard(nextPlayer);
    }

    public void reverseTurnDirection() {
        if (turnDirection == TurnDirection.CLOCKWISE) {
            turnDirection = TurnDirection.COUNTERCLOCKWISE;
        } else {
            turnDirection = TurnDirection.CLOCKWISE;
        }
        gameViewController.setTurnDirText(turnDirection);
    }

    public void selectWinner(String winner) {
        gameViewController.setStatusText(winner + " has won the game!!!");
        endGame();
    }

    public void endGame() {
        gameStart = false;
    }

    public void addPlayer(String playerName) {
        Player player = new Player(playerName);
        players.add(player);
    }

    private void reshufflePiles() {
        if (discardDeck.getCards().size() > 1) {
            drawDeck.setCards(new ArrayList<>(discardDeck.getCards()));
            discardDeck.clear();
            if (drawDeck.getCards().size() > 0) {
                discardDeck.placeCard(drawDeck.getRandom());
                gameViewController.setDiscardPileCard(discardDeck.getTopMost());
                gameViewController.setDrawPileVisible(true);
            } else {
                gameViewController.setDrawPileVisible(false);
            }
        }

    }

    private void shareStartCards(int startAmount) {
        for (int i = 0; i < players.size(); i++) {
            for (int p = 0; p < startAmount; p++) {
                Player player = players.get(i);
                Card c = drawDeck.getRandom();
                player.addCardToHand(c);
                if (i == 0) {
                    gameViewController.setPlayerCardViews(player.getCardsInHand());
                } else {
                    gameViewController.setNumberOfCPUCards(player.getCardsInHand().size(), i);
                }
            }
        }
    }
}
