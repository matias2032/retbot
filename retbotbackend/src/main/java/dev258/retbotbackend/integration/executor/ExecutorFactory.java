package dev258.retbotbackend.integration.executor;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Roteador de AcaoExecutor: escolhe, em runtime, a implementação capaz de
 * tratar a plataforma pedida. O módulo automacao depende apenas desta
 * fábrica e da interface AcaoExecutor — nunca de uma implementação concreta.
 *
 * Spring injeta automaticamente todos os beans que implementam AcaoExecutor;
 * novos executores (ex: um por plataforma, ou um genérico) não exigem
 * alterações nesta classe.
 */
@Component
@RequiredArgsConstructor
public class ExecutorFactory {

    private final List<AcaoExecutor> executores;

    private Map<PlataformaSocial, AcaoExecutor> mapaPorPlataforma;

    /**
     * Constrói o mapa plataforma -> executor uma única vez, no arranque,
     * e falha cedo (fail-fast) se dois executores disserem suportar a
     * mesma plataforma — é um erro de configuração, não algo a ignorar.
     */
    @jakarta.annotation.PostConstruct
    void construirMapa() {
        mapaPorPlataforma = new java.util.EnumMap<>(PlataformaSocial.class);

        for (PlataformaSocial plataforma : PlataformaSocial.values()) {
            List<AcaoExecutor> compativeis = executores.stream()
                    .filter(executor -> executor.suporta(plataforma))
                    .toList();

            if (compativeis.size() > 1) {
                throw new IllegalStateException(
                        "Mais de um AcaoExecutor declara suportar a plataforma " + plataforma +
                        ": " + compativeis.stream().map(e -> e.getClass().getSimpleName())
                                .collect(Collectors.joining(", ")));
            }

            if (compativeis.size() == 1) {
                mapaPorPlataforma.put(plataforma, compativeis.get(0));
            }
        }
    }

    public AcaoExecutor obterExecutor(PlataformaSocial plataforma) {
        AcaoExecutor executor = mapaPorPlataforma.get(plataforma);
        if (executor == null) {
            throw new ExecutorNaoEncontradoException(
                    "Nenhum AcaoExecutor disponível para a plataforma: " + plataforma);
        }
        return executor;
    }
}