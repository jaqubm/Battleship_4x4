package com.battleship_4x4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class DraggableMakerGrid extends GridBase{

    private double mouseAnchorX;
    private double mouseAnchorY;
    private boolean allowRotation = true;
    Image ship2red = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_2_red_64.png")).toString());
    Image ship3red = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_3_red_64.png")).toString());
    Image ship4red = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_4_red_64.png")).toString());
    Image ship5red = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_5_red_64.png")).toString());

    public DraggableMakerGrid(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        super(planeWidth, planeHeight, gridSize, anchorPane);
    }

    public void makeDraggable(Ship ship, List<Ship> ships){
        Node node = ship.getRectangle();

        node.setOnMousePressed(e -> {
            mouseAnchorX = e.getSceneX();
            mouseAnchorY = e.getSceneY();
            allowRotation = false;
        });
        node.setOnMouseDragged(e -> {
            node.relocate(e.getSceneX() - mouseAnchorX + ship.getBoardX(),
                    e.getSceneY() - mouseAnchorY + ship.getBoardY());
        });
        node.setOnMouseReleased(e -> {
            int newX = toBoard(node.getLayoutX()); //getLayout a nie getScene żeby uzależnić to od położenia statku, a nie myszki
            int newY = toBoard(node.getLayoutY());

            int x0 = toBoard(ship.getBoardX());
            int y0 = toBoard(ship.getBoardY());

            ship.move(newX, newY);
            if (collisionCheck(ship, ships) || !inBoardCheck(ship))
            {
                ship.move(x0, y0);
                error(ship);
            }

            allowRotation = true;
        });

        node.setOnScroll(e -> {
            if (allowRotation)
            {
                Rotate rotate;
                if (ship.getDirection()) rotate = new Rotate(-90, 32, 32);
                else rotate = new Rotate(90, 32, 32);

                ship.setDirection(!ship.getDirection());
                ship.move(toBoard(node.getLayoutX()),toBoard(node.getLayoutY())); //move w miejscu aby zaktualizować współrzędne statku po obrocie

                if (!collisionCheck(ship, ships) && inBoardCheck(ship)) node.getTransforms().add(rotate);
                else {
                    ship.setDirection(!ship.getDirection());
                    ship.move(toBoard(node.getLayoutX()),toBoard(node.getLayoutY()));
                    error(ship);
                }
            }
        });

    }

    public boolean collisionCheck(Ship movingShip, List<Ship> ships) {
        for (Ship temp: ships) {
            if (temp == movingShip) continue;
            if (overlaps(temp, movingShip)) return true;
        }
        return false;
    }

    public boolean overlaps(Ship s1, Ship s2) {
        for (int i = 0; i < s1.getSize(); i++)
        {
            for (int j = 0; j < s2.getSize(); j++)
            {
                if (s1.pointList.get(i).getX() == s2.pointList.get(j).getX() &&
                        s1.pointList.get(i).getY() == s2.pointList.get(j).getY()) return true;
            }
        }
        return false;
    }

    public boolean inBoardCheck(Ship ship){
        for (int i = 0; i < ship.getSize(); i++)
        {
            if ((ship.pointList.get(i).getX() > 7 || ship.pointList.get(i).getX() < 0) ||
                    (ship.pointList.get(i).getY() > 7 || ship.pointList.get(i).getY() < 0)) return false;
        }
        return true;
    }

    public void error(Ship ship) {
        if (ship.getSize() == 2) ship.getRectangle().setFill(new ImagePattern(ship2red));
        if (ship.getSize() == 3) ship.getRectangle().setFill(new ImagePattern(ship3red));
        if (ship.getSize() == 4) ship.getRectangle().setFill(new ImagePattern(ship4red));
        if (ship.getSize() == 5) ship.getRectangle().setFill(new ImagePattern(ship5red));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3)));
        timeline.setOnFinished(e2 -> {
            if (ship.getSize() == 2) ship.getRectangle().setFill(new ImagePattern(ship.ship2Image));
            if (ship.getSize() == 3) ship.getRectangle().setFill(new ImagePattern(ship.ship3Image));
            if (ship.getSize() == 4) ship.getRectangle().setFill(new ImagePattern(ship.ship4Image));
            if (ship.getSize() == 5) ship.getRectangle().setFill(new ImagePattern(ship.ship5Image));
        });
        timeline.play();
    }

    private int toBoard(double pixel) { //zamiana piksela na współrzędną siatki
        return (int) (pixel + 64 / 2) / 64;
    }
}