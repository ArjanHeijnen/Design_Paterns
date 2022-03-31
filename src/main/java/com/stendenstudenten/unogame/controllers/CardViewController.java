package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.card.Card;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CardViewController {

    @FXML
    private Rectangle cardRectangle;
    @FXML
    private Ellipse cardEllipse;
    @FXML
    private Text cardSymbolMain;
    @FXML
    private Text cardSymbolBR;
    @FXML
    private Text cardSymbolTL;


    public void setCard(Card card){
        cardSymbolMain.setVisible(true);
        cardSymbolBR.setVisible(true);
        cardSymbolTL.setVisible(true);

        cardRectangle.setFill(Paint.valueOf(card.getColor()));
        cardSymbolMain.setFill(Paint.valueOf(card.getColor()));
        cardEllipse.setFill(Paint.valueOf("#ffffff"));
        cardEllipse.setStrokeWidth(0);

        cardSymbolMain.setText(String.valueOf(card.getSymbol()));
        cardSymbolBR.setText(String.valueOf(card.getSymbol()));
        cardSymbolTL.setText(String.valueOf(card.getSymbol()));
    }

    public void setFaceDown(){
        cardSymbolMain.setVisible(false);
        cardSymbolBR.setVisible(false);
        cardSymbolTL.setVisible(false);

        cardRectangle.setFill(Paint.valueOf("#000000"));
        cardEllipse.setFill(Paint.valueOf("#e11b22"));
    }
}
