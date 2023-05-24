package com.battleship_4x4;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class ServerBackend {

    private final ServerSocket server;
    private final Socket[] client;
    private final String[] clientName;
    private final DataInputStream[] clientInput;
    private final DataOutputStream[] clientOutput;

    public ServerBackend(int port, int players, Inet4Address IP) throws IOException {
        server = new ServerSocket(port, players, IP);
        client = new Socket[players];
        clientName = new String[players];
        clientInput = new DataInputStream[players];
        clientOutput = new DataOutputStream[players];
        System.out.println("Server is ON!");
    }

    public void establishingConnection(int id) {
        try {
            client[id] = server.accept();
            clientInput[id] = new DataInputStream(new BufferedInputStream(client[id].getInputStream()));
            clientOutput[id] = new DataOutputStream(client[id].getOutputStream());
            clientName[id] = clientInput[id].readUTF();
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }
}

public class Server extends Application {

    private int MAX_PLAYERS = 4;
    private ServerBackend server;

    @FXML
    private AnchorPane serverMainView;

    @FXML
    private Label gameStatus;

    @FXML
    private AnchorPane serverStartView;

    @FXML
    private TextField serverIPTextField;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Label badServerPort;

    void waitingForPlayers() {
        gameStatus.setText("Waiting for players");
    }

    @FXML
    public void startServer() {
        badServerPort.setText("");

        try {
            Inet4Address serverIP = (Inet4Address) Inet4Address.getByName(serverIPTextField.getText());
            try {
                int serverPort = Integer.parseInt(serverPortTextField.getText());

                server = new ServerBackend(serverPort, MAX_PLAYERS, serverIP);

                serverStartView.setVisible(false);
                serverMainView.setVisible(true);

                waitingForPlayers();
            }
            catch(NumberFormatException err) {
                badServerPort.setText("Something is wrong with the server port. Try again!");
            }
            catch(IOException err) {
                badServerPort.setText("Wrong server port. Try again!");
            }
        }
        catch(UnknownHostException err) {
            badServerPort.setText("Something is wrong with the server IP. Try again!");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("server.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 320);
        stage.setTitle("Battleship 4x4 Server");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
