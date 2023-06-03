package com.battleship_4x4;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Game extends Application {

    @FXML
    private AnchorPane boardPane_1;
    @FXML
    private AnchorPane boardPane_2;
    @FXML
    private AnchorPane boardPane_3;
    @FXML
    private AnchorPane boardPane_4;
    public List<Ship> ships = new ArrayList<>();

    public void Initialize() {
        int gridSize = 32;

        GridHandler backgroundGrid_1 = new GridHandler(boardPane_1.getPrefWidth(), boardPane_1.getPrefHeight(), gridSize, boardPane_1);
        GridHandler backgroundGrid_2 = new GridHandler(boardPane_2.getPrefWidth(), boardPane_2.getPrefHeight(), gridSize, boardPane_2);
        GridHandler backgroundGrid_3 = new GridHandler(boardPane_3.getPrefWidth(), boardPane_3.getPrefHeight(), gridSize, boardPane_3);
        GridHandler backgroundGrid_4 = new GridHandler(boardPane_4.getPrefWidth(), boardPane_4.getPrefHeight(), gridSize, boardPane_4);

        backgroundGrid_1.createGrid();
        backgroundGrid_2.createGrid();
        backgroundGrid_3.createGrid();
        backgroundGrid_4.createGrid();

        Ship ship2 = new Ship(gridSize, 2);
        ships.add(ship2);
        boardPane_1.getChildren().add(ship2.getRectangle());

        Ship ship3 = new Ship(gridSize, 3);
        ships.add(ship3);
        ship3.move(0, 1);
        boardPane_1.getChildren().add(ship3.getRectangle());

        Ship ship4 = new Ship(gridSize, 4);
        ships.add(ship4);
        ship4.move(0, 2);
        boardPane_1.getChildren().add(ship4.getRectangle());

        Ship ship5 = new Ship(gridSize, 5);
        ships.add(ship5);
        ship5.move(0, 3);
        boardPane_1.getChildren().add(ship5.getRectangle());
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        Initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(Setup.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        stage.setTitle("Setup");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
