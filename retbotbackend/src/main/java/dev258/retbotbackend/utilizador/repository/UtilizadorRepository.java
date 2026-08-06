package dev258.retbotbackend.utilizador.repository;

import dev258.retbotbackend.utilizador.entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    Optional<Utilizador> findByEmail(String email);

    boolean existsByEmail(String email);

    // Lista todos os utilizadores exceto os do perfil indicado (usado para excluir administradores)
    @Query("SELECT u FROM Utilizador u WHERE u.perfil IS NULL OR u.perfil.idPerfil <> :idPerfilExcluido")
    List<Utilizador> findAllExcluindoPerfil(@Param("idPerfilExcluido") Long idPerfilExcluido);
}