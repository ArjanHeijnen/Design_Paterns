package com.company;

import com.company.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int activePlayerIndex = 0;
    private int turnCount = 1;
    private List<Player> players = new ArrayList<Player>();
    private TurnDirection turnDirection = TurnDirection.CLOCKWISE;
    boolean gameStart = true;

    public Game() {
    }

    public void startGame() throws IOException {
        shareStartCards(7);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (gameStart) {
            System.out.println(players.get(activePlayerIndex).getPlayerName() + "'s Turn(" + turnCount + "):");
            System.out.println(players.get(activePlayerIndex).getCardsInHand());
            String playedCard = reader.readLine();
            playCard(playedCard + " AND "+ players.get(activePlayerIndex).playCard(Integer.parseInt(playedCard)));
            if (players.get(activePlayerIndex).getCardsInHand().size() == 0){
                selectWinner(players.get(activePlayerIndex).getPlayerName());
            }
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

    public void addPlayer(String playerName) {
        Player player = new Player(playerName);
        players.add(player);
    }

    private void shareStartCards(int startAmount) {
        for (int i = 0; i < players.size(); i++) {
            for (int p = 0; p < startAmount; p++) {
                players.get(i).addCardToHand(i + ":" + p);
            }
        }
    }
}
