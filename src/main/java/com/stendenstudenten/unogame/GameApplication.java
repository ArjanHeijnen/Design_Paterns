package com.stendenstudenten.unogame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("playingField.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);
        stage.show();

        // write your code here
        /*Game game = new Game();
        game.addPlayer("player1");
        game.addPlayer("player2");
        game.addPlayer("player3");
        game.addPlayer("player4");
        game.startGame();*/
    }

    public static void main(String[] args) {
        launch();
    }
}