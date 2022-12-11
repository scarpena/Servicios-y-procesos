package org.scarpena;

import java.io.IOException;
import java.net.*;

public class ServerUDP {
    public static void main(String[] args) {
        final int PUERTO = 5555;
        ServerTask task;

        System.out.println("server started");
        try(DatagramSocket socket = new DatagramSocket(PUERTO)) {

            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket receptionPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receptionPacket);
                System.out.println("received packet");
                task=new ServerTask(receptionPacket, socket);
                task.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ServerTask extends Thread {

        DatagramPacket taskPacket;
        DatagramSocket taskSocket;
        byte[] buffer = new byte[1024];

        public ServerTask(DatagramPacket datagramPacket, DatagramSocket socket) {
            this.taskPacket = datagramPacket;
            this.taskSocket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {

                String message= new String(taskPacket.getData(), 0, taskPacket.getLength());
                System.out.println(message);

                message= "Echo: " + message;
                buffer = message.getBytes();
                int port=taskPacket.getPort();
                InetAddress address = taskPacket.getAddress();

                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address, port);
                taskSocket.send(sendPacket);
                System.out.println("package sent");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}