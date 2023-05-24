package com.battleship_4x4;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

public class Client {

    private String name;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    Client(String name, Inet4Address addressIP) {
        try {
            socket = new Socket(addressIP, 5000);
            System.out.println("Connected");

            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());

            this.name = name;
            sendData(this.name);
        } catch (IOException err) {
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
}
