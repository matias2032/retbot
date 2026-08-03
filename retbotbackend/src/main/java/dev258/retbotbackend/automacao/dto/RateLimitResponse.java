package dev258.retbotbackend.automacao.dto;

import java.time.OffsetDateTime;

public record RateLimitResponse(
        Long idContaSocial,
        String endpoint,
        Integer limite,
        Integer restante,
        OffsetDateTime reiniciaEm
) {}