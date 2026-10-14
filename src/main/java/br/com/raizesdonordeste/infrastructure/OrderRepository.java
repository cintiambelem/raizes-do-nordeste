package br.com.raizesdonordeste.infrastructure;
import br.com.raizesdonordeste.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCanalPedidoAndStatus(Enums.Channel canalPedido, Enums.OrderStatus status);
    List<Order> findAllByCanalPedido(Enums.Channel canalPedido);
    List<Order> findAllByStatus(Enums.OrderStatus status);
}
