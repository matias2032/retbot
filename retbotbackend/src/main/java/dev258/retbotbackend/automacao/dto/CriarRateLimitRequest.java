package dev258.retbotbackend.automacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CriarRateLimitRequest(

        @NotNull(message = "idContaSocial é obrigatório")
        Long idContaSocial,

        @NotBlank(message = "endpoint é obrigatório")
        String endpoint,

        @NotNull(message = "limite é obrigatório")
        Integer limite,

        @NotNull(message = "reiniciaEm é obrigatório")
        OffsetDateTime reiniciaEm
) {}