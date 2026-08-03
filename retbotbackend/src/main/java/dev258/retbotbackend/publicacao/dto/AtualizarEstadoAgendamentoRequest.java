package dev258.retbotbackend.publicacao.dto;

import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarEstadoAgendamentoRequest(
        @NotNull EstadoAgendamento novoEstado
) {}