package com.battleship_4x4;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShipSetup implements Initializable {

    Client client;

    @FXML
    private AnchorPane boardPane;
    public List<Ship> ships = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int gridSize = 64;
        DraggableMakerGrid draggableMakerGrid = new DraggableMakerGrid(boardPane.getPrefWidth(), boardPane.getPrefHeight(), gridSize, boardPane);

        GridHandler backgroundGridHandler = new GridHandler(boardPane.getPrefWidth(), boardPane.getPrefHeight(), gridSize, boardPane);

        Image waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_64.gif")).toString());
        Image waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_64.gif")).toString());
        backgroundGridHandler.createSetupGrid(waterImage, waterDarkerImage);

        Ship ship2 = new Ship(gridSize, 2);
        ships.add(ship2);
        ship2.move(15, 2);
        boardPane.getChildren().add(ship2.getRectangle());
        draggableMakerGrid.makeDraggable(ship2, ships);

        Ship ship3 = new Ship(gridSize, 3);
        ships.add(ship3);
        ship3.move(9, 4);
        boardPane.getChildren().add(ship3.getRectangle());
        draggableMakerGrid.makeDraggable(ship3, ships);

        Ship ship4 = new Ship(gridSize, 4);
        ships.add(ship4);
        ship4.move(13, 4);
        boardPane.getChildren().add(ship4.getRectangle());
        draggableMakerGrid.makeDraggable(ship4, ships);

        Ship ship5 = new Ship(gridSize, 5);
        ships.add(ship5);
        ship5.move(9, 2);
        boardPane.getChildren().add(ship5.getRectangle());
        draggableMakerGrid.makeDraggable(ship5, ships);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void onReadyButtonClick(ActionEvent event) throws IOException {
        for (Ship tempShip: ships){
            for (int i = 0; i < tempShip.getSize(); i++){
                if ((tempShip.pointList.get(i).getX() < 0 || tempShip.pointList.get(i).getX() > 7) ||
                        (tempShip.pointList.get(i).getY() < 0 || tempShip.pointList.get(i).getY() > 7)){
                    return;
                }
            }
        }

        for (Ship tempShip: ships)
            for (int i = 0; i < tempShip.getSize(); i++)
                client.sendData((int) ((tempShip.pointList.get(i).getY() * 8) + tempShip.pointList.get(i).getX()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();

        Game game = loader.getController();
        for (Ship ship : ships) game.setShips(ship.getSize(), ship.getX(), ship.getY(), ship.getDirection());
        game.setGameThread(client);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}