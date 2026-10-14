package br.com.raizesdonordeste.infrastructure;

import br.com.raizesdonordeste.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

@Configuration
public class SeedConfig {
    @Bean CommandLineRunner seed(UserRepository users, UnitRepository units, ProductRepository products,
                                 StockRepository stocks, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;
            User cliente = users.save(new User("Maria Cliente", "cliente@raizes.com", encoder.encode("Senha@123"),
                    Enums.Role.CLIENTE, true));
            users.save(new User("Gerente Rede", "gerente@raizes.com", encoder.encode("Senha@123"),
                    Enums.Role.GERENTE, false));
            Unit unidade = units.save(new Unit("Unidade Centro", "Rua do Nordeste, 100"));
            Product cuscuz = products.save(new Product("Cuscuz Nordestino", new BigDecimal("19.90")));
            Product tapioca = products.save(new Product("Tapioca de Carne de Sol", new BigDecimal("24.90")));
            stocks.save(new Stock(unidade, cuscuz, 10));
            stocks.save(new Stock(unidade, tapioca, 5));
        };
    }
}
