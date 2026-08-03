package dev258.retbotbackend.automacao.dto;

import jakarta.validation.constraints.NotNull;

public record FinalizarExecucaoRequest(

        @NotNull(message = "sucesso é obrigatório")
        Boolean sucesso,

        Integer codigoHttp,

        String mensagem
) {}