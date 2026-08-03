package dev258.retbotbackend.publicacao.repository;

import dev258.retbotbackend.publicacao.entity.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {

    Optional<Publicacao> findByIdPublicacaoExterna(String idPublicacaoExterna);

    boolean existsByIdPublicacaoExterna(String idPublicacaoExterna);

    List<Publicacao> findByContaSocial_IdContaSocial(Long idContaSocial);
}