package com.battleship_4x4;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;

import java.util.List;

public class DraggableMakerGrid extends GridBase{

    private double mouseAnchorX;
    private double mouseAnchorY;

    public DraggableMakerGrid(double planeWidth, double planeHeight, int gridSize, AnchorPane anchorPane) {
        super(planeWidth, planeHeight, gridSize, anchorPane);
    }

    public void makeDraggable(Ship ship, List<Ship> ships){
        Node node = ship.getRectangle();

        node.setOnMousePressed(e -> {
            mouseAnchorX = e.getSceneX();
            mouseAnchorY = e.getSceneY();
        });
        node.setOnMouseDragged(e -> {
            node.relocate(e.getSceneX() - mouseAnchorX + ship.getboardX(),
                    e.getSceneY() - mouseAnchorY + ship.getboardY());
        });
        node.setOnMouseReleased(e -> {
            int newX = toBoard(node.getLayoutX()); //getLayout a nie getScene żeby uzależnić to od położenia statku, a nie myszki
            int newY = toBoard(node.getLayoutY());

            int x0 = toBoard(ship.getboardX());
            int y0 = toBoard(ship.getboardY());

            ship.move(newX, newY);
            if (collisionCheck(ship, ships) || !inBoardCheck(ship)) ship.move(x0, y0);
        });

        node.setOnScroll(e -> {
            Rotate rotate;
            if (ship.getDirection()) rotate = new Rotate(-90, 32, 32);
            else rotate = new Rotate(90, 32, 32);

            ship.setDirection(!ship.getDirection());
            ship.move(toBoard(node.getLayoutX()),toBoard(node.getLayoutY())); //move w miejscu aby zaktualizować współrzędne statku po obrocie

            if (!collisionCheck(ship, ships) && inBoardCheck(ship)) node.getTransforms().add(rotate);
            else {
                ship.setDirection(!ship.getDirection());
                ship.move(toBoard(node.getLayoutX()),toBoard(node.getLayoutY()));
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

    private int toBoard(double pixel) { //zamiana piksela na współrzędną siatki
        return (int) (pixel + 64 / 2) / 64;
    }
}