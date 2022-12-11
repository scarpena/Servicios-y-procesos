package org.scarpena;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    public static void main(String[] args) {

        //Lo he hecho de forma que al conectarse cada cliente un solo thread gestione sus peticiones hasta que decida desconectarse.
        //si entra otra petición de otro cliente se le asigna un nuevo thread y gestiona sus peticiones de igual manera.

        final int PUERTO = 5555;
        Socket socket;
        ServerTask task;

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Server started");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("client connected");
                task = new ServerTask(socket);
                task.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ServerTask extends Thread {

        Socket socketTask;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        String echo;

        public ServerTask(Socket socket) {
            this.socketTask = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    ois = new ObjectInputStream(socketTask.getInputStream());
                    echo = (ois.readObject()).toString();
                    if (echo.equals("endClient")) {
                        break;
                    }
                    System.out.println(echo);

                    oos = new ObjectOutputStream(socketTask.getOutputStream());
                    oos.writeObject("Echo: " + echo);
                }
                System.out.println("End of client task");
                oos.close();
                ois.close();
                socketTask.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}