package com.battleship_4x4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.Objects;

/**
 * MainMenu Class is main-menu.fxml controller
 */
public class MainMenu extends Application implements Runnable {

    Client client;

    ActionEvent event;

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
    public void onJoinButtonClick(ActionEvent event) {
        errorLabel.setText("");

        boolean test = true;

        if(userNameTextField.getText().equals("") && !test) {
            errorLabel.setText("Username can't be empty!");
        }
        else {
            if(IPTextField.getText().equals("") && !test) {
                errorLabel.setText("IP can't be empty!");
            }
            else {
                try {
                    String username= userNameTextField.getText();
                    Inet4Address ipAddress= (Inet4Address) Inet4Address.getByName(IPTextField.getText());

                    client = new Client(username, ipAddress);
                    System.out.println("Connected");

                    switchToWaitingForPlayers();
                    this.event = event;

                    Thread mainMenuThread = new Thread(this);
                    mainMenuThread.start();

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

    public void switchToSetup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ship-setup.fxml"));
        Parent root = loader.load();

        ShipSetup shipSetup = loader.getController();
        shipSetup.setClient(client);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launching Java FX app
     * @param stage Stage
     * @throws IOException Error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Battleship 4x4");
        stage.setResizable(false);
        stage.setScene(scene);
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

    /**
     * Main menu loop while waiting for all players to connect
     */
    @Override
    public void run() {
        while(true) {
            try {
                //gameID = 0 - Update number of connected players
                //gameID = 1 - Switch to the ShipSetup scene
                int gameID = client.getData();
                int playerID = client.getData();
                int posID = client.getData();

                if(gameID == 0) {
                    Platform.runLater(() -> updateConnectedPlayers(playerID, posID));
                }
                else if(gameID == 1) {
                    Platform.runLater(() -> {
                        try {
                            switchToSetup();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (IOException err) {
                try {
                    client.closeConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    try {
                        stop();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
        }
    }
}