package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.application.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditController {
    private final OrderService service;
    public AuditController(OrderService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public List<Dtos.AuditResponse> list() { return service.audits(); }
}
