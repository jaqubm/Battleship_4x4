package com.battleship_4x4;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Ship
{
    private final Rectangle rectangle;
    private double boardX;
    private double boardY;
    public List<Point2D> pointList;
    private final int size;
    private boolean direction;
    private final int GridSize;
    //Image ship2Image = new Image("com/battleship_4x4/sprites/ships/ship_2_64.png");
    //Image ship3Image = new Image("com/battleship_4x4/sprites/ships/ship_3_64.png");
    //Image ship4Image = new Image("com/battleship_4x4/sprites/ships/ship_4_64.png");
    //Image ship5Image = new Image("com/battleship_4x4/sprites/ships/ship_5_64.png");

    public Ship(int rectangleSize, int ship_size) {

        this.size = ship_size;
        this.direction = true;
        rectangle = new Rectangle(0, 0, rectangleSize * ship_size, rectangleSize); //każdy statek jest tworzony na 0 0, a potem przerzucany na inną pozytcję metodą move
        if (ship_size == 2) {
            //rectangle.setFill(new ImagePattern(ship2Image));
            rectangle.setFill(Color.BLACK);
        }
        else if (ship_size == 3) {
            //rectangle.setFill(new ImagePattern(ship3Image));
            rectangle.setFill(Color.BLACK);
        }
        else if (ship_size == 4) {
            //rectangle.setFill(new ImagePattern(ship4Image));
            rectangle.setFill(Color.BLACK);
        }
        else {
            //rectangle.setFill(new ImagePattern(ship5Image));
            rectangle.setFill(Color.BLACK);
        }
        this.boardX = 0;
        this.boardY = 0;
        pointList = new ArrayList<>();
        for (int i = 0; i < ship_size; i++)
        {
            pointList.add(new Point2D(i,0));
        }
        this.GridSize = rectangleSize;
    }

    public void move (int x, int y) {
        boardX = x * GridSize;
        boardY = y * GridSize;

        for (int i = 0; i < size; i++)
        {
            if (direction) pointList.set(i, new Point2D(x + i , y));
            else pointList.set(i, new Point2D(x , y - i));
        }

        rectangle.relocate(boardX, boardY);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
    public int getSize(){return size;}
    public boolean getDirection(){return this.direction;}
    public void setDirection(boolean dir) {this.direction = dir;}

    public void setBoardX(double x) {this.boardX = x;}
    public void setBoardY(double y) {this.boardY = y;}

    public double getboardY() {return this.boardY;}
    public double getboardX() {return this.boardX;}
}
