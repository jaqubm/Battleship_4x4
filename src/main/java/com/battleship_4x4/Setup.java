package com.battleship_4x4;

import javafx.application.Application;
import javafx.fxml.FXML;
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
    private final Group segmentGroup = new Group();

    private final Tile[][] board = new Tile[WIDTH][HEIGHT];
    @FXML
    private Pane TestPane;
    private Parent createContent()
    {
        TestPane = new Pane();
        TestPane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        TestPane.getChildren().addAll(tileGroup, segmentGroup);

        boolean spawned_ships=false;
        for (int y = 0; y < HEIGHT; y++) //renders the grid and sets the pieces
        {
            for (int x = 0; x < WIDTH; x++)
            {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                int SHIP_AMOUNT = 4;
                if(x== SHIP_AMOUNT)
                {
                    spawned_ships=true;
                }
                if(!spawned_ships)
                {
                    Ship ship = new Ship(2,x , y);
                    for (int i=0;i<2;i++)
                    {
                        ship.segmentList.get(i)=makeSegment(x,y);
                        //tile.setSegment(ship.segmentList.get(i));
                        segmentGroup.getChildren().add(ship.segmentList.get(i));
                    }

                }
            }
        }

        return TestPane;
    }
    private int toBoard(double pixel) { //converts from pixel coordinates to board coordinates
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
    private boolean tryMove( int newX, int newY)
    {
        return !board[newX][newY].hasSegment() ;
    }
    private Segment makeSegment(int x, int y)
    {
        Segment segment =new Segment(x,y);
        segment.setOnMouseReleased(e ->
        {
            int x0=toBoard(segment.getOldX());
            int y0=toBoard(segment.getOldY());
            int newX = toBoard(segment.getLayoutX());
            int newY = toBoard(segment.getLayoutY());
            boolean check = tryMove( newX, newY);

            if(check)
            {
                board[x0][y0].setSegment(null);
                board[newX][newY].setSegment(segment);
                segment.move(newX,newY);
            }
            else
            {
                segment.abortMove();
            }
        });
        return segment;
    };
    @Override
    public void start(Stage stage) throws IOException
    {
        //FXMLLoader fxmlLoader = new FXMLLoader(Setup.class.getResource("setup.fxml"));
        Scene setupScene = new Scene(createContent(), 800, 800);
        stage.setTitle("Setup");
        stage.setScene(setupScene);
        stage.show();

    }
}
