package com.battleship_4x4;

import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

public class Client implements Runnable {

    private int clientID;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    private MainMenu mainMenu;

    Client(String name, Inet4Address addressIP, MainMenu mainMenu) throws IOException {
        try {
            this.socket = new Socket(addressIP, 5000);

            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(socket.getOutputStream());

            sendData(name);
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

    public void closeConnection() throws IOException {
        input.close();
        output.close();

        socket.close();
    }

    public void sendData(String data) throws IOException {
        output.writeUTF(data);
    }

    public int getData() throws IOException {
        return input.readInt();
    }

    @Override
    public void run() {
        while(true) {
            try {
                int gameID = getData();
                int playerID = getData();
                int posID = getData();

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
