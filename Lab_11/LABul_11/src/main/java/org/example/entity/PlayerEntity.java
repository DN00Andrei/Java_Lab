package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nume;

    private boolean eBot;

    @Column(name = "creat_la")
    private LocalDateTime creatLa = LocalDateTime.now();

    @Column(name = "modificat_la")
    private LocalDateTime modificatLa = LocalDateTime.now();

    @OneToMany(mappedBy = "jucator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultEntity> rezultate = new ArrayList<>();

    @ManyToMany(mappedBy = "jucatori")
    private List<GameEntity> jocuri = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        modificatLa = LocalDateTime.now();
    }

    public PlayerEntity() {}

    public PlayerEntity(String nume, boolean eBot) {
        this.nume = nume;
        this.eBot = eBot;
    }

    public Long getId() { return id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public boolean isEBot() { return eBot; }
    public void setEBot(boolean eBot) { this.eBot = eBot; }
    public LocalDateTime getCreatLa() { return creatLa; }
    public LocalDateTime getModificatLa() { return modificatLa; }
    public List<ResultEntity> getRezultate() { return rezultate; }
    public List<GameEntity> getJocuri() { return jocuri; }

    @Override
    public String toString() {
        return "Player{id=" + id + ", nume=" + nume + ", eBot=" + eBot + "}";
    }
}
