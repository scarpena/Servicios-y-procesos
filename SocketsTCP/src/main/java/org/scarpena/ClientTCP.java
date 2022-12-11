package org.scarpena;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) {

        final int PUERTO = 5555;
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner myScanner;
        String echoMessage;
        String echoReturned;
        boolean powerOn;

        try {
            powerOn = true;
            myScanner = new Scanner(System.in);
            socket = new Socket("localhost", PUERTO);
            System.out.println("Client started");

            while (powerOn) {
                oos = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Write the message you want to send: ");
                echoMessage = myScanner.nextLine();
                oos.writeObject(echoMessage);

                ois = new ObjectInputStream(socket.getInputStream());
                echoReturned = (ois.readObject()).toString();
                System.out.println(echoReturned);

                System.out.println("Do you want to send a new message (y/n)?");
                if (myScanner.nextLine().equalsIgnoreCase("n")) {
                    powerOn = false;
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject("endClient");
                }
            }
            System.out.println("Client end");
            socket.close();
            oos.close();
            ois.close();
            myScanner.close();


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

