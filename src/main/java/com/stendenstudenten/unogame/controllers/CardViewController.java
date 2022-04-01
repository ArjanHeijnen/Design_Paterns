package com.stendenstudenten.unogame.controllers;

import com.stendenstudenten.unogame.card.Card;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Objects;

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
    @FXML
    private ImageView imageMain;


    public void setCard(Card card){
        cardRectangle.setFill(Paint.valueOf(card.getColor()));
        cardSymbolMain.setFill(Paint.valueOf(card.getColor()));
        cardEllipse.setFill(Paint.valueOf("#ffffff"));
        cardEllipse.setStrokeWidth(0);

        int symbol = card.getSymbol();
        if(symbol >= 10){
            //use image
            cardSymbolMain.setVisible(false);
            cardSymbolBR.setVisible(false);
            cardSymbolTL.setVisible(false);
            imageMain.setVisible(true);
            String filename = switch (symbol) {
                case 11 -> "wild4";
                case 12 -> "skip";
                case 13 -> "reverse";
                case 14 -> "draw2";
                default -> "wild";
            };
            if(symbol != 10 && symbol != 11){
                filename = switch (card.getColor()) {
                    case "#d72600" -> filename + "R";
                    case "#0956bf" -> filename + "B";
                    case "#379711" -> filename + "G";
                    case "#ecd407" -> filename + "Y";
                    default -> filename;
                };
            }
            try {
                String url = Objects.requireNonNull(getClass().getResource("/com/stendenstudenten/unogame/img/" + filename + ".png")).toString();
                imageMain.setImage(new Image(url));
            }catch (NullPointerException e){
                System.out.println("failed to load image with name: " + filename);
            }


        }else{
            //use text
            cardSymbolMain.setVisible(true);
            cardSymbolBR.setVisible(true);
            cardSymbolTL.setVisible(true);
            imageMain.setVisible(false);

            cardSymbolMain.setText(String.valueOf(symbol));
            cardSymbolBR.setText(String.valueOf(symbol));
            cardSymbolTL.setText(String.valueOf(symbol));
        }
    }

    public void setFaceDown(){
        cardSymbolMain.setVisible(false);
        cardSymbolBR.setVisible(false);
        cardSymbolTL.setVisible(false);
        imageMain.setVisible(false);

        cardRectangle.setFill(Paint.valueOf("#000000"));
        cardEllipse.setFill(Paint.valueOf("#e11b22"));
    }
}
