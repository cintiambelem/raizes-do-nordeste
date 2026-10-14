package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "auditoria")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String acao;
    @Column(nullable = false) private String recurso;
    @Column(nullable = false) private Long recursoId;
    @Column(nullable = false) private String usuarioEmail;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected AuditLog() {}
    public AuditLog(String acao, String recurso, Long recursoId, String usuarioEmail) {
        this.acao = acao; this.recurso = recurso; this.recursoId = recursoId; this.usuarioEmail = usuarioEmail;
    }
    public Long getId() { return id; }
    public String getAcao() { return acao; }
    public String getRecurso() { return recurso; }
    public Long getRecursoId() { return recursoId; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public Instant getCreatedAt() { return createdAt; }
}
