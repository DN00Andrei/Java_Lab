package playerStuff;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class VirtualThreadsTest {

    private static final String HOST = "localhost";
    private static final int PORT = 8000;
    private static final int NR_CLIENTI = 500;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Test Virtual Threads vs Platform Threads ===");
        System.out.println("Numarul de clienti simulati: " + NR_CLIENTI);
        System.out.println();

        long timpPlatform = ruleazaTest(false);
        Thread.sleep(2000);
        long timpVirtual = ruleazaTest(true);

        System.out.println("\n=== REZULTATE ===");
        System.out.println("Platform threads: " + timpPlatform + "ms");
        System.out.println("Virtual threads:  " + timpVirtual + "ms");
        System.out.println("Diferenta: " + (timpPlatform - timpVirtual) + "ms");
    }

    static long ruleazaTest(boolean virtual) throws Exception {
        String tip = virtual ? "VIRTUAL" : "PLATFORM";
        System.out.println("--- Test cu " + tip + " threads ---");

        ExecutorService executor = virtual
                ? Executors.newVirtualThreadPerTaskExecutor()
                : Executors.newFixedThreadPool(NR_CLIENTI);

        AtomicInteger conectati = new AtomicInteger(0);
        AtomicInteger erori = new AtomicInteger(0);
        CountDownLatch gata = new CountDownLatch(NR_CLIENTI);

        long start = System.currentTimeMillis();

        for (int i = 0; i < NR_CLIENTI; i++) {
            int idClient = i;
            executor.submit(() -> {
                try {
                    Socket s = new Socket(HOST, PORT);
                    PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    br.readLine();

                    pw.println("JOIN Bot" + tip + idClient);
                    Thread.sleep(100);
                    pw.println("ANSWER A");
                    Thread.sleep(100);

                    conectati.incrementAndGet();
                    s.close();
                } catch (Exception e) {
                    erori.incrementAndGet();
                } finally {
                    gata.countDown();
                }
            });
        }

        gata.await(30, TimeUnit.SECONDS);
        long timp = System.currentTimeMillis() - start;

        executor.shutdown();
        System.out.println("Conectati: " + conectati.get() + "/" + NR_CLIENTI + ", Erori: " + erori.get() + ", Timp: " + timp + "ms");
        return timp;
    }
}
