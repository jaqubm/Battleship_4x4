package com.battleship_4x4;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Game extends Application implements Initializable {

    @FXML
    private AnchorPane boardPane1;
    @FXML
    private AnchorPane boardPane2;
    @FXML
    private AnchorPane boardPane3;
    @FXML
    private AnchorPane boardPane4;
    @FXML
    private Rectangle timer;
    @FXML
    private Rectangle scoreboard;
    @FXML
    private Label timeLabel;
    private long timeLimit;
    Timeline timeline;
    public List<Ship> ships = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image scoreboardImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/UI/scoreboard_480.png")).toString());
        Image timerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/timer_64.gif")).toString());
        ImagePattern scoreboardImagePattern = new ImagePattern(scoreboardImage);
        ImagePattern timerImagePattern = new ImagePattern(timerImage);
        scoreboard.setFill(scoreboardImagePattern);
        timer.setFill(timerImagePattern);

        int gridSize = (int) (boardPane1.getPrefWidth() / 8);

        GridHandler backgroundGrid_1 = new GridHandler(boardPane1.getPrefWidth(), boardPane1.getPrefHeight(), gridSize, boardPane1);
        GridHandler backgroundGrid_2 = new GridHandler(boardPane2.getPrefWidth(), boardPane2.getPrefHeight(), gridSize, boardPane2);
        GridHandler backgroundGrid_3 = new GridHandler(boardPane3.getPrefWidth(), boardPane3.getPrefHeight(), gridSize, boardPane3);
        GridHandler backgroundGrid_4 = new GridHandler(boardPane4.getPrefWidth(), boardPane4.getPrefHeight(), gridSize, boardPane4);

        backgroundGrid_1.createGrid();
        backgroundGrid_2.createGrid();
        backgroundGrid_3.createGrid();
        backgroundGrid_4.createGrid();

        Ship ship2 = new Ship(gridSize, 2);
        ships.add(ship2);
        boardPane1.getChildren().add(ship2.getRectangle());

        Ship ship3 = new Ship(gridSize, 3);
        ships.add(ship3);
        ship3.move(0, 1);
        boardPane1.getChildren().add(ship3.getRectangle());

        Ship ship4 = new Ship(gridSize, 4);
        ships.add(ship4);
        ship4.move(0, 2);
        boardPane1.getChildren().add(ship4.getRectangle());

        Ship ship5 = new Ship(gridSize, 5);
        ships.add(ship5);
        ship5.move(0, 3);
        boardPane1.getChildren().add(ship5.getRectangle());

        timeLimit = 70;
        updateTimerLabel(timeLimit);

         timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTimerLabel(70);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void updateTimerLabel(long limit) {
        timeLimit=timeLimit-1;

        long minutes = timeLimit / 60;
        long seconds = timeLimit % 60;
        String time = String.format("%02d:%02d", minutes, seconds);
        timeLabel.setText(time);;

        if(timeLimit==0){
            System.out.println("Counting ends");
            timeline.stop();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Battleship4x4");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

    }
}

