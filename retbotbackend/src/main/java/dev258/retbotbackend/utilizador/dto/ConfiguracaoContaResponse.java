package dev258.retbotbackend.utilizador.dto;

import dev258.retbotbackend.utilizador.entity.ConfiguracaoConta;

public record ConfiguracaoContaResponse(
        Long idContaSocial,
        Integer intervaloMinSegundos,
        Integer maxAcoes15Min,
        Integer maxAcoesDia,
        Boolean ativo
) {
    public static ConfiguracaoContaResponse from(ConfiguracaoConta configuracaoConta) {
        return new ConfiguracaoContaResponse(
                configuracaoConta.getIdContaSocial(),
                configuracaoConta.getIntervaloMinSegundos(),
                configuracaoConta.getMaxAcoes15Min(),
                configuracaoConta.getMaxAcoesDia(),
                configuracaoConta.getAtivo()
        );
    }
}