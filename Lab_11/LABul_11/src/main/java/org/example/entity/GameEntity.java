package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inceput_la")
    private LocalDateTime inceputLa;

    @Column(name = "terminat_la")
    private LocalDateTime terminatLa;

    private String castigator;

    @Column(name = "creat_la")
    private LocalDateTime creatLa = LocalDateTime.now();

    @Column(name = "modificat_la")
    private LocalDateTime modificatLa = LocalDateTime.now();

    /*
    @ManyToMany
    @JoinTable(
        name = "game_players",
        joinColumns = @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "FK_game_to_player")),
        inverseJoinColumns = @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_player_to_game"))
    )
    private Set<PlayerEntity> jucatori = new LinkedHashSet<>();

    @OneToMany(mappedBy = "joc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultEntity> rezultate = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime modificatLa;

    */

    @ManyToMany
    @JoinTable(
        name = "game_players",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerEntity> jucatori = new ArrayList<>();

    @OneToMany(mappedBy = "joc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultEntity> rezultate = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        modificatLa = LocalDateTime.now();
    }






       public GameEntity() {}

    public GameEntity(LocalDateTime inceputLa) {
        this.inceputLa = inceputLa;
    }

    public Long getId() { return id; }
    public LocalDateTime getInceputLa() { return inceputLa; }
    public void setInceputLa(LocalDateTime inceputLa) { this.inceputLa = inceputLa; }
    public LocalDateTime getTerminatLa() { return terminatLa; }
    public void setTerminatLa(LocalDateTime terminatLa) { this.terminatLa = terminatLa; }
    public String getCastigator() { return castigator; }
    public void setCastigator(String castigator) { this.castigator = castigator; }
    public List<PlayerEntity> getJucatori() { return jucatori; }
    public List<ResultEntity> getRezultate() { return rezultate; }
    public LocalDateTime getCreatLa() { return creatLa; }
    public LocalDateTime getModificatLa() { return modificatLa; }

    @Override
    public String toString() {
        return "Game{id=" + id + ", inceput=" + inceputLa + ", castigator=" + castigator + "}";
    }
}
