package com.stendenstudenten.unogame;


import com.stendenstudenten.unogame.card.Card;
import com.stendenstudenten.unogame.card.CardEffect;
import com.stendenstudenten.unogame.controllers.GameViewController;
import com.stendenstudenten.unogame.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    final GameViewController gameViewController;
    private int activePlayerIndex = 0;
    private final List<Player> players = new ArrayList<>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;
    private final Deck discardDeck;
    private final Deck drawDeck;
    private final Random random = new Random();
    private boolean playedCardThisTurn = false;
    private String wildColor = null;
    private boolean skipNextTurn = false;
    private int forceDraw = 0;

    public Game(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
        drawDeck = new Deck();
        discardDeck = new Deck();
        drawDeck.populate();
        setLastPlayedCard(drawDeck.getTopMost());
        drawDeck.getCards().remove(drawDeck.getCardsLeft() - 1);
    }

    public void startGame() {
        shareStartCards();
        gameViewController.setStatusText("It's your turn!");
    }

    private void doCPUMoves() {
        if (activePlayerIndex != 0) {
            Player player = players.get(activePlayerIndex);
            int cardsDrawn = 0;
            List<Card> hand = player.getCardsInHand();

            while(forceDraw > 0){
                drawCard(activePlayerIndex);
                cardsDrawn++;
                forceDraw--;
            }

            Card cardToPlay = null;
            for (Card card : hand) {
                if (canBePlayed(card)){
                    clearWildColor();
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
                if (canBePlayed(drawnCard)){
                    clearWildColor();
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
        }else if(forceDraw > 0){
            gameViewController.setStatusText("You need to draw " + forceDraw + " cards first.");
            return;
        } else if(playedCardThisTurn){
            gameViewController.setStatusText("You already played a card this turn!");
            return;
        }
            List<Card> playerHand = players.get(0).getCardsInHand();
            Card playedCard = playerHand.get(cardIndex);

            if (canBePlayed(playedCard)) {
                clearWildColor();
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
            if(forceDraw > 0){
                forceDraw--;
            }
            if(forceDraw > 0){
                gameViewController.setStatusText("You still need to draw " + forceDraw + " more cards.");
            }else{
                gameViewController.setStatusText("You draw a card.");
            }
        }
    }

    public void addForceDraw(){
        forceDraw++;
    }

    public Card drawCard(int playerIndex) {
        if (activePlayerIndex == playerIndex && gameStart) {

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
            System.out.println("cards left in draw pile: " + drawDeck.getCardsLeft());
            System.out.println("cards in discard " + discardDeck.getCardsLeft());
            if (drawDeck.getCardsLeft() <= 0) {
                //reshuffle discard pile to draw pile
                reshufflePiles();
                System.out.println("Pile shuffled");
                if (drawDeck.getCards().size() <= 0) {
                    return null;
                }
            }
            return drawnCard;
        }
        return null;
    }

    public void setWildColor(){
        String color;
        if(activePlayerIndex == 0){
             color = gameViewController.showCardColorPicker();
        }else{
            color = switch (random.nextInt(4)) {
                case 0 -> "#d72600";
                case 1 -> "#379711";
                case 2 -> "#0956bf";
                case 3 -> "#ecd407";
                default -> throw new RuntimeException("this should never happen");
            };
        }
        wildColor = color;
        System.out.println("set wild to:" + color);
        gameViewController.setWildIndicator(color);
    }

    private void clearWildColor(){
        wildColor = null;
        gameViewController.setWildIndicator(null);
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

        int amountToMove = 1;
        if(skipNextTurn){
            amountToMove = 2;
            skipNextTurn = false;
        }
        if (turnDirection == TurnDirection.CLOCKWISE) {
            if (activePlayerIndex + amountToMove > (players.size() - 1)) {
                activePlayerIndex = amountToMove - ((players.size() - 1) - activePlayerIndex) - 1;
            } else {
                activePlayerIndex += amountToMove;
            }
        } else {
            if (activePlayerIndex - amountToMove < 0) {
                activePlayerIndex = players.size() - (amountToMove - activePlayerIndex);
            } else {
                activePlayerIndex -= amountToMove;
            }
        }
        if (activePlayerIndex != 0) {
            doCPUMoves();
        }else{
            gameViewController.setStatusText("It's your turn!");
        }
    }

    public void skipNextTurn(){
        skipNextTurn = true;
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

    private boolean canBePlayed(Card card){
        return  card.alwaysMatches() || wildColor != null && Objects.equals(card.getColor(), wildColor) || wildColor == null && card.matches(discardDeck.getTopMost());
    }

    private void shareStartCards() {
        for (int i = 0; i < players.size(); i++) {
            for (int p = 0; p < 7; p++) {
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
