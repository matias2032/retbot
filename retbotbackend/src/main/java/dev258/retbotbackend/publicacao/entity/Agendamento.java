package dev258.retbotbackend.publicacao.entity;

import dev258.retbotbackend.publicacao.enums.EstadoAgendamento;
import dev258.retbotbackend.publicacao.enums.TipoAcao;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long idAgendamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conta_social", nullable = false)
    private ContaSocial contaSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAcao tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacao")
    private Publicacao publicacao;

    @Column(name = "executar_em", nullable = false)
    private OffsetDateTime executarEm;

    @Column(name = "prioridade", nullable = false)
    @Builder.Default
    private Short prioridade = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoAgendamento estado = EstadoAgendamento.PENDENTE;

    @Column(name = "tentativas", nullable = false)
    @Builder.Default
    private Short tentativas = 0;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    protected void aoPersistir() {
        if (criadoEm == null) {
            criadoEm = OffsetDateTime.now();
        }
    }
}