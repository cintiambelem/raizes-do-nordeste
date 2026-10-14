package br.com.raizesdonordeste.infrastructure;
import br.com.raizesdonordeste.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {}
