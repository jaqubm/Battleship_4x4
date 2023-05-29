package com.battleship_4x4;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Setup extends Application
{
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private final Group tileGroup = new Group();
    private final Group shipGroup = new Group();

    private final Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Parent createContent()
    {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, shipGroup);
        for (int y = 0; y < HEIGHT; y++) //renders the grid and sets the pieces
        {
            for (int x = 0; x < WIDTH; x++)
            {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                if(x<5 && y<5)
                {
                    Ship ship = makeSegment(x, y);
                    tile.setShip(ship);
                    shipGroup.getChildren().add(ship);
                }
            }
        }

        return root;
    }
    private int toBoard(double pixel) { //converts from pixel coordinates to board coordinates
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
    private boolean tryMove( int newX, int newY)
    {
        return !board[newX][newY].hasShip() ;
    }
    private Ship makeSegment(int x, int y)
    {
        Ship ship=new Ship(x,y);
        ship.setOnMouseReleased(e ->
        {
            int x0=toBoard(ship.getOldX());
            int y0=toBoard(ship.getOldY());
            int newX = toBoard(ship.getLayoutX());
            int newY = toBoard(ship.getLayoutY());
            boolean check = tryMove( newX, newY);

            if(check)
            {
                board[x0][y0].setShip(null);
                board[newX][newY].setShip(ship);
                ship.move(newX,newY);
            }
            else
            {
                ship.abortMove();
            }
        });
        return ship;
    };
    @Override
    public void start(Stage stage) throws IOException
    {
        Scene setupScene = new Scene(createContent(),800, 800);
        stage.setScene(setupScene);
        stage.show();
    }
}
