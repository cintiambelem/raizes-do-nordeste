package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.application.OrderService;
import br.com.raizesdonordeste.domain.Enums;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }
    @Operation(summary = "Cria pedido validando estoque e calculando o total")
    @PostMapping @PreAuthorize("hasRole('CLIENTE')")
    public Dtos.OrderResponse create(@Valid @RequestBody Dtos.CreateOrderRequest request, Authentication auth) {
        return service.create(request, auth);
    }
    @Operation(summary = "Lista pedidos com filtros opcionais de canal e status")
    @GetMapping @PreAuthorize("hasAnyRole('CLIENTE','ATENDENTE','COZINHA','GERENTE','ADMIN')")
    public List<Dtos.OrderResponse> list(@RequestParam(required = false) Enums.Channel canalPedido,
                                         @RequestParam(required = false) Enums.OrderStatus status) {
        return service.list(canalPedido, status);
    }
    @GetMapping("/{id}") public Dtos.OrderResponse get(@PathVariable Long id) { return service.get(id); }
    @PatchMapping("/{id}/status") @PreAuthorize("hasAnyRole('COZINHA','GERENTE','ADMIN')")
    public Dtos.StatusResponse status(@PathVariable Long id, @Valid @RequestBody Dtos.StatusRequest request, Authentication auth) {
        return service.changeStatus(id, request, auth);
    }
}
