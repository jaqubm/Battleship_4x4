package com.battleship_4x4;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * Client Class to take care of communication with Server and implementing game logic
 */
public class Client {

    private int clientID;
    private String clientName;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    /**
     * Constructor of Client Class creating connection with Server and DataStreams to contact with it
     * @param name String containing clientName
     * @param addressIP Inet4Address containing IP address to which it will try to connect
     * @throws IOException Error
     */
    Client(String name, Inet4Address addressIP) throws IOException {
        try {
            this.socket = new Socket(Inet4Address.getByName(null), 5000);
            //this.socket = new Socket(addressIP, 5000);

            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(socket.getOutputStream());
            this.clientName = name;

            this.output.writeUTF(this.clientName);
            this.clientID = getData();

            System.out.println("Player ID: " + clientID);
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

    public String getName() throws IOException {
        return input.readUTF();
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

    public int getClientID() {
        return clientID;
    }

    public String getClientName() {
        return clientName;
    }
}
