package dev258.retbotbackend.publicacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CriarPublicacaoRequest(
        @NotNull Long idContaSocial,
        @NotBlank String idPublicacaoExterna,
        String texto,
        OffsetDateTime publicadoEm
) {}