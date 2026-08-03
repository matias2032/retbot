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

@RestController
@RequestMapping("/api/v1/utilizadores")
@RequiredArgsConstructor
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

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
}