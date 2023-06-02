package com.battleship_4x4;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Ship
{
    private Rectangle rectangle;
    private double boardX;
    private double boardY;
    public List<Point2D> pointList;
    private int size;
    private boolean direction;
    Image ship2Image = new Image("com.battleship_4x4/sprites/ships/ship_2_64_png");
}
