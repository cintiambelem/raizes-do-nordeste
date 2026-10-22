package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.application.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {
    private final OrderService service;
    public PaymentController(OrderService service) { this.service = service; }
    @Operation(summary = "Simula o retorno de um gateway externo de pagamento")
    @PostMapping("/mock")
    public Dtos.PaymentResponse mock(@Valid @RequestBody Dtos.PaymentRequest request, Authentication auth) {
        return service.pay(request, auth);
    }
}
