package dev258.retbotbackend.utilizador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ActualizarConfiguracaoContaRequest(

        @NotNull
        @Positive(message = "Intervalo mínimo deve ser maior que zero")
        Integer intervaloMinSegundos,

        @NotNull
        @PositiveOrZero
        Integer maxAcoes15Min,

        @PositiveOrZero
        Integer maxAcoesDia,

        @NotNull
        Boolean ativo
) {}