package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.Game;
import com.stendenstudenten.unogame.GameApplication;
import com.stendenstudenten.unogame.TurnDirection;
import com.stendenstudenten.unogame.card.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private Text TurnDirText;
    @FXML
    private Text StatusText;
    @FXML
    private Button nextTurnButton;
    @FXML
    private Ellipse wildIndicator;

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
        nextTurnButton.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onNextTurnButtonClick);
        setTurnDirText(TurnDirection.CLOCKWISE);
        wildIndicator.setVisible(false);

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

    public void setWildIndicator(String color){
        if(color != null){
            wildIndicator.setFill(Paint.valueOf(color));
            wildIndicator.setVisible(true);
        }else{
            wildIndicator.setVisible(false);
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

    public String showCardColorPicker() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.resizableProperty().set(false);
        alert.setTitle("Uno");
        alert.setContentText("Wild card!");
        alert.setContentText("Pick a color.");
        ButtonType buttonRed = new ButtonType("Red");
        ButtonType buttonGreen = new ButtonType("Green");
        ButtonType buttonYellow = new ButtonType("Yellow");
        ButtonType buttonBlue = new ButtonType("Blue");
        alert.getButtonTypes().setAll(buttonRed, buttonBlue, buttonYellow, buttonGreen);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty()){
            throw new RuntimeException("Color picker closed with no result");
        }
        if(result.get() == buttonRed){
            return "#d72600";
        }else if(result.get() == buttonGreen){
            return "#379711";
        }else if(result.get() == buttonYellow){
            return "#ecd407";
        }else if(result.get() == buttonBlue){
            return "#0956bf";
        }else{
            throw new RuntimeException("Color picker had invalid result");
        }
    }

    private void onPlayerCardClick(MouseEvent event){
        StackPane clickedCardView = (StackPane) event.getSource();
        int i = PlayerHandPanel.getChildren().indexOf(clickedCardView);
        if(i >= 0){
            game.tryPlayCard(i);
        }
    }

    private void onDrawPileClick(MouseEvent event){
      game.tryDrawCard();
    }

    private void onNextTurnButtonClick(MouseEvent event){game.nextTurn();}
}
