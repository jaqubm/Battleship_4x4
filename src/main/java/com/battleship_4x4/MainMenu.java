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

public class MainMenu extends Application {

    @FXML
    private Label testText_1;
    @FXML
    private Label testText_2;
    @FXML
    private Label username_error;
    @FXML
    private Label ip_error;
    @FXML
    private TextField username_textfield;
    @FXML
    private TextField ip_textfield;
    @FXML
    protected void onHostButtonClick()
    {
        testText_1.setText("Host button was clicked!");
        testText_2.setText("");
    }
    @FXML
    protected void onJoinButtonClick()
    {
        testText_2.setText("Join button was clicked!");
        testText_1.setText("");
        String username=username_textfield.getText();
        if(username.equals(""))
        {
            username_error.setText("Username can't be empty!");
        }

        else
        {
            System.out.println(username);
            username_error.setText("");
        }
        String ip=ip_textfield.getText();
        if(ip.equals(""))
        {
            ip_error.setText("IP can't be empty!");
        }
        else
        {
            System.out.println(ip);
            ip_error.setText("");
        }
    }
    public void onExitButtonClick()
    {
        Platform.exit();
        System.exit(0);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Battleship_4x4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}