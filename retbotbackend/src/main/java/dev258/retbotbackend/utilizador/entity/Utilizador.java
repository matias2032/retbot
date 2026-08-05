package dev258.retbotbackend.utilizador.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Corresponde à tabela 'utilizador'.
 * Raiz do agregado: um utilizador pode ter várias contas sociais (1:N).
 */
@Entity
@Table(name = "utilizador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilizador")
    private Long idUtilizador;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "requer_troca_senha", nullable = false)
    @Builder.Default
    private Boolean requerTrocaSenha = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "actualizado_em", nullable = false)
    private OffsetDateTime actualizadoEm;

    @OneToMany(mappedBy = "utilizador", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContaSocial> contasSociais = new ArrayList<>();

    @PrePersist
    protected void aoPersistir() {
        OffsetDateTime agora = OffsetDateTime.now();
        this.criadoEm = agora;
        this.actualizadoEm = agora;
    }

    @PreUpdate
    protected void aoAtualizar() {
        this.actualizadoEm = OffsetDateTime.now();
    }
}
