package dev258.retbotbackend.automacao.controller;

import dev258.retbotbackend.automacao.dto.ConsumirRateLimitRequest;
import dev258.retbotbackend.automacao.dto.CriarRateLimitRequest;
import dev258.retbotbackend.automacao.dto.ExecucaoResponse;
import dev258.retbotbackend.automacao.dto.FinalizarExecucaoRequest;
import dev258.retbotbackend.automacao.dto.IniciarExecucaoRequest;
import dev258.retbotbackend.automacao.dto.RateLimitResponse;
import dev258.retbotbackend.automacao.entity.Execucao;
import dev258.retbotbackend.automacao.entity.RateLimit;
import dev258.retbotbackend.automacao.service.AutomacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AutomacaoController {

    private final AutomacaoService automacaoService;

    // ---- Execuções ----

    @PostMapping("/api/v1/execucoes")
    public ResponseEntity<ExecucaoResponse> iniciarExecucao(@Valid @RequestBody IniciarExecucaoRequest request) {
        Execucao execucao = automacaoService.iniciarExecucao(request.idAgendamento(), request.requestId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(execucao));
    }

    @PatchMapping("/api/v1/execucoes/{id}/finalizar")
    public ResponseEntity<ExecucaoResponse> finalizarExecucao(
            @PathVariable Long id, @Valid @RequestBody FinalizarExecucaoRequest request) {
        Execucao execucao = automacaoService.finalizarExecucao(
                id, request.sucesso(), request.codigoHttp(), request.mensagem());
        return ResponseEntity.ok(toResponse(execucao));
    }

    @GetMapping("/api/v1/execucoes/agendamento/{idAgendamento}")
    public ResponseEntity<List<ExecucaoResponse>> listarPorAgendamento(@PathVariable Long idAgendamento) {
        List<ExecucaoResponse> respostas = automacaoService.listarExecucoesPorAgendamento(idAgendamento)
                .stream().map(AutomacaoController::toResponse).toList();
        return ResponseEntity.ok(respostas);
    }

    @GetMapping("/api/v1/execucoes/falhadas")
    public ResponseEntity<List<ExecucaoResponse>> listarFalhadas() {
        List<ExecucaoResponse> respostas = automacaoService.listarExecucoesFalhadas()
                .stream().map(AutomacaoController::toResponse).toList();
        return ResponseEntity.ok(respostas);
    }

    // ---- Rate Limits ----

    @PostMapping("/api/v1/rate-limits")
    public ResponseEntity<RateLimitResponse> obterOuCriarRateLimit(@Valid @RequestBody CriarRateLimitRequest request) {
        RateLimit rateLimit = automacaoService.obterOuCriarRateLimit(
                request.idContaSocial(), request.endpoint(), request.limite(), request.reiniciaEm());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(rateLimit));
    }

    @PostMapping("/api/v1/rate-limits/consumir")
    public ResponseEntity<RateLimitResponse> consumir(@Valid @RequestBody ConsumirRateLimitRequest request) {
        RateLimit rateLimit = automacaoService.consumir(request.idContaSocial(), request.endpoint());
        return ResponseEntity.ok(toResponse(rateLimit));
    }

    @GetMapping("/api/v1/rate-limits/conta/{idContaSocial}")
    public ResponseEntity<List<RateLimitResponse>> listarPorConta(@PathVariable Long idContaSocial) {
        List<RateLimitResponse> respostas = automacaoService.listarRateLimitsPorConta(idContaSocial)
                .stream().map(AutomacaoController::toResponse).toList();
        return ResponseEntity.ok(respostas);
    }

    // ---- Mapeamento ----

    private static ExecucaoResponse toResponse(Execucao e) {
        return new ExecucaoResponse(
                e.getIdExecucao(),
                e.getAgendamento().getIdAgendamento(),
                e.getIniciadoEm(),
                e.getTerminadoEm(),
                e.isSucesso(),
                e.getCodigoHttp(),
                e.getMensagem(),
                e.getRequestId()
        );
    }

    private static RateLimitResponse toResponse(RateLimit r) {
        return new RateLimitResponse(
                r.getContaSocial().getIdContaSocial(),
                r.getId().getEndpoint(),
                r.getLimite(),
                r.getRestante(),
                r.getReiniciaEm()
        );
    }
}