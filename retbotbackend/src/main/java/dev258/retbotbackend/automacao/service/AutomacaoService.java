package dev258.retbotbackend.automacao.service;

import dev258.retbotbackend.automacao.entity.Execucao;
import dev258.retbotbackend.automacao.entity.RateLimit;
import dev258.retbotbackend.automacao.entity.RateLimitId;
import dev258.retbotbackend.automacao.exception.ExecucaoNaoEncontradaException;
import dev258.retbotbackend.automacao.exception.RateLimitExcedidoException;
import dev258.retbotbackend.automacao.exception.RateLimitNaoEncontradoException;
import dev258.retbotbackend.automacao.repository.ExecucaoRepository;
import dev258.retbotbackend.automacao.repository.RateLimitRepository;
import dev258.retbotbackend.publicacao.entity.Agendamento;
import dev258.retbotbackend.publicacao.exception.AgendamentoNaoEncontradoException;
import dev258.retbotbackend.publicacao.repository.AgendamentoRepository;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.exception.ContaSocialException;
import dev258.retbotbackend.utilizador.repository.ContaSocialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomacaoService {

    private final ExecucaoRepository execucaoRepository;
    private final RateLimitRepository rateLimitRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ContaSocialRepository contaSocialRepository;

    // ---- Execução ----

    public Execucao iniciarExecucao(Long idAgendamento, String requestId) {
        Agendamento agendamento = agendamentoRepository.findById(idAgendamento)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(
                        "Agendamento com id " + idAgendamento + " não encontrado"));

        Execucao execucao = Execucao.builder()
                .agendamento(agendamento)
                .iniciadoEm(OffsetDateTime.now())
                .sucesso(false)
                .requestId(requestId)
                .build();
        return execucaoRepository.save(execucao);
    }

    public Execucao finalizarExecucao(Long idExecucao, boolean sucesso, Integer codigoHttp, String mensagem) {
        Execucao execucao = execucaoRepository.findById(idExecucao)
                .orElseThrow(() -> new ExecucaoNaoEncontradaException(
                        "Execução com id " + idExecucao + " não encontrada"));

        execucao.setTerminadoEm(OffsetDateTime.now());
        execucao.setSucesso(sucesso);
        execucao.setCodigoHttp(codigoHttp);
        execucao.setMensagem(mensagem);

        return execucaoRepository.save(execucao);
    }

    public List<Execucao> listarExecucoesPorAgendamento(Long idAgendamento) {
        return execucaoRepository.findByAgendamento_IdAgendamentoOrderByIniciadoEmDesc(idAgendamento);
    }

    public List<Execucao> listarExecucoesFalhadas() {
        return execucaoRepository.findBySucessoFalseOrderByIniciadoEmDesc();
    }

    // ---- Rate Limit ----

public RateLimit obterOuCriarRateLimit(Long idContaSocial, String endpoint,
                                            int limitePadrao, OffsetDateTime reinicioPadrao) {
        ContaSocial contaSocial = contaSocialRepository.findById(idContaSocial)
                .orElseThrow(() -> new ContaSocialException(
                        "Conta social com id " + idContaSocial + " não encontrada", HttpStatus.NOT_FOUND));

        RateLimitId id = new RateLimitId(contaSocial.getIdContaSocial(), endpoint);

        return rateLimitRepository.findById(id)
                .orElseGet(() -> rateLimitRepository.save(
                        RateLimit.builder()
                                .id(id)
                                .contaSocial(contaSocial)
                                .limite(limitePadrao)
                                .restante(limitePadrao)
                                .reiniciaEm(reinicioPadrao)
                                .build()));
    }

    public RateLimit consumir(Long idContaSocial, String endpoint) {
        RateLimit rateLimit = rateLimitRepository.findByIdIdContaSocialAndIdEndpoint(idContaSocial, endpoint)
                .orElseThrow(() -> new RateLimitNaoEncontradoException(
                        "Rate limit não encontrado para conta " + idContaSocial + " e endpoint " + endpoint));

        if (OffsetDateTime.now().isAfter(rateLimit.getReiniciaEm())) {
            rateLimit.setRestante(rateLimit.getLimite());
            // TODO: confirmar duração da janela para recalcular reiniciaEm
            // (15 min? 24h? depende do endpoint/plataforma)
        }

        if (rateLimit.getRestante() <= 0) {
            throw new RateLimitExcedidoException(
                    "Limite de ações excedido para o endpoint " + endpoint
                            + ", reinicia em " + rateLimit.getReiniciaEm());
        }

        rateLimit.setRestante(rateLimit.getRestante() - 1);
        return rateLimitRepository.save(rateLimit);
    }

    public List<RateLimit> listarRateLimitsPorConta(Long idContaSocial) {
        return rateLimitRepository.findByContaSocial_IdContaSocial(idContaSocial);
    }
}