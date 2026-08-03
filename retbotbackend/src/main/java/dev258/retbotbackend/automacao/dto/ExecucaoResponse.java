package dev258.retbotbackend.automacao.dto;

import java.time.OffsetDateTime;

public record ExecucaoResponse(
        Long idExecucao,
        Long idAgendamento,
        OffsetDateTime iniciadoEm,
        OffsetDateTime terminadoEm,
        boolean sucesso,
        Integer codigoHttp,
        String mensagem,
        String requestId
) {}