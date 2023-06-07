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
import java.util.concurrent.atomic.AtomicLong;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

class GameThread implements Runnable {

    Client client;

    Game game;

    public void setGame(Client client, Game game) {
        this.client = client;
        this.game = game;

        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        try {
            int data = client.getData();
            System.out.println("Client: " + client.getClientID() + " received - " + data);
            if(data == 3) {
                game.setMyRound(true);
                game.timer(10000);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


public class Game extends Application implements Initializable {

    boolean myRound = false;

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

        Image waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_64.gif")).toString());
        Image waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_64.gif")).toString());
        backgroundGrid_1.createSetupGrid(waterImage, waterDarkerImage);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_yellow_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_yellow_64.gif")).toString());
        backgroundGrid_2.createGameGrid(waterImage, waterDarkerImage, 2, this);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_green_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_green_64.gif")).toString());
        backgroundGrid_3.createGameGrid(waterImage, waterDarkerImage, 3, this);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_red_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_red_64.gif")).toString());
        backgroundGrid_4.createGameGrid(waterImage, waterDarkerImage, 4, this);

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

       timer(10000);
    }

    public void handleGridClick(int row, int column, int quarter) {
        if(myRound) {
            System.out.println("Clicked cell: row=" + row + ", column=" + column + " quarter" + quarter);
        }
    }

    public void setMyRound(boolean myRound) {
        this.myRound = myRound;
    }

    public void setGameThread(Client client) {
        new GameThread().setGame(client, this);
    }

    public void timer(long time){
        AtomicLong timeLimit= new AtomicLong(time);
        updateTimerLabel(timeLimit.get());

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            timeLimit.set(updateTimerLabel(timeLimit.get()));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private long updateTimerLabel(long timeLimit) {
        timeLimit=timeLimit-10;

        long second = timeLimit / 1000;
        long millisecond = (timeLimit % 1000) / 10;
        String time = String.format("%02d:%02d", second, millisecond);
        timeLabel.setText(time);;

        if(timeLimit==0){
            timeline.stop();
        }
        return timeLimit;
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

