package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.domain.Enums;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class Dtos {
    private Dtos() {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String senha) {}
    public record LoginResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {}
    public record UserResponse(Long id, String nome, String email, Enums.Role perfil) {}
    public record ErrorResponse(String error, String message, List<FieldIssue> details, Instant timestamp, String path) {}
    public record FieldIssue(String field, String issue) {}
    public record UnitResponse(Long id, String nome, String endereco) {}
    public record ProductResponse(Long id, String nome, BigDecimal preco, int estoqueDisponivel) {}
    public record ItemRequest(@NotNull Long produtoId, @Min(1) int quantidade) {}
    public record CreateOrderRequest(@NotNull Long unidadeId, @NotNull Enums.Channel canalPedido,
                                     @NotEmpty List<@Valid ItemRequest> itens,
                                     @NotBlank String formaPagamento) {}
    public record OrderItemResponse(Long produtoId, String nome, int quantidade, BigDecimal precoUnitario) {}
    public record OrderResponse(Long pedidoId, Long unidadeId, Enums.Channel canalPedido,
                                Enums.OrderStatus status, BigDecimal total,
                                List<OrderItemResponse> itens, Instant createdAt) {}
    public record PaymentRequest(@NotNull Long pedidoId, @NotNull Boolean aprovado) {}
    public record PaymentResponse(Long pagamentoId, Long pedidoId, Enums.PaymentStatus status,
                                  Enums.OrderStatus statusPedido, String payload, Instant createdAt) {}
    public record StatusRequest(@NotNull Enums.OrderStatus status) {}
    public record StatusResponse(Long pedidoId, Enums.OrderStatus status) {}
    public record AuditResponse(Long id, String acao, String recurso, Long recursoId,
                                String usuarioEmail, Instant createdAt) {}
}
