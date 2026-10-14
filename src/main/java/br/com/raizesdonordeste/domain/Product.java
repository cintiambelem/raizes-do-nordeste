package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nome;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal preco;
    @Column(nullable = false) private boolean ativo = true;
    protected Product() {}
    public Product(String nome, BigDecimal preco) { this.nome = nome; this.preco = preco; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public boolean isAtivo() { return ativo; }
}
