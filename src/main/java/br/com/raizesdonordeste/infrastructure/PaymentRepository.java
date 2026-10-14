package br.com.raizesdonordeste.infrastructure;
import br.com.raizesdonordeste.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PaymentRepository extends JpaRepository<Payment, Long> { Optional<Payment> findByPedidoId(Long pedidoId); }
