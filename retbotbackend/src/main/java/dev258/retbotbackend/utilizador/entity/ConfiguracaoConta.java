package dev258.retbotbackend.utilizador.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Corresponde à tabela 'configuracao_conta'.
 * Relação 1:1 com ContaSocial, partilhando a mesma chave primária
 * (id_conta_social é PK e também FK — padrão @MapsId).
 */
@Entity
@Table(name = "configuracao_conta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoConta {

    @Id
    @Column(name = "id_conta_social")
    private Long idContaSocial;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_conta_social")
    private ContaSocial contaSocial;

    @Column(name = "intervalo_min_segundos", nullable = false)
    @Builder.Default
    private Integer intervaloMinSegundos = 120;

    @Column(name = "max_acoes_15_min", nullable = false)
    @Builder.Default
    private Integer maxAcoes15Min = 50;

    @Column(name = "max_acoes_dia")
    private Integer maxAcoesDia;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
