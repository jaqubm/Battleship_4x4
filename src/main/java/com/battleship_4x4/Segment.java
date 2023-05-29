package com.battleship_4x4;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.battleship_4x4.Setup.TILE_SIZE;

public class Segment extends StackPane {
    //private final ShipType type;
    private double mouseX, mouseY;
    private double oldX, oldY; //previous position of the piece
    // do rozpatrzenia, jesli segment ma wiedziec jakim jest statkiem
    /*public ShipType getType() {
        return this.type;
    }*/

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Segment(int x, int y) { //segment constructor
        //this.type = type;

        move(x, y); //now new position of the segment

        Rectangle segment = new Rectangle(TILE_SIZE * 0.3, TILE_SIZE * 0.3);
        segment.setFill( Color.WHITESMOKE);
        segment.setStroke(Color.SADDLEBROWN);
        segment.setStrokeWidth(TILE_SIZE * 0.03);
        segment.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3 * 2) / 2);
        segment.setTranslateY((TILE_SIZE - TILE_SIZE * 0.3 * 2) / 2);


        getChildren().add(segment);

        setOnMousePressed(e ->
        { //tracking where the mouse is
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }



    public void move (int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;

        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }
}