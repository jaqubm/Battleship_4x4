package com.battleship_4x4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * MainMenu Class is main-menu.fxml controller
 */
public class MainMenu extends Application {

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
}