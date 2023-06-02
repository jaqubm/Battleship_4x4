package com.battleship_4x4;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class GridHandler extends GridBase {

    Image waterImage = new Image(this.getClass().getResource("sprites/gifs/water_64.gif").toString());
    Image waterDarkerImage = new Image(this.getClass().getResource("sprites/gifs/water_darker_64.gif").toString());


    public GridHandler(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        super(planeWidth, planeHeight, gridSize, anchorPane);
    }

    public void createGrid() {
        ImagePattern waterImagePattern = new ImagePattern(waterImage);
        ImagePattern waterDarkerImagePattern = new ImagePattern(waterDarkerImage);

        for(int i = 0; i < getTileAmount(); i++){
            int x = (i % getTilesAcross());
            int y = (i / getTilesAcross());

            Rectangle rectangle = new Rectangle(x * getGridSize(),y * getGridSize(),getGridSize(),getGridSize());

            if((x + y) % 2 == 0){
                rectangle.setFill(waterImagePattern);
            } else {
                rectangle.setFill(waterDarkerImagePattern);
            }
            getAnchorPane().getChildren().add(rectangle);
        }
    }
}