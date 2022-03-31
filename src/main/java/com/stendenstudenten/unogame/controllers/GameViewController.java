package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.Game;
import com.stendenstudenten.unogame.GameApplication;
import com.stendenstudenten.unogame.TurnDirection;
import com.stendenstudenten.unogame.card.Card;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GameViewController {

    @FXML
    private Pane PlayerHandPanel;
    @FXML
    private Group CPU1HandGroup;
    @FXML
    private Group CPU2HandGroup;
    @FXML
    private Group CPU3HandGroup;
    @FXML
    private Pane DiscardPileCard;
    @FXML
    private Pane DrawPileCard;
    @FXML
    private Text TurnText;
    @FXML
    private Text TurnDirText;
    @FXML
    private Text StatusText;

    private Game game;


    @FXML
    public void initialize(){

        PlayerHandPanel.getChildren().clear();
        CPU1HandGroup.getChildren().clear();
        CPU2HandGroup.getChildren().clear();
        CPU3HandGroup.getChildren().clear();
        DiscardPileCard.getChildren().clear();
        DrawPileCard.getChildren().clear();

        DrawPileCard.getChildren().add(loadFaceDownCardView());
        DrawPileCard.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onDrawPileClick);
        setTurnDirText(TurnDirection.CLOCKWISE);
        setTurnText(0);

        game = new Game(this);
        game.addPlayer("player");
        game.addPlayer("CPU1");
        game.addPlayer("CPU2");
        game.addPlayer("CPU3");
        try {
            game.startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTurnDirText(TurnDirection direction){
        String text;
        switch (direction){
            case COUNTERCLOCKWISE -> text = "Counter Clockwise";
            case CLOCKWISE -> text = "Clockwise";
            default -> throw new IllegalArgumentException();
        }
        TurnDirText.textProperty().set(text);
    }

    private void setTurnText(int turnIndex){
        String text;
        switch (turnIndex){
            case 0 -> text = "Player";
            case 1 -> text = "CPU 1";
            case 2 -> text = "CPU 2";
            case 3 -> text = "CPU 3";
            default -> throw new IndexOutOfBoundsException();
        }
        TurnText.textProperty().set(text);
    }

    public void setPlayerCardViews(List<Card> playerHand){
        PlayerHandPanel.getChildren().clear();
        for (Card card: playerHand){
            PlayerHandPanel.getChildren().add(loadFaceUpCardView(card));
        }
    }

    public void setNumberOfCPUCards(int amount, int playerIndex){
        Group group;
        int rotation;
        boolean hasXOffset;
        boolean hasYOffset;
        switch (playerIndex) {
            case 1 -> {
                group = CPU1HandGroup;
                rotation = 90;
                hasXOffset = false;
                hasYOffset = true;
            }
            case 2 -> {
                group = CPU2HandGroup;
                rotation = 0;
                hasXOffset = true;
                hasYOffset = false;
            }
            case 3 -> {
                group = CPU3HandGroup;
                rotation = -90;
                hasXOffset = false;
                hasYOffset = true;
            }
            default -> throw new IndexOutOfBoundsException();
        }
        group.getChildren().clear();
        for(int i = 0; i < amount; i++){
            Pane cardView = loadFaceDownCardView();
            cardView.rotateProperty().set(rotation);
            if(hasXOffset){
                cardView.translateXProperty().set(10 * i);
            }
            if(hasYOffset){
                cardView.translateYProperty().set(10 * i);
            }
            group.getChildren().add(cardView);
        }
    }

    public void setDiscardPileCard(Card card){
        DiscardPileCard.getChildren().clear();
        DiscardPileCard.getChildren().add(loadFaceUpCardView(card));
    }

    public void setDrawPileVisible(boolean isVisible){
        DrawPileCard.setVisible(isVisible);
    }

    public void setStatusText(String text) {
        StatusText.textProperty().set(text);
    }

    private Pane loadFaceUpCardView(Card card){
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GameApplication.class.getResource("CardView.fxml")));
        StackPane cardView;
        try{
            cardView  = loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        CardViewController controller = loader.getController();
        controller.setCard(card);
        //set click handler
        cardView.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onPlayerCardClick);
        return cardView;
    }

    private Pane loadFaceDownCardView(){
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GameApplication.class.getResource("CardView.fxml")));
        StackPane cardView;
        try{
            cardView  = loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        CardViewController controller = loader.getController();
        controller.setFaceDown();
        return cardView;
    }

    private void onPlayerCardClick(MouseEvent event){
        StackPane clickedCardView = (StackPane) event.getSource();
        int i = PlayerHandPanel.getChildren().indexOf(clickedCardView);
        if(i >= 0){
            game.doPlayerMove(i);
        }
    }

    private void onDrawPileClick(MouseEvent event){
      game.drawCard(0);
    }
}
