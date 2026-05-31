package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raspunsDat;
    private boolean esteCorect;
    private int puncteObtinute;
    private long timpRaspunsMs;

    @Column(name = "creat_la")
    private LocalDateTime creatLa = LocalDateTime.now();

    @Column(name = "modificat_la")
    private LocalDateTime modificatLa = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity jucator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity intrebare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity joc;

    @PreUpdate
    public void preUpdate() {
        modificatLa = LocalDateTime.now();
    }

    public ResultEntity() {}

    public ResultEntity(PlayerEntity jucator, QuestionEntity intrebare, GameEntity joc,
                        String raspunsDat, boolean esteCorect, int puncteObtinute, long timpRaspunsMs) {
        this.jucator = jucator;
        this.intrebare = intrebare;
        this.joc = joc;
        this.raspunsDat = raspunsDat;
        this.esteCorect = esteCorect;
        this.puncteObtinute = puncteObtinute;
        this.timpRaspunsMs = timpRaspunsMs;
    }

    public Long getId() { return id; }
    public String getRaspunsDat() { return raspunsDat; }
    public boolean isEsteCorect() { return esteCorect; }
    public int getPuncteObtinute() { return puncteObtinute; }
    public long getTimpRaspunsMs() { return timpRaspunsMs; }
    public PlayerEntity getJucator() { return jucator; }
    public QuestionEntity getIntrebare() { return intrebare; }
    public GameEntity getJoc() { return joc; }
    public LocalDateTime getCreatLa() { return creatLa; }
    public LocalDateTime getModificatLa() { return modificatLa; }

    @Override
    public String toString() {
        return "Result{corect=" + esteCorect + ", puncte=" + puncteObtinute + ", raspuns=" + raspunsDat + ", timp=" + timpRaspunsMs + "ms}";
    }
}