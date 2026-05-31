package org.example;

import org.example.entity.*;
import org.example.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class QuizDBApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(QuizDBApp.class);

    private final QuizService quizService;

    public QuizDBApp(QuizService quizService) {
        this.quizService = quizService;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuizDBApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Pornesc testul bazei de date ===");

        PlayerEntity bob = quizService.salveazaJucator("Bob", false);
        PlayerEntity ana = quizService.salveazaJucator("Ana", false);
        PlayerEntity bot = quizService.salveazaJucator("BotRandom", true);

        Map<String, String> optiuni1 = new LinkedHashMap<>();
        optiuni1.put("A", "Diogene");
        optiuni1.put("B", "Iulius Cezar");
        optiuni1.put("C", "Nero");
        optiuni1.put("D", "Antonius");
        QuestionEntity q1 = quizService.salveazaIntrebare(
                "Al cui fiu adoptiv a fost imparatul Augustus?", "B", 10, optiuni1);

        Map<String, String> optiuni2 = new LinkedHashMap<>();
        optiuni2.put("A", "54");
        optiuni2.put("B", "56");
        optiuni2.put("C", "64");
        optiuni2.put("D", "48");
        QuestionEntity q2 = quizService.salveazaIntrebare("Cat face 7 x 8?", "B", 10, optiuni2);

        GameEntity joc = quizService.incepeJoc(List.of(bob, ana, bot));

        quizService.salveazaRezultat(bob, q1, joc, "B", true, 10, 3200L);
        quizService.salveazaRezultat(ana, q1, joc, "A", false, 0, 5100L);
        quizService.salveazaRezultat(bot, q1, joc, "C", false, 0, 1200L);

        quizService.salveazaRezultat(bob, q2, joc, "B", true, 10, 2800L);
        quizService.salveazaRezultat(ana, q2, joc, "B", true, 10, 4000L);
        quizService.salveazaRezultat(bot, q2, joc, "A", false, 0, 900L);

        quizService.terminaJoc(joc, "Bob");

        quizService.redenumestejucator(ana.getId(), "AnaRenumita");

        log.info("=== Jucatori umani in DB ===");
        quizService.totiJucatoriiUmani().forEach(p -> log.info("  -> {}", p));

        log.info("=== Rezultate joc {} ===", joc.getId());
        quizService.rezultatePentruJoc(joc.getId()).forEach(r -> log.info("  -> {}", r));

        log.info("=== Audit Log ===");
        quizService.getAuditLog().forEach(a -> log.info("  -> {}", a));

        log.info("=== Test terminat cu succes! ===");
    }
}
