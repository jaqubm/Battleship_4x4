package com.battleship_4x4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

class GameThread implements Runnable {

    Client client;  //Communication with server

    Game game;  //Game Controller
    private final long ROUND_TIME = 50000;   //Round time in ms

    public void setGame(Client client, Game game) {
        this.client = client;
        this.game = game;

        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        //Waiting for all players to be ready
        try {
            if(client.getData() == 0) {
                System.out.println("All players are ready!");

                int MAX_PLAYERS = 4;
                for(int i = 0; i<MAX_PLAYERS; i++) {
                    String name = client.getName();
                    game.addToScoreBoard(name);
                }

                Platform.runLater(() -> game.displayScoreBoard());

                while(true) {
                    int gameID = client.getData();
                    System.out.println("Client: gameID: " + gameID);

                    if(gameID == -1) {
                        break;
                    }
                    else if(gameID == client.getClientID()) {
                        game.setMyRound(true);
                        Platform.runLater(() -> {
                            try {
                                game.timer(ROUND_TIME, true);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    else
                        Platform.runLater(() -> {
                            try {
                                game.timer(ROUND_TIME, false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    //PlayerID = 4 - Map didn't change
                    int playerID = client.getData();
                    if(playerID != 4) {
                        int posID = client.getData();
                        int statusID = client.getData();

                        System.out.println("Client: Update from server: " + playerID + " " + posID + " " + statusID);

                        if(statusID == 2)
                            Platform.runLater(() -> game.setShot(true, playerID, posID));
                        else if(statusID == 3) {
                            Platform.runLater(() -> game.setShot(false, playerID, posID));
                            Platform.runLater(() -> game.updateScoreBoard(gameID));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


public class Game extends Application implements Initializable {

    Client client;
    private boolean myRound = false;

    private ArrayList<String> scoreBoardNames;
    private ArrayList<Integer> scoreBoardPoints;

    private int gridSize;
    private Timeline timeline;

    Image missedShot = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/board/marker_miss_64.png")).toString());
    Image hitShot = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/board/marker_hit_64.png")).toString());

    GridHandler backgroundGrid_1;
    GridHandler backgroundGrid_2;
    GridHandler backgroundGrid_3;
    GridHandler backgroundGrid_4;

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
    private Label firstPlace;
    @FXML
    private Label secondPlace;
    @FXML
    private Label thirdPlace;
    @FXML
    private Label fourthPlace;
    @FXML
    private Label timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image scoreboardImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/UI/scoreboard_480.png")).toString());
        Image timerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/timer_64.gif")).toString());

        ImagePattern scoreboardImagePattern = new ImagePattern(scoreboardImage);
        ImagePattern timerImagePattern = new ImagePattern(timerImage);

        scoreboard.setFill(scoreboardImagePattern);
        timer.setFill(timerImagePattern);

        gridSize = (int) (boardPane1.getPrefWidth() / 8);

        backgroundGrid_1 = new GridHandler(boardPane1.getPrefWidth(), boardPane1.getPrefHeight(), gridSize, boardPane1);
        backgroundGrid_2 = new GridHandler(boardPane2.getPrefWidth(), boardPane2.getPrefHeight(), gridSize, boardPane2);
        backgroundGrid_3 = new GridHandler(boardPane3.getPrefWidth(), boardPane3.getPrefHeight(), gridSize, boardPane3);
        backgroundGrid_4 = new GridHandler(boardPane4.getPrefWidth(), boardPane4.getPrefHeight(), gridSize, boardPane4);

        Image waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_64.gif")).toString());
        Image waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_64.gif")).toString());
        backgroundGrid_1.createSetupGrid(waterImage, waterDarkerImage);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_yellow_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_yellow_64.gif")).toString());
        backgroundGrid_2.createGameGrid(waterImage, waterDarkerImage, 1, this);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_green_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_green_64.gif")).toString());
        backgroundGrid_3.createGameGrid(waterImage, waterDarkerImage, 2, this);

        waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_red_64.gif")).toString());
        waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_red_64.gif")).toString());
        backgroundGrid_4.createGameGrid(waterImage, waterDarkerImage, 3, this);

        scoreBoardNames = new ArrayList<>();
        scoreBoardPoints = new ArrayList<>();
    }

    public synchronized void handleGridClick(int quarter, int pos) throws IOException {
        if(myRound) {
            client.sendData(quarter);
            client.sendData(pos);

            myRound = false;

            if(timeline != null)
                timeline.stop();
        }
    }

    public void setShot(boolean missed, int quarter, int pos) {
        if(missed) {
            if(quarter == 0)
                backgroundGrid_1.fillRectangle(pos, missedShot);
            else if(quarter == 1)
                backgroundGrid_2.fillRectangle(pos, missedShot);
            else if(quarter == 2)
                backgroundGrid_3.fillRectangle(pos, missedShot);
            else if(quarter == 3)
                backgroundGrid_4.fillRectangle(pos, missedShot);
        }
        else {
            if(quarter == 0)
                backgroundGrid_1.fillRectangle(pos, hitShot);
            else if(quarter == 1)
                backgroundGrid_2.fillRectangle(pos, hitShot);
            else if(quarter == 2)
                backgroundGrid_3.fillRectangle(pos, hitShot);
            else if(quarter == 3)
                backgroundGrid_4.fillRectangle(pos, hitShot);
        }
    }

    public void setMyRound(boolean myRound) {
        this.myRound = myRound;
    }

    public void setShips(int size, int x, int y, boolean direction) {
        Ship ship = new Ship(gridSize, size);
        ship.move(x, y);

        if (!direction) {
            Rotate rotate;
            Node node = ship.getRectangle();
            ship.move(ship.toBoard(node.getLayoutX()), ship.toBoard(node.getLayoutY()));
            rotate = new Rotate(-90, (double) gridSize / 2, (double) gridSize / 2);
            node.getTransforms().add(rotate);
        }

        boardPane1.getChildren().add(ship.getRectangle());
    }

    public void addToScoreBoard(String name) {
        this.scoreBoardNames.add(name);
        this.scoreBoardPoints.add(0);
    }

    public void updateScoreBoard(int id) {
        scoreBoardPoints.set(id, scoreBoardPoints.get(id) + 1);
        for(int i=0; i<scoreBoardNames.size(); i++) {
            System.out.println("Client: Player: " + scoreBoardNames.get(i) + " Points: " + scoreBoardPoints.get(i));
        }
        displayScoreBoard();
    }

    public void displayScoreBoard() {
        firstPlace.setText(scoreBoardNames.get(0) + ": " + scoreBoardPoints.get(0));
        secondPlace.setText(scoreBoardNames.get(1) + ": " + scoreBoardPoints.get(1));
        thirdPlace.setText(scoreBoardNames.get(2) + ": " + scoreBoardPoints.get(2));
        fourthPlace.setText(scoreBoardNames.get(3) + ": " + scoreBoardPoints.get(3));
    }

    public void setGameThread(Client client) {
        this.client = client;
        new GameThread().setGame(client, this);
    }

    public void timer(long time, boolean playerPlay) throws IOException {
        AtomicLong timeLimit= new AtomicLong(time);
        updateTimerLabel(timeLimit.get(), playerPlay);

        if(timeline != null)
            timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            try {
                timeLimit.set(updateTimerLabel(timeLimit.get(), playerPlay));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private synchronized long updateTimerLabel(long timeLimit, boolean playerPlay) throws IOException {
        timeLimit=timeLimit-10;

        long second = timeLimit / 1000;
        long millisecond = (timeLimit % 1000) / 10;
        String time = String.format("%02d:%02d", second, millisecond);
        timeLabel.setText(time);;

        if(timeLimit <= 0) {
            timeline.stop();

            if(playerPlay && myRound) {
                client.sendData(-1);
                myRound = false;
            }
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
