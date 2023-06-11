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

    public void sendName(int id, String name) throws IOException {
        clientOutput.get(id).writeUTF(name);
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

    private final int MAX_PLAYERS = 2;
    private int playersConnected = 0;
    private ServerBackend server;

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

            int PORT = 5000;
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
        System.out.println("Server: Closing Server");

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
        //Waiting for players to connect
        while (playersConnected != MAX_PLAYERS) {
            try {
                server.establishingConnection(playersConnected);
                System.out.println("Server: Client connected: " + server.getClientName(playersConnected));
                playersConnected++;

                for(int i=0; i<playersConnected; i++) {
                    //Sending all connected players updates about how many players are connected
                    server.sendData(i, 0);
                    server.sendData(i, playersConnected);
                    server.sendData(i, MAX_PLAYERS);
                }
                Platform.runLater(this::updateConnectedPlayers);
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        }

        //Sending all players new game state - all players are connected to the server
        for(int i=0; i<MAX_PLAYERS; i++) {
            try {
                server.sendData(i ,1);
                server.sendData(i, playersConnected);
                server.sendData(i, MAX_PLAYERS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Map description
        //0 - empty
        //1 - contain ship
        //2 - contain missed shot
        //3 - contain destroyed ship
        ArrayList<ArrayList<Integer>> map = new ArrayList<>(MAX_PLAYERS);

        //Waiting for players to be ready and reading their ships positions
        int MAP_SIZE = 64;
        for(int i = 0; i<MAX_PLAYERS; i++) {
            map.add(new ArrayList<>(MAP_SIZE));
            for(int j = 0; j< MAP_SIZE; j++) {
                map.get(i).add(j, 0);
            }

            try {
                for(int j=0; j<14; j++) {
                    int data = server.getData(i);
                    map.get(i).set(data, 1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Showing maps to test
        for(int i=0; i<MAX_PLAYERS; i++) {
            System.out.print("\nMap of player ID: " + i);
            for(int j = 0; j< MAP_SIZE; j++) {
                if(j % 8 == 0)
                    System.out.println();
                System.out.print(map.get(i).get(j));
            }
            System.out.println();
        }

        ArrayList<Boolean> playerLost = new ArrayList<>();

        //Sending all players new game state - all players are ready
        for(int i=0; i<MAX_PLAYERS; i++) {
            try {
                server.sendData(i, 0);
                for(int j=0; j<MAX_PLAYERS; j++)
                    server.sendName(i, server.getClientName(j));

                playerLost.add(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        int round = (int) (Math.random() * 10) % MAX_PLAYERS;
        System.out.println("Server: Player ID starting round: " + round);

        //Main game loop on the server side
        while(true) {
            //Checking if there are any ships remaining
            int playersRemaining = 0;
            for(int i=0; i<MAX_PLAYERS; i++) {
                if(!(playerLost.get(i))) {
                    boolean playerPlay = false;
                    for(int j = 0; j< MAP_SIZE; j++) {
                        if(map.get(i).get(j) == 1) {
                            playerPlay = true;
                            break;
                        }
                    }

                    if(!playerPlay)
                        playerLost.set(i, true);
                    else
                        playersRemaining++;
                }
            }

            //Ending main game loop on the server side if there is only one player left
            if(playersRemaining <= 1)
                break;

            //Updating current player round if current player already lost a game
            if(playerLost.get(round)) {
                round = (round + 1) % MAX_PLAYERS;
                continue;
            }

            //Sending information to all players about whose round it is
            try {
                for(int i=0; i<MAX_PLAYERS; i++)
                    server.sendData(i, round);

                int quarter = server.getData(round);

                if(quarter == -1) {
                    for(int i=0; i<MAX_PLAYERS; i++)
                        server.sendData(i, 4);

                    round = (round + 1) % MAX_PLAYERS;

                    continue;
                }

                int pos = server.getData(round);
                quarter = (quarter + round) % MAX_PLAYERS;
                System.out.println("Server: Quarter: " + quarter + " Pos: " + pos);

                boolean mapUpdate = false;
                if(map.get(quarter).get(pos) == 0) {    //Missed shot
                    map.get(quarter).set(pos, 2);
                    mapUpdate = true;

                    round = (round + 1) % MAX_PLAYERS;  //Updating current player round only when he missed shot
                }
                else if(map.get(quarter).get(pos) == 1) {   //Ship taken down
                    map.get(quarter).set(pos, 3);
                    mapUpdate = true;
                }

                for(int i=0; i<MAX_PLAYERS; i++) {  //Sending all players new map update
                    if(mapUpdate) {
                        int curQuarter = (quarter - i + MAX_PLAYERS) % MAX_PLAYERS;
                        server.sendData(i, curQuarter);
                        server.sendData(i, pos);
                        server.sendData(i, map.get(quarter).get(pos));
                    }
                    else    //Sending info about no updates of the map
                        server.sendData(i, 4);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Sending all players new game state - end of the game
        for(int i=0; i<MAX_PLAYERS; i++) {
            try {
                server.sendData(i, -1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
