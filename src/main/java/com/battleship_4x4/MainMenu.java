package com.battleship_4x4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * MainMenu Class is main-menu.fxml controller
 */
public class MainMenu extends Application
{

    public static final int TILE_SIZE = 64;
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
    @FXML
    private AnchorPane waitingForPlayers;

    @FXML
    private Label playersConnectedLabel;

    @FXML
    private AnchorPane mainMenu;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField IPTextField;

    /**
     * Function checking if TextFields aren't empty and calling Client constructor
     */
    public void onJoinButtonClick() {
        errorLabel.setText("");

        if(userNameTextField.getText().equals("")) {
            errorLabel.setText("Username can't be empty!");
        }
        else {
            if(IPTextField.getText().equals("")) {
                errorLabel.setText("IP can't be empty!");
            }
            else {
                try {
                    String username= userNameTextField.getText();
                    Inet4Address ipAddress= (Inet4Address) Inet4Address.getByName(IPTextField.getText());

                    new Client(username, ipAddress, this);
                    System.out.println("Connected");
                } catch(IOException err) {
                    errorLabel.setText("Something is wrong with IP");
                }
            }
        }
    }

    /**
     * Function launching Server app
     */
    public void onHostButtonClick() {
        Platform.runLater(() -> {
            try {
                new Server().start(new Stage());
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        });
    }

    /**
     * Function to close this app
     */
    public void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Function updating the number of connected players to the Server
     * @param playersConnected Integer telling how much players are connected to the Server
     * @param maxPlayers Integer telling what is maximum player number on the Server
     */
    public void updateConnectedPlayers(int playersConnected, int maxPlayers) {
        playersConnectedLabel.setText(playersConnected + "/" + maxPlayers + " players connected");
    }

    /**
     * Changing currently visible state of MainMenu
     */
    public void switchToWaitingForPlayers() {
        mainMenu.setVisible(false);
        waitingForPlayers.setVisible(true);
    }

    /**
     * Launching Java FX app
     * @param stage Stage
     * @throws IOException Error
     */
    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Battleship 4x4");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();*/
        Scene setupScene = new Scene(createContent(),800, 800);
        stage.setScene(setupScene);

        stage.show();
    }

    /**
     * Closing Java FX app
     * @throws IOException Error
     */
    @Override
    public void stop() throws IOException {
        System.out.println("Closing Client");

        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}