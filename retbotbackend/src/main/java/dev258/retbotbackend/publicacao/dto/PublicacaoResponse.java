package dev258.retbotbackend.publicacao.dto;

import java.time.OffsetDateTime;

public record PublicacaoResponse(
        Long idPublicacao,
        Long idContaSocial,
        String idPublicacaoExterna,
        String texto,
        OffsetDateTime publicadoEm,
        OffsetDateTime criadoEm
) {}