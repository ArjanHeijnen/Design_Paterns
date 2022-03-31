package com.stendenstudenten.unogame;


import com.stendenstudenten.unogame.card.Card;
import com.stendenstudenten.unogame.controllers.GameViewController;
import com.stendenstudenten.unogame.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Game {
    GameViewController gameViewController;
    private int activePlayerIndex = 0;
    private final List<Player> players = new ArrayList<Player>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;
    private final Deck discardDeck;
    private final Deck drawDeck;

    public Game(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
        drawDeck = new Deck();
        discardDeck = new Deck();
        setLastPlayedCard(drawDeck.getRandom());
    }

    public void startGame() throws IOException {
        shareStartCards(7);
        doCPUMoves();
    }

    private void doCPUMoves() {
        if (activePlayerIndex != 0) {
            gameViewController.setStatusText("its CPU turn:" + activePlayerIndex);
            List<Card> hand = players.get(activePlayerIndex).getCardsInHand();
            Card cardToPlay = null;
            for (Card card : hand) {
                if (card.matches(discardDeck.getTopMost())) {
                    cardToPlay = card;
                    gameViewController.setStatusText("found a card!");
                    break;
                }
            }

            while (cardToPlay == null) {
                gameViewController.setStatusText("cards left:" + hand.size());
                gameViewController.setNumberOfCPUCards(hand.size(), activePlayerIndex);
                Card drawnCard = drawCard(activePlayerIndex);
                if (drawnCard == null) {
                    nextTurn();
                    return;
                }
                if (drawnCard.matches(discardDeck.getTopMost())) {
                    cardToPlay = drawnCard;
                    gameViewController.setStatusText("found a card!");
                }
            }

            setLastPlayedCard(cardToPlay);
            hand.remove(cardToPlay);
            gameViewController.setStatusText("cards left:" + hand.size());
            gameViewController.setNumberOfCPUCards(hand.size(), activePlayerIndex);
            if (hand.size() <= 0) {
                if (cardToPlay.getSymbol() >= 10) {
                    cardToPlay.getCardEffect().execute(this);
                }
                selectWinner(players.get(activePlayerIndex).getPlayerName());
            } else {
                if (cardToPlay.getSymbol() >= 10) {
                    cardToPlay.getCardEffect().execute(this);
                }
                nextTurn();
            }
        }
    }

    public void doPlayerMove(int cardIndex) {
        if (activePlayerIndex == 0) {
            gameViewController.setStatusText("its players turn" + activePlayerIndex);
            List<Card> playerHand = players.get(activePlayerIndex).getCardsInHand();
            Card playedCard = playerHand.get(cardIndex);

            if (playedCard.matches(discardDeck.getTopMost())) {
                setLastPlayedCard(playedCard);
                playerHand.remove(cardIndex);
                gameViewController.setPlayerCardViews(playerHand);

                if (playerHand.size() <= 0) {
                    selectWinner(players.get(activePlayerIndex).getPlayerName());
                    if (playedCard.getSymbol() >= 10) {
                        playedCard.getCardEffect().execute(this);
                    }
                } else {
                    if (playedCard.getSymbol() >= 10) {
                        playedCard.getCardEffect().execute(this);
                    }
                    nextTurn();
                }
            }
        }
    }

    public Card drawCard(int playerIndex) {
        if (activePlayerIndex == playerIndex && gameStart) {
            gameViewController.setStatusText("drawing card...");
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

    private void setLastPlayedCard(Card card) {
        discardDeck.placeCard(card);
        gameViewController.setDiscardPileCard(card);
    }

    public void nextTurn() {
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
        doCPUMoves();
    }

    public void draw2CardsEffect() {
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
        drawCard(nextPlayer);
        drawCard(nextPlayer);
        drawCard(nextPlayer);
        drawCard(nextPlayer);
    }

    public void changeCardColour() {
        Card card = discardDeck.getTopMost();
        card.setColor("#d72600");
//        todo: let pick colour
        gameViewController.setDiscardPileCard(card);
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
