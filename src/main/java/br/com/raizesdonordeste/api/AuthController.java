package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.application.AppException;
import br.com.raizesdonordeste.domain.User;
import br.com.raizesdonordeste.infrastructure.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }
    @Operation(summary = "Autentica usuário e retorna token JWT")
    @PostMapping("/login")
    public Dtos.LoginResponse login(@Valid @RequestBody Dtos.LoginRequest request) {
        User user = users.findByEmail(request.email()).orElseThrow(() ->
                new AppException("CREDENCIAIS_INVALIDAS", "E-mail ou senha inválidos.", 401));
        if (!encoder.matches(request.senha(), user.getSenhaHash())) {
            throw new AppException("CREDENCIAIS_INVALIDAS", "E-mail ou senha inválidos.", 401);
        }
        return new Dtos.LoginResponse(jwt.issue(user), "Bearer", jwt.expiration(),
                new Dtos.UserResponse(user.getId(), user.getNome(), user.getEmail(), user.getRole()));
    }
}
