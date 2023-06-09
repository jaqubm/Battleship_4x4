package com.battleship_4x4;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class GridHandler extends GridBase {

    public GridHandler(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        super(planeWidth, planeHeight, gridSize, anchorPane);
    }

    public Rectangle createRectangle(ImagePattern waterImagePattern, ImagePattern waterDarkerImagePattern, int i) {
        int x = (i % getTilesAcross());
        int y = (i / getTilesAcross());

        Rectangle rectangle = new Rectangle(x * getGridSize(),y * getGridSize(),getGridSize(),getGridSize());

        if((x + y) % 2 == 0){
            rectangle.setFill(waterImagePattern);
        } else {
            rectangle.setFill(waterDarkerImagePattern);
        }

        return rectangle;
    }

    public void createSetupGrid(Image waterImage, Image waterDarkerImage) {
        ImagePattern waterImagePattern = new ImagePattern(waterImage);
        ImagePattern waterDarkerImagePattern = new ImagePattern(waterDarkerImage);

        for(int i = 0; i < getTileAmount(); i++){
            Rectangle rectangle = createRectangle(waterImagePattern, waterDarkerImagePattern, i);
            getAnchorPane().getChildren().add(rectangle);
        }
    }

    public void createGameGrid(Image waterImage, Image waterDarkerImage, int quarter, Game game) {
        ImagePattern waterImagePattern = new ImagePattern(waterImage);
        ImagePattern waterDarkerImagePattern = new ImagePattern(waterDarkerImage);

        for(int i = 0; i < getTileAmount(); i++){
            Rectangle rectangle = createRectangle(waterImagePattern, waterDarkerImagePattern, i);
            getAnchorPane().getChildren().add(rectangle);

            int x = (i % getTilesAcross());
            int y = (i / getTilesAcross());

            rectangle.setOnMouseClicked((MouseEvent event) -> {
                try {
                    game.handleGridClick(quarter, (y * 8) + x);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void fillRectangle(int id, Image image) {
        ImagePattern imagePattern = new ImagePattern(image);

        int x = (id % getTilesAcross());
        int y = (id / getTilesAcross());

        Rectangle rectangle = new Rectangle(x * getGridSize(),y * getGridSize(),getGridSize(),getGridSize());
        rectangle.setFill(imagePattern);

        getAnchorPane().getChildren().set(id, rectangle);
    }

    private int toBoard(double pixel) { //zamiana piksela na współrzędną siatki
        return (int) (pixel + 64 / 2) / 64;
}
}

