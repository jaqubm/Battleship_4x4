package com.battleship_4x4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;

public class MainMenu extends Application {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField IPTextField;

    @FXML
    protected void onHostButtonClick() {
        System.out.println("Host button has been clicked");
    }

    @FXML
    protected void onJoinButtonClick() {
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

                    Client client = new Client(username, ipAddress);
                    System.out.println("Connected");
                } catch(IOException err) {
                    errorLabel.setText("Something is wrong with IP");
                }
            }
        }
    }

    public void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Battleship 4x4");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}