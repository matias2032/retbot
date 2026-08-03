package dev258.retbotbackend.automacao.exception;

public class RateLimitNaoEncontradoException extends RuntimeException {

    public RateLimitNaoEncontradoException(String message) {
        super(message);
    }
}