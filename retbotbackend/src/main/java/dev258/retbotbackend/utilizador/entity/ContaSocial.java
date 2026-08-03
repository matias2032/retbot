package dev258.retbotbackend.utilizador.entity;

import dev258.retbotbackend.utilizador.enums.EstadoConta;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * Corresponde à tabela 'conta_social'.
 * Liga um Utilizador a uma conta numa plataforma externa (X, Bluesky, etc).
 * Constraint uk_plataforma_user (plataforma, id_utilizador_plataforma) garante
 * que a mesma conta externa não é ligada duas vezes.
 */
@Entity
@Table(
        name = "conta_social",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_plataforma_user",
                columnNames = {"plataforma", "id_utilizador_plataforma"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta_social")
    private Long idContaSocial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilizador", nullable = false)
    private Utilizador utilizador;

    @Enumerated(EnumType.STRING)
    @Column(name = "plataforma", nullable = false, length = 20)
    private PlataformaSocial plataforma;

    @Column(name = "id_utilizador_plataforma", length = 80, nullable = false)
    private String idUtilizadorPlataforma;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "nome_exibicao", length = 150)
    private String nomeExibicao;

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "token_expira_em")
    private OffsetDateTime tokenExpiraEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoConta estado = EstadoConta.ATIVA;

    @Column(name = "ultimo_sync")
    private OffsetDateTime ultimoSync;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @OneToOne(mappedBy = "contaSocial", cascade = CascadeType.ALL, orphanRemoval = true)
    private ConfiguracaoConta configuracaoConta;

    @PrePersist
    protected void aoPersistir() {
        this.criadoEm = OffsetDateTime.now();
    }
}
