package com.stendenstudenten.unogame;


import com.stendenstudenten.unogame.card.Card;
import com.stendenstudenten.unogame.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private int activePlayerIndex = 0;
    private int turnCount = 1;
    private List<Player> players = new ArrayList<Player>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;
    private final CardBuilder cardBuilder = new CardBuilder();
    private Card lastPlayedCard = new Card("red", 1);

    public Game() {
    }

    public void startGame() throws IOException {
        shareStartCards(7);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (gameStart) {
            System.out.println(players.get(activePlayerIndex).getPlayerName() + "'s Turn(" + turnCount + "):");
            System.out.println(players.get(activePlayerIndex).getCardsInHandString());
            String playedCard = reader.readLine();

            if (isNumeric(playedCard)) {
                int cardNumber = Integer.parseInt(playedCard);
                if (cardNumber < players.get(activePlayerIndex).getCardsInHand().size() && cardNumber > 0) {
                    if (Objects.equals(players.get(activePlayerIndex).playCard(cardNumber).getColour(), lastPlayedCard.getColour()) || players.get(activePlayerIndex).playCard(cardNumber).getNumber() == lastPlayedCard.getNumber()) {
                        playCard(playedCard + " AND " + players.get(activePlayerIndex).playCard(cardNumber).getColour() + players.get(activePlayerIndex).playCard(cardNumber).getNumber());
                        if (players.get(activePlayerIndex).getCardsInHand().size() == 0) {
                            selectWinner(players.get(activePlayerIndex).getPlayerName());
                        }
                        lastPlayedCard = players.get(activePlayerIndex).playCard(cardNumber);
                        players.get(activePlayerIndex).removeCardFromHand(cardNumber);
                        nextTurn();
                    } else {
                        System.out.println("try again");
                    }
                } else {
                    System.out.println("try again");
                }
            } else {
                if (playedCard.equals("p")) {
                    players.get(activePlayerIndex).addCardToHand(cardBuilder.BuildCard());
                    System.out.println("Picked a card");
                } else {
                    System.out.println("try again");
                }
            }
        }
        System.out.println("End Game");
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
        turnCount++;
    }

    public void reverseTurnDirection() {
        if (turnDirection == TurnDirection.CLOCKWISE) {
            turnDirection = TurnDirection.COUNTERCLOCKWISE;
        } else {
            turnDirection = TurnDirection.CLOCKWISE;
        }
    }

    public void playCard(String playedCard) {
        System.out.println(playedCard);
    }

    public void selectWinner(String winner) {
        System.out.println(winner + " has won the game!!!");
        endGame();
    }

    public void endGame() {
        gameStart = false;
    }

    public void addPlayer(String playerName) {
        Player player = new Player(playerName);
        players.add(player);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void shareStartCards(int startAmount) {
        for (Player player : players) {
            for (int p = 0; p < startAmount; p++) {
                Card c = cardBuilder.BuildCard();
                player.addCardToHand(c);
                System.out.println(c.getColour() + c.getNumber());
            }
        }
    }
}
