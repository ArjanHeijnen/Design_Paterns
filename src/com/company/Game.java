package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int activePlayerIndex = 0;
    private int turnCount = 1;
    private List<String> players = new ArrayList<String>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;

    public Game() {
        addPlayers();
    }

    public void startGame() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (gameStart) {
            System.out.println(players.get(activePlayerIndex) + "'s Turn(" + turnCount + "):");
            String playedCard = reader.readLine();
            playCard(playedCard);
            nextTurn();
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

    private void addPlayers() {
        players.add("player1");
        players.add("player2");
        players.add("player3");
        players.add("player4");
        System.out.println(players);
    }

}
