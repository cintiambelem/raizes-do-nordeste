package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nome;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String senhaHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.Role role;
    @Column(nullable = false) private boolean consentimentoFidelidade;
    @Column(nullable = false) private Instant createdAt = Instant.now();

    protected User() {}
    public User(String nome, String email, String senhaHash, Enums.Role role, boolean consentimentoFidelidade) {
        this.nome = nome; this.email = email; this.senhaHash = senhaHash; this.role = role;
        this.consentimentoFidelidade = consentimentoFidelidade;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public Enums.Role getRole() { return role; }
    public boolean isConsentimentoFidelidade() { return consentimentoFidelidade; }
}
