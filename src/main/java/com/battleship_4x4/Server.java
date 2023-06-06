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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

class ServerBackend {

    private final ServerSocket server;
    private final ArrayList<Socket> client;
    private final ArrayList<String> clientName;
    private final ArrayList<DataInputStream> clientInput;
    private final ArrayList<DataOutputStream> clientOutput;

    /**
     * Constructor of ServerBackend creating server socket
     * @param port Server Port
     * @param players Maximum number of players that can connect to the Server
     * @param IP Server IP Address
     * @throws IOException Error
     */
    public ServerBackend(int port, int players, Inet4Address IP) throws IOException {
        this.server = new ServerSocket(port, players, IP);
        this.client = new ArrayList<>();
        this.clientName = new ArrayList<>();
        this.clientInput = new ArrayList<>();
        this.clientOutput = new ArrayList<>();
        System.out.println("Server is ON!");
    }

    /**
     * Function that creates connection with Client, getting clientName and sending userID
     * @param id Current Client ID
     * @throws IOException Error
     */
    public void establishingConnection(int id) throws IOException {
        client.add(id, server.accept());
        clientInput.add(id, new DataInputStream(new BufferedInputStream(client.get(id).getInputStream())));
        clientOutput.add(id, new DataOutputStream(client.get(id).getOutputStream()));
        clientName.add(id, clientInput.get(id).readUTF());
        clientOutput.get(id).writeInt(id);
    }

    /**
     * Function to send message to Client
     * @param id Current Client ID
     * @param data Integer that will be sent to Client
     * @throws IOException Error
     */
    public void sendData(int id, int data) throws IOException {
        clientOutput.get(id).writeInt(data);
    }

    /**
     * Function to read message from Client
     * @param id Current Client ID
     * @return Integer sent by Client
     * @throws IOException Error
     */
    public int getData(int id) throws IOException {
        return clientInput.get(id).readInt();
    }

    /**
     * Function returning clientName of Client
     * @param id Current Client ID
     * @return Client Name
     */
    public String getClientName(int id) {
        return clientName.get(id);
    }

    /**
     * Function closing connection with Clients and closing Server socket
     * @throws IOException Error
     */
    public void closeServer() throws IOException {
        for(int i=0; i<client.size(); i++) {
            clientInput.get(i).close();
            clientOutput.get(i).close();
            client.get(i).close();
        }
        server.close();
    }
}

public class Server extends Application implements Runnable{

    private final int MAX_PLAYERS = 4;
    private int playersConnected = 0;
    private ServerBackend server;
    public final int PORT = 5000;

    @FXML
    private AnchorPane serverMainView;

    @FXML
    private Label gameStatus;

    @FXML
    private Label playersConnectedLabel;

    @FXML
    private AnchorPane serverStartView;

    @FXML
    private TextField serverIPTextField;

    @FXML
    private Label badServerIP;

    /**
     * Function changing starting server view to main server view
     * @throws IOException Error
     */
    void waitingForPlayers() throws IOException {
        serverStartView.setVisible(false);
        serverMainView.setVisible(true);

        gameStatus.setText("Waiting for players");
        updateConnectedPlayers();
    }

    /**
     * Function updating the number of connected players to the Server
     */
    public void updateConnectedPlayers() {
        playersConnectedLabel.setText(playersConnected + "/" + MAX_PLAYERS + " players connected");
    }

    /**
     * Function checking if serverIPTextField isn't empty and calling ServerBackend constructor
     */
    @FXML
    public void startServer() {
        badServerIP.setText("");

        try {
            Inet4Address serverIP = (Inet4Address) Inet4Address.getByName(serverIPTextField.getText());

            server = new ServerBackend(PORT, MAX_PLAYERS, (Inet4Address) Inet4Address.getByName(null));
            //server = new ServerBackend(PORT, MAX_PLAYERS, serverIP);

            waitingForPlayers();

            Thread serverThread = new Thread(this);
            serverThread.start();
        }
        catch(UnknownHostException err) {
            badServerIP.setText("Something is wrong with the server IP. Try again!");
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    /**
     * Launching Java FX app
     * @param stage Stage
     * @throws IOException Error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("server.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 320);
        stage.setTitle("Battleship 4x4 Server");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closing Java FX app and closing ServerBackend
     * @throws IOException Error
     */
    @Override
    public void stop() throws IOException {
        System.out.println("Closing Server");

        server.closeServer();

        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Main game loop on the Server side
     */
    @Override
    public void run() {
        while (playersConnected != 4) {
            try {
                server.establishingConnection(playersConnected);
                System.out.println("Client connected: " + server.getClientName(playersConnected));
                playersConnected++;

                for(int i=0; i<playersConnected; i++) {
                    server.sendData(i, 0);
                    server.sendData(i, playersConnected);
                    server.sendData(i, MAX_PLAYERS);
                }
                Platform.runLater(this::updateConnectedPlayers);
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        }

        for(int i=0; i<MAX_PLAYERS; i++) {
            try {
                server.sendData(i ,1);
                server.sendData(i, playersConnected);
                server.sendData(i, MAX_PLAYERS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
