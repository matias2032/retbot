package dev258.retbotbackend.shared.exception;

import dev258.retbotbackend.automacao.exception.ExecucaoNaoEncontradaException;
import dev258.retbotbackend.automacao.exception.RateLimitExcedidoException;
import dev258.retbotbackend.automacao.exception.RateLimitNaoEncontradoException;
import dev258.retbotbackend.publicacao.exception.AgendamentoNaoEncontradoException;
import dev258.retbotbackend.publicacao.exception.PublicacaoNaoEncontradaException;
import dev258.retbotbackend.security.CredenciaisInvalidasException;
import dev258.retbotbackend.security.TokenInvalidoException;
import dev258.retbotbackend.utilizador.exception.ContaSocialException;
import dev258.retbotbackend.utilizador.exception.UtilizadorNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import dev258.retbotbackend.integration.executor.ExecutorNaoEncontradoException;
import dev258.retbotbackend.integration.client.ApiClienteException;
import dev258.retbotbackend.integration.oauth.EstadoOAuthInvalidoException;
import dev258.retbotbackend.integration.oauth.OAuthClientNaoEncontradoException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UtilizadorNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUtilizadorNaoEncontrado(
            UtilizadorNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ContaSocialException.class)
    public ResponseEntity<ErrorResponse> handleContaSocial(
            ContaSocialException ex, HttpServletRequest request) {
        return build(ex.getStatus(), ex.getMessage(), request);
    }

    @ExceptionHandler(PublicacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handlePublicacaoNaoEncontrada(
            PublicacaoNaoEncontradaException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

@ExceptionHandler(AgendamentoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleAgendamentoNaoEncontrado(
            AgendamentoNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ExecucaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleExecucaoNaoEncontrada(
            ExecucaoNaoEncontradaException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RateLimitNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitNaoEncontrado(
            RateLimitNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RateLimitExcedidoException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExcedido(
            RateLimitExcedidoException ex, HttpServletRequest request) {
        return build(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), request);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredenciaisInvalidas(
            CredenciaisInvalidasException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleTokenInvalido(
            TokenInvalidoException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> erros = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação nos dados enviados",
                request.getRequestURI(),
                erros
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(
            Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request);
    }

    @ExceptionHandler(ExecutorNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleExecutorNaoEncontrado(
            ExecutorNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(ApiClienteException.class)
    public ResponseEntity<ErrorResponse> handleApiCliente(
            ApiClienteException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_GATEWAY, ex.getMessage(), request);
    }

    @ExceptionHandler(EstadoOAuthInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleEstadoOAuthInvalido(
            EstadoOAuthInvalidoException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(OAuthClientNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleOAuthClientNaoEncontrado(
            OAuthClientNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }
    
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}