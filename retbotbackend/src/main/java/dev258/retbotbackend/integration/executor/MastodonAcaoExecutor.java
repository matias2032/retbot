package dev258.retbotbackend.integration.executor;

import dev258.retbotbackend.integration.client.ApiClienteException;
import dev258.retbotbackend.integration.client.ApiClient;
import dev258.retbotbackend.publicacao.enums.TipoAcao;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MastodonAcaoExecutor implements AcaoExecutor {

    private final ApiClient apiClient;

    @Override
    public boolean suporta(PlataformaSocial plataforma) {
        return plataforma == PlataformaSocial.MASTODON;
    }

    @Override
    public ExecutarAcaoResponse executar(ExecutarAcaoRequest pedido) {
        if (pedido.tipoAcao() != TipoAcao.PUBLICAR) {
            return ExecutarAcaoResponse.falha(null,
                    "MastodonAcaoExecutor ainda só suporta PUBLICAR. Tipo pedido: " + pedido.tipoAcao(), null);
        }

        try {
            StatusRequest corpo = new StatusRequest(pedido.conteudo());
            StatusResponse resposta = apiClient.postJson(
                    pedido.urlInstancia() + "/api/v1/statuses",
                    corpo,
                    pedido.accessToken(),
                    StatusResponse.class);

            return ExecutarAcaoResponse.sucesso(200, resposta.id(), null);

        } catch (ApiClienteException ex) {
            return ExecutarAcaoResponse.falha(ex.getStatusCode(), ex.getMessage(), null);
        }
    }

    private record StatusRequest(String status) {}

    private record StatusResponse(String id) {}
}