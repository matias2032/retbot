package dev258.retbotbackend.automacao.entity;

import dev258.retbotbackend.publicacao.entity.Agendamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "execucao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Execucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_execucao")
    private Long idExecucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agendamento", nullable = false)
    private Agendamento agendamento;

    @Column(name = "iniciado_em", nullable = false)
    private OffsetDateTime iniciadoEm;

    @Column(name = "terminado_em")
    private OffsetDateTime terminadoEm;

    @Column(nullable = false)
    private boolean sucesso;

    @Column(name = "codigo_http")
    private Integer codigoHttp;

    private String mensagem;

    @Column(name = "request_id")
    private String requestId;
}