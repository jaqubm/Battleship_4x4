package com.battleship_4x4;

import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * Client Class to take care of communication with Server and implementing game logic
 */
public class Client implements Runnable {

    private int clientID;
    private String clientName;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    private MainMenu mainMenu;

    /**
     * Constructor of Client Class creating connection with Server and DataStreams to contact with it
     * @param name String containing clientName
     * @param addressIP Inet4Address containing IP address to which it will try to connect
     * @param mainMenu MainMenu Class object to control and/or call functions from this class
     * @throws IOException Error
     */
    Client(String name, Inet4Address addressIP, MainMenu mainMenu) throws IOException {
        try {
            this.socket = new Socket(addressIP, 5000);

            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(socket.getOutputStream());
            this.clientName = name;

            this.output.writeUTF(this.clientName);
            this.clientID = getData();

            System.out.println("Player ID: " + clientID);

            this.mainMenu = mainMenu;
            this.mainMenu.switchToWaitingForPlayers();

            Thread clientThread = new Thread(this);
            clientThread.start();
        } catch (IOException err) {
            closeConnection();
            err.printStackTrace();
        }
    }

    /**
     * Function closing connection with Server
     * @throws IOException Error
     */
    public void closeConnection() throws IOException {
        input.close();
        output.close();

        socket.close();
    }

    /**
     * Function to send message to Server
     * @param data Integer that will be sent to Server
     * @throws IOException Error
     */
    public void sendData(int data) throws IOException {
        output.writeInt(data);
    }

    /**
     * Function to read message from Server
     * @return Integer sent by Server
     * @throws IOException Error
     */
    public int getData() throws IOException {
        return input.readInt();
    }

    /**
     * Main game loop on the Client side
     */
    @Override
    public void run() {
        while(true) {
            try {
                int gameID = getData();
                int playerID = getData();
                int posID = getData();

                //Change IF statements for SWITCH!
                //Implement another stages of game HERE!
                if(gameID == 0) {
                    Platform.runLater(() -> mainMenu.updateConnectedPlayers(playerID, posID));
                }
            } catch (IOException err) {
                try {
                    closeConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    try {
                        mainMenu.stop();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
        }
    }
}
