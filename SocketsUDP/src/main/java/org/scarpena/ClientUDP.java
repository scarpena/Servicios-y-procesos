package org.scarpena;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        byte[] buffer;
        ReceptionClient receptionClient;

        //final int serverPort = 5555;

        System.out.println("client started");
        try (DatagramSocket socket = new DatagramSocket()) {
            receptionClient = new ReceptionClient(socket);
            receptionClient.start();

            System.out.println("Enter the IP address where you want to send the package: ");
            String address = myScanner.nextLine();
            InetAddress serverAddress= InetAddress.getByName(address);

            System.out.println("Enter the port number: ");
            int serverPort = Integer.parseInt(myScanner.nextLine());

            System.out.println("Write the message you want to send: ");
            String echoMessage = myScanner.nextLine();
            buffer = echoMessage.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
            socket.send(sendPacket);
            System.out.println("package sent");

            receptionClient.join();
            System.out.println("client end");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static class ReceptionClient extends Thread {
        DatagramSocket taskSocket;
        byte[] buffer = new byte[1024];

        public ReceptionClient(DatagramSocket socket) {
            this.taskSocket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {

                DatagramPacket receptionPacket = new DatagramPacket(buffer, buffer.length);
                taskSocket.receive(receptionPacket);
                String message = new String(receptionPacket.getData(), 0, receptionPacket.getLength());
                System.out.println("received packet");

                System.out.println(message);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
