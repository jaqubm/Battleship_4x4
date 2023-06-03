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

            rectangle.setOnMouseClicked((MouseEvent event) -> {
                double mouseAnchorX = event.getSceneX();
                double mouseAnchorY = event.getSceneY();
                int x2=toBoard(mouseAnchorX);
                int y2=toBoard(mouseAnchorY);

                if(x2<6&&y2<6) {

                    handleGridClick(y, x,3);

                }else if (x2<14&&y2<6) {

                    handleGridClick(y, x,4);

                } else if (x2<6) {

                    handleGridClick(y, x,1);

                } else {

                    handleGridClick(y, x, 2);

                }

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

