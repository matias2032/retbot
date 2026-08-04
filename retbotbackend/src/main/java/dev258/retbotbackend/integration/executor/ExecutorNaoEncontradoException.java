package dev258.retbotbackend.integration.executor;

public class ExecutorNaoEncontradoException extends RuntimeException {
    public ExecutorNaoEncontradoException(String message) {
        super(message);
    }
}