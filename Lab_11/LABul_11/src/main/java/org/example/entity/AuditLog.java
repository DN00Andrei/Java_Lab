package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entitate;
    private String operatie;
    private String detalii;

    @Column(name = "la_momentul")
    private LocalDateTime laMomentul = LocalDateTime.now();

    public AuditLog() {}

    public AuditLog(String entitate, String operatie, String detalii) {
        this.entitate = entitate;
        this.operatie = operatie;
        this.detalii = detalii;
    }

    public Long getId() { return id; }
    public String getEntitate() { return entitate; }
    public String getOperatie() { return operatie; }
    public String getDetalii() { return detalii; }
    public LocalDateTime getLaMomentul() { return laMomentul; }

    @Override
    public String toString() {
        return "[AUDIT] " + laMomentul + " | " + operatie + " pe " + entitate + " | " + detalii;
    }
}
