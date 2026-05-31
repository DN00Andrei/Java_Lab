package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String litera;
    private String text;

       @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity intrebare;

    public OptionEntity() {}

    public OptionEntity(String litera, String text, QuestionEntity intrebare) {
        this.litera = litera;
        this.text = text;
        this.intrebare = intrebare;
    }

    public Long getId() { return id; }
    public String getLitera() { return litera; }
    public String getText(){ return text; }
    public QuestionEntity getIntrebare() { 
    return intrebare; }
    public void setIntrebare(QuestionEntity intrebare) { this.intrebare = intrebare; }
}
