package org.example;

import model.Game;
import model.Player;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket socket;
    private GameServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String numeJucator;
    private boolean conectat = true;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            trimite("Bun venit la Quiz! Trimite: JOIN <nume> pentru a intra in joc.");

            String linie;
            while (conectat && (linie = in.readLine()) != null) {
                linie = linie.trim();
                System.out.println("[Server] Primit de la " + (numeJucator != null ? numeJucator : "anonim") + ": " + linie);
                proceseazaComanda(linie);
            }

        } catch (IOException e) {
            System.out.println("[Server] Clientul a plecat: " + e.getMessage());
        } finally {
            server.deconecteazaClient(this);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void proceseazaComanda(String comanda) {
        if (comanda.equalsIgnoreCase("stop")) {
            trimite("Server stopped");
            server.opreste();
            return;
        }

        String[] parti = comanda.split(" ", 2);
        String cmd = parti[0].toUpperCase();
        String arg = parti.length > 1 ? parti[1] : "";

        switch (cmd) {
            case "JOIN":
                if (arg.isEmpty()) {
                    trimite("Trebuie sa dai un nume: JOIN <nume>");
                } else {
                    numeJucator = arg.trim();
                    Player jucator = new Player(numeJucator, false);
                    boolean ok = server.adaugaJucator(jucator, this);
                    if (ok) {
                        trimite("Te-ai alaturat jocului ca: " + numeJucator);
                        server.broadcast("Jucatorul " + numeJucator + " a intrat in joc!");
                    } else {
                        trimite("Numele e deja luat sau jocul a inceput deja.");
                    }
                }
                break;

            case "ANSWER":
                if (numeJucator == null) {
                    trimite("Trebuie sa dai JOIN mai intai.");
                } else if (arg.isEmpty()) {
                    trimite("Trimite: ANSWER <A/B/C/D>");
                } else {
                    server.inregistreazaRaspuns(numeJucator, arg.trim());
                }
                break;

            case "SCORES":
                Game joc = server.getJoc();
                if (joc == null) {
                    trimite("Nu exista niciun joc activ.");
                } else {
                    trimite(joc.getScoruri());
                }
                break;

            case "START":
                server.incepeJocul();
                break;

            default:
                trimite("Server received the request: " + comanda);
                break;
        }
    }

    public void trimite(String mesaj) {
        if (out != null) out.println(mesaj);
    }

    public String getNumeJucator() { return numeJucator; }

    public void deconecteaza() {
        conectat = false;
        try { socket.close(); } catch (IOException ignored) {}
    }
}
