package dev258.retbotbackend.publicacao.repository;

import dev258.retbotbackend.publicacao.entity.Agendamento;
import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByContaSocial_IdContaSocial(Long idContaSocial);

    List<Agendamento> findByEstado(EstadoAgendamento estado);

    List<Agendamento> findByEstadoAndExecutarEmLessThanEqual(EstadoAgendamento estado, OffsetDateTime momento);
}