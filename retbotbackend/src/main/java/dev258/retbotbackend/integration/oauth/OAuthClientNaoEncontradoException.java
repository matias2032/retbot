package dev258.retbotbackend.integration.oauth;

public class OAuthClientNaoEncontradoException extends RuntimeException {
    public OAuthClientNaoEncontradoException(String message) {
        super(message);
    }
}