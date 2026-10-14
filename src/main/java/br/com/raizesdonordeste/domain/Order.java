package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Unit unidade;
    @ManyToOne(optional = false) private User cliente;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.Channel canalPedido;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.OrderStatus status;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal total;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> itens = new ArrayList<>();
    protected Order() {}
    public Order(Unit unidade, User cliente, Enums.Channel canalPedido, BigDecimal total) {
        this.unidade = unidade; this.cliente = cliente; this.canalPedido = canalPedido;
        this.total = total; this.status = Enums.OrderStatus.AGUARDANDO_PAGAMENTO;
    }
    public void addItem(Product produto, int quantidade, BigDecimal precoUnitario) {
        itens.add(new OrderItem(this, produto, quantidade, precoUnitario));
    }
    public void changeStatus(Enums.OrderStatus status) { this.status = status; }
    public Long getId() { return id; }
    public Unit getUnidade() { return unidade; }
    public User getCliente() { return cliente; }
    public Enums.Channel getCanalPedido() { return canalPedido; }
    public Enums.OrderStatus getStatus() { return status; }
    public BigDecimal getTotal() { return total; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderItem> getItens() { return itens; }
}
