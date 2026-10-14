package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Order pedido;
    @ManyToOne(optional = false) private Product produto;
    @Column(nullable = false) private int quantidade;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal precoUnitario;
    protected OrderItem() {}
    public OrderItem(Order pedido, Product produto, int quantidade, BigDecimal precoUnitario) {
        this.pedido = pedido; this.produto = produto; this.quantidade = quantidade; this.precoUnitario = precoUnitario;
    }
    public Product getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}
