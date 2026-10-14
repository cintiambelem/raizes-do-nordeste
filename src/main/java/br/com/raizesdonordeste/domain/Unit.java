package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "unidades")
public class Unit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nome;
    @Column(nullable = false) private String endereco;
    protected Unit() {}
    public Unit(String nome, String endereco) { this.nome = nome; this.endereco = endereco; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
}
