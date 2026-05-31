package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String text;

    private String raspunsCorect;
    private int puncte;

    @Column(name = "creat_la")
    private LocalDateTime creatLa = LocalDateTime.now();

    @Column(name = "modificat_la")
    private LocalDateTime modificatLa = LocalDateTime.now();

    @OneToMany(mappedBy = "intrebare", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OptionEntity> optiuni = new ArrayList<>();

    @OneToMany(mappedBy = "intrebare", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultEntity> rezultate = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        modificatLa = LocalDateTime.now();
    }

    public QuestionEntity() {}

    public QuestionEntity(String text, String raspunsCorect, int puncte) {
        this.text = text;
        this.raspunsCorect = raspunsCorect;
        this.puncte = puncte;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getRaspunsCorect() { return raspunsCorect; }
    public void setRaspunsCorect(String raspunsCorect) { this.raspunsCorect = raspunsCorect; }
    public int getPuncte() { return puncte; }
    public void setPuncte(int puncte) { this.puncte = puncte; }
    public List<OptionEntity> getOptiuni() { return optiuni; }
    public List<ResultEntity> getRezultate() { return rezultate; }
    public LocalDateTime getCreatLa() { return creatLa; }
    public LocalDateTime getModificatLa() { return modificatLa; }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text=" + text + ", raspuns=" + raspunsCorect + "}";
    }
}
