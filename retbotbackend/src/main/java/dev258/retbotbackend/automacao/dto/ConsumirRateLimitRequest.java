package dev258.retbotbackend.automacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsumirRateLimitRequest(

        @NotNull(message = "idContaSocial é obrigatório")
        Long idContaSocial,

        @NotBlank(message = "endpoint é obrigatório")
        String endpoint
) {}