package dev258.retbotbackend.utilizador.repository;

import dev258.retbotbackend.utilizador.entity.ConfiguracaoConta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoContaRepository extends JpaRepository<ConfiguracaoConta, Long> {
    // A chave é a mesma de ContaSocial (id_conta_social via @MapsId),
    // por isso findById(idContaSocial) já cobre a busca principal.
}