package dev258.retbotbackend.publicacao.dto;

import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import dev258.retbotbackend.publicacao.enums.TipoAcao;

import java.time.OffsetDateTime;

public record AgendamentoResponse(
        Long idAgendamento,
        Long idContaSocial,
        TipoAcao tipo,
        Long idPublicacao,
        OffsetDateTime executarEm,
        Short prioridade,
        EstadoAgendamento estado,
        Short tentativas,
        OffsetDateTime criadoEm
) {}