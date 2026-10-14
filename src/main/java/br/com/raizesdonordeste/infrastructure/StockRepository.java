package br.com.raizesdonordeste.infrastructure;
import br.com.raizesdonordeste.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
    List<Stock> findByUnidadeId(Long unidadeId);
}
