package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "estoques", uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "produto_id"}))
public class Stock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Unit unidade;
    @ManyToOne(optional = false) private Product produto;
    @Column(nullable = false) private int quantidade;
    protected Stock() {}
    public Stock(Unit unidade, Product produto, int quantidade) {
        this.unidade = unidade; this.produto = produto; this.quantidade = quantidade;
    }
    public Long getId() { return id; }
    public Unit getUnidade() { return unidade; }
    public Product getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public void retirar(int quantidade) { this.quantidade -= quantidade; }
}
