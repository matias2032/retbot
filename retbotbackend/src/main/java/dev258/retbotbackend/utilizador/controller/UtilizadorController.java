package dev258.retbotbackend.utilizador.controller;

import dev258.retbotbackend.utilizador.dto.ActualizarConfiguracaoContaRequest;
import dev258.retbotbackend.utilizador.dto.ActualizarUtilizadorRequest;
import dev258.retbotbackend.utilizador.dto.AdicionarContaSocialRequest;
import dev258.retbotbackend.utilizador.dto.CriarUtilizadorRequest;
import dev258.retbotbackend.utilizador.dto.ConfiguracaoContaResponse;
import dev258.retbotbackend.utilizador.dto.ContaSocialResponse;
import dev258.retbotbackend.utilizador.dto.UtilizadorResponse;
import dev258.retbotbackend.utilizador.entity.ConfiguracaoConta;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.entity.Utilizador;
import dev258.retbotbackend.utilizador.service.UtilizadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev258.retbotbackend.integration.oauth.EstadoAutorizacaoOAuth;
import dev258.retbotbackend.integration.oauth.EstadoAutorizacaoStore;
import dev258.retbotbackend.integration.oauth.EstadoOAuthInvalidoException;
import dev258.retbotbackend.integration.oauth.OAuthClient;
import dev258.retbotbackend.integration.oauth.OAuthClientFactory;
import dev258.retbotbackend.integration.oauth.PerfilExternoOAuth;
import dev258.retbotbackend.integration.oauth.RegistoAplicacaoResponse;
import dev258.retbotbackend.integration.oauth.TokenOAuthResponse;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/utilizadores")
@RequiredArgsConstructor
public class UtilizadorController {

    private final UtilizadorService utilizadorService;
    private final OAuthClientFactory oAuthClientFactory;
    private final EstadoAutorizacaoStore estadoAutorizacaoStore;

    @Value("${app.oauth.callback-base-url}")
    private String callbackBaseUrl;

    @PostMapping
    public ResponseEntity<UtilizadorResponse> criar(@Valid @RequestBody CriarUtilizadorRequest request) {
        Utilizador utilizador = utilizadorService.criarUtilizador(
                request.nome(), request.email(), request.senha());

        return ResponseEntity.status(HttpStatus.CREATED).body(UtilizadorResponse.from(utilizador));
    }

    @GetMapping("/{idUtilizador}")
    public ResponseEntity<UtilizadorResponse> buscar(@PathVariable Long idUtilizador) {
        Utilizador utilizador = utilizadorService.buscarUtilizador(idUtilizador);
        return ResponseEntity.ok(UtilizadorResponse.from(utilizador));
    }

    @PutMapping("/{idUtilizador}")
    public ResponseEntity<UtilizadorResponse> actualizar(
            @PathVariable Long idUtilizador,
            @Valid @RequestBody ActualizarUtilizadorRequest request) {

        Utilizador utilizador = utilizadorService.actualizarUtilizador(
                idUtilizador, request.nome(), request.email());

        return ResponseEntity.ok(UtilizadorResponse.from(utilizador));
    }

    @PostMapping("/{idUtilizador}/contas")
    public ResponseEntity<ContaSocialResponse> adicionarContaSocial(
            @PathVariable Long idUtilizador,
            @Valid @RequestBody AdicionarContaSocialRequest request) {

        ContaSocial contaSocial = ContaSocial.builder()
                .plataforma(request.plataforma())
                .idUtilizadorPlataforma(request.idUtilizadorPlataforma())
                .username(request.username())
                .nomeExibicao(request.nomeExibicao())
                .accessToken(request.accessToken())
                .refreshToken(request.refreshToken())
                .build();

        ContaSocial criada = utilizadorService.adicionarContaSocial(idUtilizador, contaSocial);

        return ResponseEntity.status(HttpStatus.CREATED).body(ContaSocialResponse.from(criada));
    }

    @DeleteMapping("/contas/{idContaSocial}")
    public ResponseEntity<Void> removerContaSocial(@PathVariable Long idContaSocial) {
        utilizadorService.removerContaSocial(idContaSocial);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/contas/{idContaSocial}/configuracao")
    public ResponseEntity<ConfiguracaoContaResponse> actualizarConfiguracaoConta(
            @PathVariable Long idContaSocial,
            @Valid @RequestBody ActualizarConfiguracaoContaRequest request) {

        ConfiguracaoConta novaConfiguracao = ConfiguracaoConta.builder()
                .intervaloMinSegundos(request.intervaloMinSegundos())
                .maxAcoes15Min(request.maxAcoes15Min())
                .maxAcoesDia(request.maxAcoesDia())
                .ativo(request.ativo())
                .build();

        ConfiguracaoConta actualizada = utilizadorService.actualizarConfiguracaoConta(
                idContaSocial, novaConfiguracao);

        return ResponseEntity.ok(ConfiguracaoContaResponse.from(actualizada));
    }

    // ---------- OAuth de Contas Sociais ----------

    @GetMapping("/{idUtilizador}/contas-sociais/oauth/{plataforma}/iniciar")
    public ResponseEntity<Void> iniciarAutorizacaoOAuth(
            @PathVariable Long idUtilizador,
            @PathVariable PlataformaSocial plataforma,
            @RequestParam String urlInstancia) {

        OAuthClient oAuthClient = oAuthClientFactory.obterCliente(plataforma);
        String redirectUri = callbackBaseUrl + "/api/v1/utilizadores/oauth/callback/" + plataforma.name().toLowerCase();

        RegistoAplicacaoResponse app = oAuthClient.registrarAplicacao(urlInstancia, redirectUri);
        String state = estadoAutorizacaoStore.guardar(idUtilizador, plataforma, urlInstancia);
        String urlAutorizacao = oAuthClient.construirUrlAutorizacao(urlInstancia, app.clientId(), redirectUri, state);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlAutorizacao))
                .build();
    }

    @GetMapping("/oauth/callback/{plataforma}")
    public ResponseEntity<LigacaoContaSocialResponse> callbackOAuth(
            @PathVariable PlataformaSocial plataforma,
            @RequestParam String code,
            @RequestParam String state) {

        EstadoAutorizacaoOAuth estado = estadoAutorizacaoStore.consumir(state)
                .orElseThrow(() -> new EstadoOAuthInvalidoException(
                        "State inválido, expirado, ou já utilizado: " + state));

        OAuthClient oAuthClient = oAuthClientFactory.obterCliente(plataforma);
        String redirectUri = callbackBaseUrl + "/api/v1/utilizadores/oauth/callback/" + plataforma.name().toLowerCase();

        // Reutiliza a app já registada (cache no OAuthClient) — evita transportar
        // client_secret através do state, que viaja num URL.
        RegistoAplicacaoResponse app = oAuthClient.registrarAplicacao(estado.urlInstancia(), redirectUri);

        TokenOAuthResponse token = oAuthClient.trocarCodigoPorToken(
                estado.urlInstancia(), app.clientId(), app.clientSecret(), redirectUri, code);

        PerfilExternoOAuth perfil = oAuthClient.obterPerfil(estado.urlInstancia(), token.accessToken());

        ContaSocial contaSocial = ContaSocial.builder()
                .plataforma(plataforma)
                .idUtilizadorPlataforma(perfil.idExterno())
                .username(perfil.username())
                .nomeExibicao(perfil.nomeExibicao())
                .accessToken(token.accessToken())
                .urlInstancia(estado.urlInstancia())
                .build();

        ContaSocial guardada = utilizadorService.adicionarContaSocial(estado.idUtilizador(), contaSocial);

        LigacaoContaSocialResponse resposta = new LigacaoContaSocialResponse(
                guardada.getIdContaSocial(), guardada.getPlataforma(),
                guardada.getUsername(), guardada.getUrlInstancia());

        return ResponseEntity.ok(resposta);
    }

    private record LigacaoContaSocialResponse(
            Long idContaSocial, PlataformaSocial plataforma, String username, String urlInstancia
    ) {}
}