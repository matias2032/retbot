package dev258.retbotbackend.automacao.exception;

public class RateLimitExcedidoException extends RuntimeException {

    public RateLimitExcedidoException(String message) {
        super(message);
    }
}