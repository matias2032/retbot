package dev258.retbotbackend.integration.executor;

import dev258.retbotbackend.publicacao.enums.TipoAcao;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;

import java.util.Map;

public record ExecutarAcaoRequest(
        Long idContaSocial,
        PlataformaSocial plataforma,
        TipoAcao tipoAcao,
        String accessToken,
        String urlInstancia,               // NOVO — obrigatório para Mastodon, null nas restantes
        String conteudo,
        String idPublicacaoExternaAlvo,
        Map<String, String> metadados
) {}