package org.example;

import model.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {

    private static final int PORT = 8000;
    private static final int TIMP_LIMITA_SECUNDE = 15;
    private static final boolean FOLOSESTE_VIRTUAL_THREADS = false;

    private ServerSocket serverSocket;
    private ExecutorService executor;
    private List<ClientThread> clienti = new CopyOnWriteArrayList<>();
    private Game joc;
    private List<Question> intrebari;
    private boolean serverActiv = true;

    public GameServer() throws Exception {
        serverSocket = new ServerSocket(PORT);

        if (FOLOSESTE_VIRTUAL_THREADS) {
            executor = Executors.newVirtualThreadPerTaskExecutor();
            System.out.println("[Server] Pornit cu virtual threads pe portul " + PORT);
        } else {
            executor = Executors.newCachedThreadPool();
            System.out.println("[Server] Pornit cu platform threads pe portul " + PORT);
        }

        String caleIntrebari = "Qs.txt";
        intrebari = QuestionLoader.incarcaDinFisier(caleIntrebari);
        System.out.println("[Server] " + intrebari.size() + " intrebari incarcate.");

        joc = new Game(new ArrayList<>(intrebari), TIMP_LIMITA_SECUNDE);
        adaugaBoti();
    }

    private void adaugaBoti() {
        RandomBot rb = new RandomBot("BotRandom");
        joc.adaugaJucator(new Player(rb.getNume(), true));
        System.out.println("[Server] Bot adaugat: " + rb.getNume());
    }

    public void acceptaClientii() {
        System.out.println("[Server] Astept clienti... (trimite START pentru a incepe jocul)");
        while (serverActiv) {
            try {
                serverSocket.setSoTimeout(1000);
                Socket socket = serverSocket.accept();
                System.out.println("[Server] Client conectat: " + socket.getInetAddress());
                ClientThread ct = new ClientThread(socket, this);
                clienti.add(ct);
                executor.submit(ct);
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                if (serverActiv) System.out.println("[Server] Eroare accept: " + e.getMessage());
            }
        }
    }

    public boolean adaugaJucator(Player jucator, ClientThread ct) {
        if (joc.isJocInCurs()) return false;
        if (joc.areJucator(jucator.getNume())) return false;
        joc.adaugaJucator(jucator);
        return true;
    }

    public synchronized void incepeJocul() {
        if (joc.isJocInCurs()) {
            broadcast("Jocul a inceput deja!");
            return;
        }
        if (joc.getJucatori().size() < 1) {
            broadcast("Trebuie cel putin un jucator uman!");
            return;
        }
        joc.incepe();
        broadcast("=== JOCUL INCEPE! ===");
        new Thread(this::ruleazaJocul).start();
    }

    private void ruleazaJocul() {
        RandomBot rb = new RandomBot("BotRandom");

        do {
            Question q = joc.getIntrebareCurenta();
            if (q == null) break;

            broadcast("--- Intrebarea " + (joc.getIndexCurent() + 1) + "/" + joc.getNumarIntrebari() + " ---");
            broadcast("INTREBARE: " + q.getText());
            for (Map.Entry<String, String> opt : q.getOptions().entrySet()) {
                broadcast("  " + opt.getKey() + ") " + opt.getValue());
            }
            broadcast("Ai " + joc.getTimpLimitaSecunde() + " secunde sa raspunzi! (ANSWER A/B/C/D)");

            joc.starteazaCronometru();

            simulareRaspunsBot(rb, q);

            long sfarsit = System.currentTimeMillis() + joc.getTimpLimitaSecunde() * 1000L;
            while (System.currentTimeMillis() < sfarsit && !joc.auRaspunsToate()) {
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            broadcast("Timpul a expirat! Raspunsul corect era: " + q.getCorrectAnswer());
            Map<String, Boolean> rezultate = joc.evalueazaRunda();
            for (Map.Entry<String, Boolean> e : rezultate.entrySet()) {
                String mesaj = e.getKey() + ": " + (e.getValue() ? "CORECT! +" + q.getPuncte() + "p" : "GRESIT");
                broadcast(mesaj);
            }

            broadcast(joc.getScoruri());

            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        } while (joc.urmatoareaIntrebare());

        joc.setJocInCurs(false);
        Player castigator = joc.getCastigator();
        broadcast("=== JOC TERMINAT! ===");
        if (castigator != null) {
            broadcast("CASTIGATORUL este: " + castigator.getNume() + " cu " + castigator.getScor() + " puncte!");
        }
        broadcast(joc.getScoruri());
    }

    private void simulareRaspunsBot(RandomBot bot, Question q) {
        executor.submit(() -> {
            String raspuns = bot.raspunde(q);
            inregistreazaRaspuns(bot.getNume(), raspuns);
        });
    }

    public synchronized void inregistreazaRaspuns(String numeJucator, String raspuns) {
        if (!joc.isJocInCurs()) return;
        boolean inregistrat = joc.inregistreazaRaspuns(numeJucator, raspuns);
        if (inregistrat) {
            broadcast(numeJucator + " a raspuns.");
        }
    }

    public void broadcast(String mesaj) {
        System.out.println("[Broadcast] " + mesaj);
        for (ClientThread ct : clienti) {
            ct.trimite(mesaj);
        }
    }

    public void deconecteazaClient(ClientThread ct) {
        clienti.remove(ct);
        if (ct.getNumeJucator() != null) {
            broadcast("Jucatorul " + ct.getNumeJucator() + " s-a deconectat.");
        }
    }

    public void opreste() {
        System.out.println("[Server] Se opreste...");
        serverActiv = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("[Server] Eroare la oprire: " + e.getMessage());
        }
        System.out.println("[Server] Oprit.");
    }

    public Game getJoc() { return joc; }

    public static void main(String[] args) throws Exception {
        GameServer server = new GameServer();
        server.acceptaClientii();
    }
}