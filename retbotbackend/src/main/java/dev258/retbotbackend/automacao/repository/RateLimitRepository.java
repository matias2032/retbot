package dev258.retbotbackend.automacao.repository;

import dev258.retbotbackend.automacao.entity.RateLimit;
import dev258.retbotbackend.automacao.entity.RateLimitId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RateLimitRepository extends JpaRepository<RateLimit, RateLimitId> {

    List<RateLimit> findByContaSocial_IdContaSocial(Long idContaSocial);

    Optional<RateLimit> findByIdIdContaSocialAndIdEndpoint(Long idContaSocial, String endpoint);
}