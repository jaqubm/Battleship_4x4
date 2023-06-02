package com.battleship_4x4;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane boardPane;
    public List<Ship> ships = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int gridSize = 64;
        DraggableMakerGrid draggableMakerGrid = new DraggableMakerGrid(boardPane.getPrefWidth(), boardPane.getPrefHeight(), gridSize, boardPane);

        GridHandler backgroundGridHandler = new GridHandler(boardPane.getPrefWidth(), boardPane.getPrefHeight(), gridSize, boardPane);
        backgroundGridHandler.createGrid();

        Ship ship2 = new Ship(gridSize, 2);
        ships.add(ship2);
        boardPane.getChildren().add(ship2.getRectangle());
        draggableMakerGrid.makeDraggable(ship2, ships);

        Ship ship3 = new Ship(gridSize, 3);
        ships.add(ship3);
        ship3.move(0, 1);
        boardPane.getChildren().add(ship3.getRectangle());
        draggableMakerGrid.makeDraggable(ship3, ships);

        Ship ship4 = new Ship(gridSize, 4);
        ships.add(ship4);
        ship4.move(0, 2);
        boardPane.getChildren().add(ship4.getRectangle());
        draggableMakerGrid.makeDraggable(ship4, ships);

        Ship ship5 = new Ship(gridSize, 5);
        ships.add(ship5);
        ship5.move(0, 3);
        boardPane.getChildren().add(ship5.getRectangle());
        draggableMakerGrid.makeDraggable(ship5, ships);
    }

    public void onReadyButtonClick() {
        for (Ship tempShip: ships){
            for (int i = 0; i < tempShip.getSize(); i++)
            {
                System.out.println(tempShip.pointList.get(i).getX() + " " + tempShip.pointList.get(i).getY());
            }
            System.out.println(" ");
        }
    }
}