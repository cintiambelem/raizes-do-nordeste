package br.com.raizesdonordeste.infrastructure;
import br.com.raizesdonordeste.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UnitRepository extends JpaRepository<Unit, Long> {}
