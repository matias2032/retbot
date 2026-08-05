package dev258.retbotbackend.utilizador.dto;

import dev258.retbotbackend.utilizador.entity.Utilizador;

import java.time.OffsetDateTime;

public record UtilizadorResponse(
        Long idUtilizador,
        String nome,
        String email,
        Boolean ativo,
        Boolean requerTrocaSenha,
        String perfil,
        OffsetDateTime criadoEm
) {
    public static UtilizadorResponse from(Utilizador utilizador) {
        return new UtilizadorResponse(
                utilizador.getIdUtilizador(),
                utilizador.getNome(),
                utilizador.getEmail(),
                utilizador.getAtivo(),
                utilizador.getRequerTrocaSenha(),
                utilizador.getPerfil() != null ? utilizador.getPerfil().getNome() : null,
                utilizador.getCriadoEm()
        );
    }
}