package org.example.service;

import org.example.entity.*;
import org.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private static final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final PlayerRepository playerRepo;
    private final QuestionRepository questionRepo;
    private final GameRepository gameRepo;
    private final ResultRepository resultRepo;
    private final AuditLogRepository auditRepo;

    public QuizService(PlayerRepository playerRepo, QuestionRepository questionRepo,
                       GameRepository gameRepo, ResultRepository resultRepo,
                       AuditLogRepository auditRepo) {
        this.playerRepo = playerRepo;
        this.questionRepo = questionRepo;
        this.gameRepo = gameRepo;
        this.resultRepo = resultRepo;
        this.auditRepo = auditRepo;
    }

    @Transactional
    public PlayerEntity salveazaJucator(String nume, boolean eBot) {
        long start = System.currentTimeMillis();
        try {
            Optional<PlayerEntity> existent = playerRepo.findByNume(nume);
            if (existent.isPresent()) {
                log.info("Jucatorul '{}' exista deja in DB.", nume);
                return existent.get();
            }
            PlayerEntity p = new PlayerEntity(nume, eBot);
            PlayerEntity salvat = playerRepo.save(p);
            auditRepo.save(new AuditLog("PlayerEntity", "INSERT", "Jucator nou: " + nume));
            log.info("Jucator salvat: {}", salvat);
            return salvat;
        } catch (Exception e) {
            log.error("Eroare la salvarea jucatorului '{}': {}", nume, e.getMessage());
            throw e;
        } finally {
            log.debug("salveazaJucator() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public QuestionEntity salveazaIntrebare(String text, String raspuns, int puncte, java.util.Map<String, String> optiuni) {
        long start = System.currentTimeMillis();
        try {
            QuestionEntity q = new QuestionEntity(text, raspuns, puncte);
            q = questionRepo.save(q);
            for (java.util.Map.Entry<String, String> e : optiuni.entrySet()) {
                q.getOptiuni().add(new OptionEntity(e.getKey(), e.getValue(), q));
            }
            QuestionEntity salvata = questionRepo.save(q);
            auditRepo.save(new AuditLog("QuestionEntity", "INSERT", "Intrebare noua: " + text));
            log.info("Intrebare salvata: {}", salvata);
            return salvata;
        } catch (Exception e) {
            log.error("Eroare la salvarea intrebarii: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("salveazaIntrebare() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public GameEntity incepeJoc(List<PlayerEntity> jucatori) {
        long start = System.currentTimeMillis();
        try {
            GameEntity joc = new GameEntity(LocalDateTime.now());
            joc.getJucatori().addAll(jucatori);
            GameEntity salvat = gameRepo.save(joc);
            auditRepo.save(new AuditLog("GameEntity", "INSERT", "Joc nou inceput cu " + jucatori.size() + " jucatori"));
            log.info("Joc inceput: {}", salvat);
            return salvat;
        } catch (Exception e) {
            log.error("Eroare la inceperea jocului: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("incepeJoc() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public void salveazaRezultat(PlayerEntity jucator, QuestionEntity intrebare, GameEntity joc,
                                 String raspuns, boolean corect, int puncte, long timpMs) {
        long start = System.currentTimeMillis();
        try {
            ResultEntity r = new ResultEntity(jucator, intrebare, joc, raspuns, corect, puncte, timpMs);
            resultRepo.save(r);
            auditRepo.save(new AuditLog("ResultEntity", "INSERT",
                    jucator.getNume() + " a raspuns la '" + intrebare.getText() + "': " + raspuns + " (" + (corect ? "corect" : "gresit") + ")"));
            log.info("Rezultat salvat: {}", r);
        } catch (Exception e) {
            log.error("Eroare la salvarea rezultatului: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("salveazaRezultat() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public void terminaJoc(GameEntity joc, String castigator) {
        long start = System.currentTimeMillis();
        try {
            joc.setTerminatLa(LocalDateTime.now());
            joc.setCastigator(castigator);
            gameRepo.save(joc);
            auditRepo.save(new AuditLog("GameEntity", "UPDATE", "Joc terminat. Castigator: " + castigator));
            log.info("Joc terminat. Castigator: {}", castigator);
        } catch (Exception e) {
            log.error("Eroare la terminarea jocului: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("terminaJoc() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public void redenumestejucator(Long id, String numeNou) {
        long start = System.currentTimeMillis();
        try {
            int actualizat = playerRepo.redenumestejucator(id, numeNou);
            if (actualizat > 0) {
                auditRepo.save(new AuditLog("PlayerEntity", "UPDATE", "Jucator " + id + " redenumit la: " + numeNou));
                log.info("Jucator {} redenumit la '{}'", id, numeNou);
            }
        } catch (Exception e) {
            log.error("Eroare la redenumire: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("redenumestejucator() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    public List<PlayerEntity> totiJucatoriiUmani() {
        long start = System.currentTimeMillis();
        try {
            List<PlayerEntity> lista = playerRepo.gasesteTotiJucatoriiUmani();
            log.info("Jucatori umani gasiti: {}", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Eroare la cautarea jucatorilor: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("totiJucatoriiUmani() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    public List<ResultEntity> rezultatePentruJoc(Long jocId) {
        long start = System.currentTimeMillis();
        try {
            List<ResultEntity> lista = resultRepo.gasesteRezultateJoc(jocId);
            log.info("Rezultate pentru jocul {}: {} inregistrari", jocId, lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Eroare la cautarea rezultatelor: {}", e.getMessage());
            throw e;
        } finally {
            log.debug("rezultatePentruJoc() -> {}ms", System.currentTimeMillis() - start);
        }
    }

    public List<AuditLog> getAuditLog() {
        return auditRepo.findAll();
    }

    public Optional<PlayerEntity> gasesteJucatorDupaNume(String nume) {
        return playerRepo.findByNume(nume);
    }

    public List<QuestionEntity> toateIntrebarile() {
        return questionRepo.findAll();
    }

    public Optional<GameEntity> gasesteJoc(Long id) {
        return gameRepo.findById(id);
    }
}