package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.infrastructure.*;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnitController {
    private final UnitRepository units; private final StockRepository stocks;
    public UnitController(UnitRepository units, StockRepository stocks) { this.units = units; this.stocks = stocks; }
    @Operation(summary = "Lista as unidades da rede")
    @GetMapping public List<Dtos.UnitResponse> list() {
        return units.findAll().stream().map(u -> new Dtos.UnitResponse(u.getId(), u.getNome(), u.getEndereco())).toList();
    }
    @Operation(summary = "Consulta o cardápio disponível por unidade")
    @GetMapping("/{id}/cardapio")
    public List<Dtos.ProductResponse> menu(@PathVariable @NonNull Long id) {
        if (!units.existsById(id)) throw new br.com.raizesdonordeste.application.AppException("UNIDADE_NAO_ENCONTRADA", "Unidade não encontrada.", 404);
        return stocks.findByUnidadeId(id).stream().filter(s -> s.getProduto().isAtivo())
                .map(s -> new Dtos.ProductResponse(s.getProduto().getId(), s.getProduto().getNome(),
                        s.getProduto().getPreco(), s.getQuantidade())).toList();
    }
}
