package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pagamentos")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false) private Order pedido;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Enums.PaymentStatus status;
    @Column(nullable = false) private String payload;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected Payment() {}
    public Payment(Order pedido, Enums.PaymentStatus status, String payload) {
        this.pedido = pedido; this.status = status; this.payload = payload;
    }
    public Long getId() { return id; }
    public Order getPedido() { return pedido; }
    public Enums.PaymentStatus getStatus() { return status; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
}
