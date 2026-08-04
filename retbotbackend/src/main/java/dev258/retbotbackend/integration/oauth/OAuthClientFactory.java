package dev258.retbotbackend.integration.oauth;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuthClientFactory {

    private final List<OAuthClient> clientes;

    private Map<PlataformaSocial, OAuthClient> mapaPorPlataforma;

    @PostConstruct
    void construirMapa() {
        mapaPorPlataforma = new EnumMap<>(PlataformaSocial.class);

        for (PlataformaSocial plataforma : PlataformaSocial.values()) {
            List<OAuthClient> compativeis = clientes.stream()
                    .filter(c -> c.suporta(plataforma))
                    .toList();

            if (compativeis.size() > 1) {
                throw new IllegalStateException(
                        "Mais de um OAuthClient declara suportar a plataforma " + plataforma + ": " +
                        compativeis.stream().map(c -> c.getClass().getSimpleName())
                                .collect(Collectors.joining(", ")));
            }
            if (compativeis.size() == 1) {
                mapaPorPlataforma.put(plataforma, compativeis.get(0));
            }
        }
    }

    public OAuthClient obterCliente(PlataformaSocial plataforma) {
        OAuthClient cliente = mapaPorPlataforma.get(plataforma);
        if (cliente == null) {
            throw new OAuthClientNaoEncontradoException(
                    "Nenhum OAuthClient disponível para a plataforma: " + plataforma);
        }
        return cliente;
    }
}