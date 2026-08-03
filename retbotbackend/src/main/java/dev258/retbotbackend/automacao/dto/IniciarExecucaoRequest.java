package dev258.retbotbackend.automacao.dto;

import jakarta.validation.constraints.NotNull;

public record IniciarExecucaoRequest(

        @NotNull(message = "idAgendamento é obrigatório")
        Long idAgendamento,

        String requestId
) {}