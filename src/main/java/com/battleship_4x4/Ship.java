package com.battleship_4x4;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ship
{
    private final Rectangle rectangle;
    private double boardX;
    private double boardY;
    public List<Point2D> pointList;
    private final int size;
    private boolean direction;
    private final int GridSize;
    Image ship2Image = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_2_64.png")).toString());
    Image ship3Image = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_3_64.png")).toString());
    Image ship4Image = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_4_64.png")).toString());
    Image ship5Image = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/ships/ship_5_64.png")).toString());

    public Ship(int rectangleSize, int ship_size) {

        this.size = ship_size;
        this.direction = true;

        rectangle = new Rectangle(0, 0, rectangleSize * ship_size, rectangleSize); //każdy statek jest tworzony na 0 0, a potem przerzucany na inną pozytcję metodą move

        if (ship_size == 2)
            rectangle.setFill(new ImagePattern(ship2Image));
        else if (ship_size == 3)
            rectangle.setFill(new ImagePattern(ship3Image));
        else if (ship_size == 4)
            rectangle.setFill(new ImagePattern(ship4Image));
        else
            rectangle.setFill(new ImagePattern(ship5Image));

        this.boardX = 0;
        this.boardY = 0;
        this.GridSize = rectangleSize;

        pointList = new ArrayList<>();
        for (int i = 0; i < ship_size; i++)
            pointList.add(new Point2D(i,0));
    }

    public void move (int x, int y) {
        boardX = x * GridSize;
        boardY = y * GridSize;

        for (int i = 0; i < size; i++) {
            if (direction) pointList.set(i, new Point2D(x + i , y));
            else pointList.set(i, new Point2D(x , y - i));
        }

        rectangle.relocate(boardX, boardY);
    }

    public int getX() {
        return (int)(boardX / GridSize);
    }

    public int getY() {
        return (int)(boardY / GridSize);
    }

    int toBoard(double pixel) { //zamiana piksela na współrzędną siatki
        return (int) (pixel + GridSize / 2) / GridSize;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
    public int getSize(){return size;}
    public boolean getDirection(){return this.direction;}
    public void setDirection(boolean dir) {this.direction = dir;}

    public double getBoardY() {return this.boardY;}
    public double getBoardX() {return this.boardX;}
}
