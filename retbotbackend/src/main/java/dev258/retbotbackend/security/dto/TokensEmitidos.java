package dev258.retbotbackend.security.dto;

/**
 * DTO interno — nunca sai da camada security.
 * O refreshToken aqui dentro só é usado para construir o cookie;
 * nunca é serializado numa resposta JSON.
 */
public record TokensEmitidos(
        String accessToken,
        String refreshToken,
        String tipo,
        long expiraEmSegundos
) {}