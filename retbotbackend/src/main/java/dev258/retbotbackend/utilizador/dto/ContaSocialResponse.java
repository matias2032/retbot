package dev258.retbotbackend.utilizador.dto;

import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.enums.EstadoConta;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;

import java.time.OffsetDateTime;

// Nota: access_token e refresh_token nunca são expostos ao cliente.
public record ContaSocialResponse(
        Long idContaSocial,
        PlataformaSocial plataforma,
        String username,
        String nomeExibicao,
        EstadoConta estado,
        OffsetDateTime ultimoSync,
        OffsetDateTime criadoEm,
        String urlInstancia // obrigatório apenas para Mastodon; null para as restantes plataformas
) {
    public static ContaSocialResponse from(ContaSocial contaSocial) {
        return new ContaSocialResponse(
                contaSocial.getIdContaSocial(),
                contaSocial.getPlataforma(),
                contaSocial.getUsername(),
                contaSocial.getNomeExibicao(),
                contaSocial.getEstado(),
                contaSocial.getUltimoSync(),
                contaSocial.getCriadoEm(),
                contaSocial.getUrlInstancia()
        );
    }
}