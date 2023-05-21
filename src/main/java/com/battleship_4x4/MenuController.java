package com.battleship_4x4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MenuController
{
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
}