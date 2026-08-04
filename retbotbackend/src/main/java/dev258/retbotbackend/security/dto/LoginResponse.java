package dev258.retbotbackend.security.dto;

public record LoginResponse(
        String accessToken,
        String tipo,
        long expiraEmSegundos
) {}
