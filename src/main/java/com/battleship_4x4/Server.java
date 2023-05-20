package com.battleship_4x4;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerBackend implements Runnable {

    private Socket socket;
    private ServerSocket server;
    private DataInputStream[] in;
    private DataOutputStream[] out;

    private boolean serverWork;

    public ServerBackend(int port) throws IOException {
        server = new ServerSocket(port);
        serverWork = true;
    }

    public void establishingConnection(int id) {
        try {
            socket = server.accept();
            in[id] = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out[id] = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(serverWork) {

        }
    }

    /*public static void main(String[] args) throws IOException {
        Thread serverRunning = new Thread(server);
        serverRunning.start();
    }*/
}

public class Server extends Application {

    private ServerBackend server;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Label badServerPort;

    @FXML
    public void startServer() {
        badServerPort.setText("");

        try {
            int serverPort = Integer.parseInt(serverPortTextField.getText());
            server = new ServerBackend(serverPort);
        }
        catch(NumberFormatException err) {
            badServerPort.setText("Something is wrong with the server port. Try again!");
        }
        catch(IOException err) {
            badServerPort.setText("Wrong server port. Try again!");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Battleship 4x4 Server");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
