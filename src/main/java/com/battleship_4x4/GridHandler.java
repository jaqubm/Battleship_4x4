package com.battleship_4x4;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class GridHandler extends GridBase {

    Image waterImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_64.gif")).toString());
    Image waterDarkerImage = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/water_darker_64.gif")).toString());
    private int quarter;

    public GridHandler(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        super(planeWidth, planeHeight, gridSize, anchorPane);
    }

    public void createGrid(int quarter) {
        this.quarter = quarter;
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

            rectangle.setOnMouseClicked((MouseEvent event) -> {

                handleGridClick(y, x,quarter);

            });
        }
    }

    private int toBoard(double pixel) { //zamiana piksela na współrzędną siatki
        return (int) (pixel + 64 / 2) / 64;
}
    private void handleGridClick(int row, int column,int quarter) {
        // Logika obsługi kliknięcia w siatkę
        System.out.println("Clicked cell: row=" + row + ", column=" + column + " quarter" + quarter);
    }
}

