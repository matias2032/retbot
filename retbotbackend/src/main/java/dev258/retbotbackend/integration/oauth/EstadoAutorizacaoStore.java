package dev258.retbotbackend.integration.oauth;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Guarda em memória o contexto de um pedido OAuth entre o redirecionamento
 * inicial e o callback. O "state" funciona também como proteção CSRF.
 * PENDENTE: sem persistência nem TTL — perde-se ao reiniciar a aplicação.
 */
@Component
public class EstadoAutorizacaoStore {

    private final ConcurrentMap<String, EstadoAutorizacaoOAuth> estados = new ConcurrentHashMap<>();

    public String guardar(Long idUtilizador, PlataformaSocial plataforma, String urlInstancia) {
        String state = UUID.randomUUID().toString();
        estados.put(state, new EstadoAutorizacaoOAuth(idUtilizador, plataforma, urlInstancia));
        return state;
    }

    public Optional<EstadoAutorizacaoOAuth> consumir(String state) {
        return Optional.ofNullable(estados.remove(state));
    }
}