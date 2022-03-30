package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.GameApplication;
import com.stendenstudenten.unogame.card.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
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
    public void initialize(){

        PlayerHandPanel.getChildren().clear();
        CPU1HandGroup.getChildren().clear();
        CPU2HandGroup.getChildren().clear();
        CPU3HandGroup.getChildren().clear();
        DiscardPileCard.getChildren().clear();
        DrawPileCard.getChildren().clear();

        DrawPileCard.getChildren().add(loadFaceDownCardView());

        addPlayerCardView(new Card("#99ff00", 5));
        addPlayerCardView(new Card("#99ff00", 3));
        addPlayerCardView(new Card("#99ff00", 2));
        addPlayerCardView(new Card("#99ff00", 0));
        addPlayerCardView(new Card("#99ff00", 2));

        setDiscardPileCard(new Card("#99ff00", 2));

        setNumberOfCPUCards(3, 1);
        setNumberOfCPUCards(6, 2);
        setNumberOfCPUCards(1, 3);


        /*game = new Game();
        game.addPlayer("player");
        game.addPlayer("CPU1");
        game.addPlayer("CPU2");
        game.addPlayer("CPU3");
        try {
            game.startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    private void addPlayerCardView(Card card){
        PlayerHandPanel.getChildren().add(loadFaceUpCardView(card));
    }

    private void setNumberOfCPUCards(int amount, int playerIndex){
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

    private void setDiscardPileCard(Card card){
        DiscardPileCard.getChildren().clear();
        DiscardPileCard.getChildren().add(loadFaceUpCardView(card));
    }

    private void setDrawPileVisible(boolean isVisible){
        DrawPileCard.setVisible(isVisible);
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
        System.out.println(i);
    }
}
