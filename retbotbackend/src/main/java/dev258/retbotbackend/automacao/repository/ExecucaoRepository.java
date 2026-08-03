package dev258.retbotbackend.automacao.repository;

import dev258.retbotbackend.automacao.entity.Execucao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecucaoRepository extends JpaRepository<Execucao, Long> {

    List<Execucao> findByAgendamento_IdAgendamentoOrderByIniciadoEmDesc(Long idAgendamento);

    List<Execucao> findBySucessoFalseOrderByIniciadoEmDesc();
}