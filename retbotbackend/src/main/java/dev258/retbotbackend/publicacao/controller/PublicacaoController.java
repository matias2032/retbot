package dev258.retbotbackend.publicacao.controller;

import dev258.retbotbackend.publicacao.dto.*;
import dev258.retbotbackend.publicacao.entity.Agendamento;
import dev258.retbotbackend.publicacao.entity.Publicacao;
import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import dev258.retbotbackend.publicacao.service.PublicacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PublicacaoController {

    private final PublicacaoService publicacaoService;

    // ---------- Publicação ----------

    @PostMapping("/publicacoes")
    public ResponseEntity<PublicacaoResponse> criarPublicacao(@Valid @RequestBody CriarPublicacaoRequest request) {
        Publicacao publicacao = publicacaoService.criarPublicacao(
                request.idContaSocial(),
                request.idPublicacaoExterna(),
                request.texto(),
                request.publicadoEm()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toPublicacaoResponse(publicacao));
    }

    @GetMapping("/publicacoes/{id}")
    public ResponseEntity<PublicacaoResponse> buscarPublicacao(@PathVariable Long id) {
        return ResponseEntity.ok(toPublicacaoResponse(publicacaoService.buscarPublicacao(id)));
    }

    @GetMapping("/publicacoes/externa/{idExterno}")
    public ResponseEntity<PublicacaoResponse> buscarPorIdExterno(@PathVariable String idExterno) {
        return ResponseEntity.ok(toPublicacaoResponse(publicacaoService.buscarPorIdExterno(idExterno)));
    }

    @GetMapping("/publicacoes/conta/{idContaSocial}")
    public ResponseEntity<List<PublicacaoResponse>> listarPublicacoesPorConta(@PathVariable Long idContaSocial) {
        List<PublicacaoResponse> resposta = publicacaoService.listarPublicacoesPorContaSocial(idContaSocial)
                .stream().map(PublicacaoController::toPublicacaoResponse).toList();
        return ResponseEntity.ok(resposta);
    }

    // ---------- Agendamento ----------

    @PostMapping("/agendamentos")
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@Valid @RequestBody CriarAgendamentoRequest request) {
        Agendamento agendamento = publicacaoService.criarAgendamento(
                request.idContaSocial(),
                request.tipo(),
                request.idPublicacao(),
                request.executarEm(),
                request.prioridade()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toAgendamentoResponse(agendamento));
    }

    @GetMapping("/agendamentos/{id}")
    public ResponseEntity<AgendamentoResponse> buscarAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(toAgendamentoResponse(publicacaoService.buscarAgendamento(id)));
    }

    @GetMapping("/agendamentos/conta/{idContaSocial}")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentosPorConta(@PathVariable Long idContaSocial) {
        List<AgendamentoResponse> resposta = publicacaoService.listarAgendamentosPorContaSocial(idContaSocial)
                .stream().map(PublicacaoController::toAgendamentoResponse).toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/agendamentos/estado/{estado}")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentosPorEstado(@PathVariable EstadoAgendamento estado) {
        List<AgendamentoResponse> resposta = publicacaoService.listarAgendamentosPorEstado(estado)
                .stream().map(PublicacaoController::toAgendamentoResponse).toList();
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/agendamentos/{id}/estado")
    public ResponseEntity<AgendamentoResponse> atualizarEstado(@PathVariable Long id,
                                                                @Valid @RequestBody AtualizarEstadoAgendamentoRequest request) {
        Agendamento agendamento = publicacaoService.atualizarEstadoAgendamento(id, request.novoEstado());
        return ResponseEntity.ok(toAgendamentoResponse(agendamento));
    }

    @PatchMapping("/agendamentos/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelarAgendamento(@PathVariable Long id) {
        Agendamento agendamento = publicacaoService.cancelarAgendamento(id);
        return ResponseEntity.ok(toAgendamentoResponse(agendamento));
    }

    // ---------- Mapeamento Entity -> DTO ----------

    private static PublicacaoResponse toPublicacaoResponse(Publicacao p) {
        return new PublicacaoResponse(
                p.getIdPublicacao(),
                p.getContaSocial().getIdContaSocial(),
                p.getIdPublicacaoExterna(),
                p.getTexto(),
                p.getPublicadoEm(),
                p.getCriadoEm()
        );
    }

    private static AgendamentoResponse toAgendamentoResponse(Agendamento a) {
        return new AgendamentoResponse(
                a.getIdAgendamento(),
                a.getContaSocial().getIdContaSocial(),
                a.getTipo(),
                a.getPublicacao() != null ? a.getPublicacao().getIdPublicacao() : null,
                a.getExecutarEm(),
                a.getPrioridade(),
                a.getEstado(),
                a.getTentativas(),
                a.getCriadoEm()
        );
    }
}