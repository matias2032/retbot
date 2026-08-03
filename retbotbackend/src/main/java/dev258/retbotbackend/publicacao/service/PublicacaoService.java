package dev258.retbotbackend.publicacao.service;

import dev258.retbotbackend.publicacao.entity.Agendamento;
import dev258.retbotbackend.publicacao.entity.Publicacao;
import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import dev258.retbotbackend.publicacao.enums.TipoAcao;
import dev258.retbotbackend.publicacao.exception.AgendamentoNaoEncontradoException;
import dev258.retbotbackend.publicacao.exception.PublicacaoNaoEncontradaException;
import dev258.retbotbackend.publicacao.repository.AgendamentoRepository;
import dev258.retbotbackend.publicacao.repository.PublicacaoRepository;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.exception.UtilizadorNaoEncontradoException;
import dev258.retbotbackend.utilizador.repository.ContaSocialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicacaoService {

    private final PublicacaoRepository publicacaoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ContaSocialRepository contaSocialRepository;

    // ---------- Publicação ----------

    public Publicacao criarPublicacao(Long idContaSocial, String idPublicacaoExterna,
                                       String texto, OffsetDateTime publicadoEm) {

        ContaSocial contaSocial = buscarContaSocial(idContaSocial);

        if (publicacaoRepository.existsByIdPublicacaoExterna(idPublicacaoExterna)) {
            throw new PublicacaoNaoEncontradaException(
                    "Já existe uma publicação com id externo: " + idPublicacaoExterna);
        }

        Publicacao publicacao = Publicacao.builder()
                .contaSocial(contaSocial)
                .idPublicacaoExterna(idPublicacaoExterna)
                .texto(texto)
                .publicadoEm(publicadoEm)
                .build();

        return publicacaoRepository.save(publicacao);
    }

    @Transactional(readOnly = true)
    public Publicacao buscarPublicacao(Long idPublicacao) {
        return publicacaoRepository.findById(idPublicacao)
                .orElseThrow(() -> new PublicacaoNaoEncontradaException(
                        "Publicação não encontrada: id=" + idPublicacao));
    }

    @Transactional(readOnly = true)
    public Publicacao buscarPorIdExterno(String idPublicacaoExterna) {
        return publicacaoRepository.findByIdPublicacaoExterna(idPublicacaoExterna)
                .orElseThrow(() -> new PublicacaoNaoEncontradaException(
                        "Publicação não encontrada: idExterno=" + idPublicacaoExterna));
    }

    @Transactional(readOnly = true)
    public List<Publicacao> listarPublicacoesPorContaSocial(Long idContaSocial) {
        return publicacaoRepository.findByContaSocial_IdContaSocial(idContaSocial);
    }

    // ---------- Agendamento ----------

    public Agendamento criarAgendamento(Long idContaSocial, TipoAcao tipo, Long idPublicacao,
                                         OffsetDateTime executarEm, Short prioridade) {

        ContaSocial contaSocial = buscarContaSocial(idContaSocial);

        Publicacao publicacao = null;
        if (idPublicacao != null) {
            publicacao = buscarPublicacao(idPublicacao);
        }

        Agendamento agendamento = Agendamento.builder()
                .contaSocial(contaSocial)
                .tipo(tipo)
                .publicacao(publicacao)
                .executarEm(executarEm)
                .prioridade(prioridade != null ? prioridade : 0)
                .build();

        return agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public Agendamento buscarAgendamento(Long idAgendamento) {
        return agendamentoRepository.findById(idAgendamento)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(
                        "Agendamento não encontrado: id=" + idAgendamento));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosPorContaSocial(Long idContaSocial) {
        return agendamentoRepository.findByContaSocial_IdContaSocial(idContaSocial);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosPorEstado(EstadoAgendamento estado) {
        return agendamentoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPendentesParaExecutar(OffsetDateTime momento) {
        return agendamentoRepository.findByEstadoAndExecutarEmLessThanEqual(
                EstadoAgendamento.PENDENTE, momento);
    }

    public Agendamento atualizarEstadoAgendamento(Long idAgendamento, EstadoAgendamento novoEstado) {
        Agendamento agendamento = buscarAgendamento(idAgendamento);
        agendamento.setEstado(novoEstado);
        return agendamento;
    }

    public Agendamento incrementarTentativas(Long idAgendamento) {
        Agendamento agendamento = buscarAgendamento(idAgendamento);
        agendamento.setTentativas((short) (agendamento.getTentativas() + 1));
        return agendamento;
    }

    public Agendamento cancelarAgendamento(Long idAgendamento) {
        return atualizarEstadoAgendamento(idAgendamento, EstadoAgendamento.CANCELADO);
    }

    // ---------- Auxiliar privado, partilhado pelas duas secções ----------

    private ContaSocial buscarContaSocial(Long idContaSocial) {
        return contaSocialRepository.findById(idContaSocial)
                .orElseThrow(() -> new UtilizadorNaoEncontradoException(
                        "Conta social não encontrada: id=" + idContaSocial));
    }
}