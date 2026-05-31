package org.example.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(HOST, PORT);
        System.out.println("S-a conectat la server!");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Thread threadCitireServer = new Thread(() -> {
            try {
                String mesaj;
                while ((mesaj = in.readLine()) != null) {
                    System.out.println("[Server] " + mesaj);
                }
            } catch (IOException e) {
                System.out.println("Conexiunea s-a inchis.");
            }
        });
        threadCitireServer.setDaemon(true);
        threadCitireServer.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String linie = scanner.nextLine().trim();
            if (linie.equalsIgnoreCase("exit")) {
                socket.close();
                break;
            }
            out.println(linie);
        }

        System.out.println("Te-ai deconectat.");
    }
}