package br.com.raizesdonordeste.application;

import br.com.raizesdonordeste.api.Dtos;
import br.com.raizesdonordeste.domain.*;
import br.com.raizesdonordeste.infrastructure.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final UnitRepository units;
    private final ProductRepository products;
    private final UserRepository users;
    private final StockRepository stocks;
    private final OrderRepository orders;
    private final PaymentRepository payments;
    private final AuditLogRepository audits;

    public OrderService(UnitRepository units, ProductRepository products, UserRepository users,
                        StockRepository stocks, OrderRepository orders, PaymentRepository payments,
                        AuditLogRepository audits) {
        this.units = units; this.products = products; this.users = users; this.stocks = stocks;
        this.orders = orders; this.payments = payments; this.audits = audits;
    }

    @Transactional
    public Dtos.OrderResponse create(Dtos.CreateOrderRequest request, Authentication auth) {
        Unit unit = units.findById(request.unidadeId())
                .orElseThrow(() -> new AppException("UNIDADE_NAO_ENCONTRADA", "Unidade não encontrada.", 404));
        User customer = users.findByEmail(auth.getName()).orElseThrow();
        BigDecimal total = BigDecimal.ZERO;
        List<Product> foundProducts = request.itens().stream().map(item -> products.findById(item.produtoId())
                .orElseThrow(() -> new AppException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado: " + item.produtoId(), 404))).toList();
        for (int i = 0; i < request.itens().size(); i++) {
            Dtos.ItemRequest item = request.itens().get(i);
            Stock stock = stocks.findByUnidadeIdAndProdutoId(unit.getId(), item.produtoId()).orElseThrow(
                    () -> new AppException("ESTOQUE_INSUFICIENTE", "Produto indisponível nesta unidade.", 409));
            if (stock.getQuantidade() < item.quantidade()) {
                throw new AppException("ESTOQUE_INSUFICIENTE", "Não há quantidade suficiente para um ou mais itens.", 409);
            }
            total = total.add(foundProducts.get(i).getPreco().multiply(BigDecimal.valueOf(item.quantidade())));
        }
        Order order = new Order(unit, customer, request.canalPedido(), total);
        for (int i = 0; i < request.itens().size(); i++) {
            Dtos.ItemRequest item = request.itens().get(i);
            Product product = foundProducts.get(i);
            stocks.findByUnidadeIdAndProdutoId(unit.getId(), product.getId()).orElseThrow().retirar(item.quantidade());
            order.addItem(product, item.quantidade(), product.getPreco());
        }
        order = orders.save(order);
        audits.save(new AuditLog("CRIACAO_PEDIDO", "PEDIDO", order.getId(), auth.getName()));
        return toResponse(order);
    }

    @Transactional
    public Dtos.PaymentResponse pay(Dtos.PaymentRequest request, Authentication auth) {
        Order order = orders.findById(request.pedidoId())
                .orElseThrow(() -> new AppException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado.", 404));
        if (!order.getCliente().getEmail().equals(auth.getName()) && !hasRole(auth, "GERENTE") && !hasRole(auth, "ADMIN")) {
            throw new AppException("ACESSO_NEGADO", "Você não pode pagar este pedido.", 403);
        }
        if (payments.findByPedidoId(order.getId()).isPresent()) {
            throw new AppException("PAGAMENTO_JA_REGISTRADO", "O pedido já possui pagamento registrado.", 409);
        }
        Enums.PaymentStatus paymentStatus = request.aprovado() ? Enums.PaymentStatus.APROVADO : Enums.PaymentStatus.RECUSADO;
        if (request.aprovado()) order.changeStatus(Enums.OrderStatus.EM_PREPARO);
        Payment payment = payments.save(new Payment(order, paymentStatus, request.aprovado() ? "MOCK_APPROVED" : "MOCK_DECLINED"));
        orders.save(order);
        audits.save(new AuditLog("REGISTRO_PAGAMENTO_" + paymentStatus, "PEDIDO", order.getId(), auth.getName()));
        return new Dtos.PaymentResponse(payment.getId(), order.getId(), paymentStatus, order.getStatus(),
                payment.getPayload(), payment.getCreatedAt());
    }

    @Transactional
    public Dtos.StatusResponse changeStatus(Long id, Dtos.StatusRequest request, Authentication auth) {
        Order order = orders.findById(id).orElseThrow(() -> new AppException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado.", 404));
        order.changeStatus(request.status());
        orders.save(order);
        audits.save(new AuditLog("ATUALIZACAO_STATUS_" + request.status(), "PEDIDO", id, auth.getName()));
        return new Dtos.StatusResponse(id, order.getStatus());
    }

    public List<Dtos.OrderResponse> list(Enums.Channel channel, Enums.OrderStatus status) {
        List<Order> result = channel != null && status != null ? orders.findAllByCanalPedidoAndStatus(channel, status)
                : channel != null ? orders.findAllByCanalPedido(channel)
                : status != null ? orders.findAllByStatus(status) : orders.findAll();
        return result.stream().map(this::toResponse).toList();
    }

    public Dtos.OrderResponse get(Long id) {
        return toResponse(orders.findById(id).orElseThrow(() -> new AppException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado.", 404)));
    }

    public List<Dtos.AuditResponse> audits() {
        return audits.findAll().stream().map(a -> new Dtos.AuditResponse(a.getId(), a.getAcao(), a.getRecurso(),
                a.getRecursoId(), a.getUsuarioEmail(), a.getCreatedAt())).toList();
    }

    private Dtos.OrderResponse toResponse(Order order) {
        return new Dtos.OrderResponse(order.getId(), order.getUnidade().getId(), order.getCanalPedido(), order.getStatus(),
                order.getTotal(), order.getItens().stream().map(i -> new Dtos.OrderItemResponse(i.getProduto().getId(),
                        i.getProduto().getNome(), i.getQuantidade(), i.getPrecoUnitario())).toList(), order.getCreatedAt());
    }
    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
