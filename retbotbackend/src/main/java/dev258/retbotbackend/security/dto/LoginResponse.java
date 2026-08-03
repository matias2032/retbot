package dev258.retbotbackend.security.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tipo,
        long expiraEmSegundos
) {}