package dev258.retbotbackend.utilizador.repository;

import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.enums.EstadoConta;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaSocialRepository extends JpaRepository<ContaSocial, Long> {

    List<ContaSocial> findByUtilizador_IdUtilizador(Long idUtilizador);

    Optional<ContaSocial> findByPlataformaAndIdUtilizadorPlataforma(
            PlataformaSocial plataforma,
            String idUtilizadorPlataforma
    );

    List<ContaSocial> findByEstado(EstadoConta estado);

    boolean existsByUtilizador_IdUtilizadorAndPlataforma(Long idUtilizador, PlataformaSocial plataforma);
}