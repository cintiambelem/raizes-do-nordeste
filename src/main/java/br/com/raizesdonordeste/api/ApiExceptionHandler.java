package br.com.raizesdonordeste.api;

import br.com.raizesdonordeste.application.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AppException.class)
    ResponseEntity<Dtos.ErrorResponse> handle(AppException ex, HttpServletRequest request) {
        return response(ex.getStatus(), new Dtos.ErrorResponse(ex.getCode(), ex.getMessage(), List.of(),
                Instant.now(), request.getRequestURI()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Dtos.ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Dtos.FieldIssue> issues = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new Dtos.FieldIssue(e.getField(), e.getDefaultMessage())).toList();
        return response(422, new Dtos.ErrorResponse("VALIDACAO_INVALIDA", "Existem campos inválidos.", issues,
                Instant.now(), request.getRequestURI()));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Dtos.ErrorResponse> constraint(ConstraintViolationException ex, HttpServletRequest request) {
        return response(422, new Dtos.ErrorResponse("VALIDACAO_INVALIDA", ex.getMessage(), List.of(),
                Instant.now(), request.getRequestURI()));
    }
    private ResponseEntity<Dtos.ErrorResponse> response(int status, Dtos.ErrorResponse body) {
        return ResponseEntity.status(status).body(body);
    }
}
