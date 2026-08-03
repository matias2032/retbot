package dev258.retbotbackend.publicacao.dto;

import dev258.retbotbackend.publicacao.enums.TipoAcao;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CriarAgendamentoRequest(
        @NotNull Long idContaSocial,
        @NotNull TipoAcao tipo,
        Long idPublicacao,
        @NotNull OffsetDateTime executarEm,
        Short prioridade
) {}